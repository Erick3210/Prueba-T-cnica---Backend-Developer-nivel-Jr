package com.example.gestordeeventos;

import com.example.gestordeeventos.entidades.Evento;
import com.example.gestordeeventos.repositorios.EventoRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Component
public class Query implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final EventoRepository eventoRepository;

    public Query(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    // Consulta para listar eventos
    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    // Mutación para actualizar un evento con validaciones de negocio
    public Evento actualizarEvento(Long id, String nombre, String fechaInicio, String fechaFin, Integer boletosDisponibles) {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);

        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();

            // Validar fechas si se proporcionan
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fechaActual = LocalDate.now();
            LocalDate nuevaFechaInicio = null;
            LocalDate nuevaFechaFin = null;

            if (fechaInicio != null) {
                nuevaFechaInicio = LocalDate.parse(fechaInicio, formatter);
                if (nuevaFechaInicio.isBefore(fechaActual)) {
                    throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a la fecha actual.");
                }
            }

            if (fechaFin != null) {
                nuevaFechaFin = LocalDate.parse(fechaFin, formatter);
                if (nuevaFechaInicio != null && nuevaFechaFin.isBefore(nuevaFechaInicio)) {
                    throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
                }
            }

            // Validar boletos disponibles
            if (boletosDisponibles != null) {
                if (boletosDisponibles < evento.getBoletosVendidos()) {
                    throw new IllegalArgumentException("No puedes establecer una cantidad de boletos disponibles menor a los vendidos.");
                }
            }

            // Actualizar los valores del evento
            if (nombre != null) evento.setNombre(nombre);
            if (fechaInicio != null) evento.setFechaInicio(fechaInicio);
            if (fechaFin != null) evento.setFechaFin(fechaFin);
            if (boletosDisponibles != null) evento.setBoletosDisponibles(boletosDisponibles);

            return eventoRepository.save(evento);
        } else {
            throw new RuntimeException("Evento no encontrado con el ID " + id);
        }
    }


    public String borrarEvento(Long id) {
        // Buscar el evento por ID
        Optional<Evento> eventoOpt = eventoRepository.findById(id);

        if (eventoOpt.isPresent()) {
            Evento evento = eventoOpt.get();

            // Obtener la fecha actual y la fecha de fin del evento
            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaFin = LocalDate.parse(evento.getFechaFin(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Validar si se puede borrar el evento
            if (fechaActual.isAfter(fechaFin) || evento.getBoletosVendidos() == 0) {
                eventoRepository.deleteById(id);
                return "Evento borrado correctamente";
            } else {
                return "No se puede borrar el evento porque aún está vigente o tiene boletos vendidos";
            }
        } else {
            return "Evento no encontrado";
        }
    }



    // Mutación para vender boletos
    public Evento venderBoleto(Long eventoId, int cantidad) {
        Optional<Evento> eventoOptional = eventoRepository.findById(eventoId);

        if (eventoOptional.isEmpty()) {
            throw new RuntimeException("Evento no encontrado");
        }

        Evento evento = eventoOptional.get();

        if (evento.getBoletosDisponibles() < cantidad) {
            throw new RuntimeException("No hay suficientes boletos disponibles");
        }

        // Actualizar los datos del evento
        evento.setBoletosDisponibles(evento.getBoletosDisponibles() - cantidad);
        evento.setBoletosVendidos(evento.getBoletosVendidos() + cantidad);

        // Guardar el evento actualizado en la base de datos
        return eventoRepository.save(evento);
    }







public Evento canjearBoleto(Long eventoId, Long boletoId) {
    try {
        // Buscar el evento
        Optional<Evento> eventoOptional = eventoRepository.findById(eventoId);
        if (eventoOptional.isEmpty()) {
            throw new RuntimeException("Evento no encontrado con el ID: " + eventoId);
        }

        Evento evento = eventoOptional.get();

        // Validar fechas del evento
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicio = LocalDate.parse(evento.getFechaInicio(), formatter);
        LocalDate fechaFin = LocalDate.parse(evento.getFechaFin(), formatter);

        if (fechaActual.isBefore(fechaInicio)) {
            throw new RuntimeException("El evento aún no ha comenzado. Fecha de inicio: " + evento.getFechaInicio());
        }

        if (fechaActual.isAfter(fechaFin)) {
            throw new RuntimeException("El evento ya ha finalizado. Fecha de fin: " + evento.getFechaFin());
        }

        // Verificar disponibilidad de boletos para canjear
        if (evento.getBoletosVendidos() == 0) {
            throw new RuntimeException("No hay boletos vendidos para este evento que puedan ser canjeados.");
        }

        if (evento.getBoletosCanjeados() >= evento.getBoletosVendidos()) {
            throw new RuntimeException("Todos los boletos vendidos ya han sido canjeados.");
        }

        // Simular el canje del boleto
        evento.setBoletosCanjeados(evento.getBoletosCanjeados() + 1);

        // Guardar cambios en la base de datos
        return eventoRepository.save(evento);

    } catch (DateTimeParseException e) {
        throw new RuntimeException("Formato de fecha inválido para el evento. Use el formato 'yyyy-MM-dd'.", e);
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error al canjear boleto: " + e.getMessage());
    }
}



public Evento detalleEvento(Long id) {
    // Buscar el evento por ID
    Optional<Evento> eventoOptional = eventoRepository.findById(id);

    if (eventoOptional.isEmpty()) {
        throw new RuntimeException("Evento no encontrado con el ID " + id);
    }

    // Retornar el evento encontrado
    return eventoOptional.get();
}





}
