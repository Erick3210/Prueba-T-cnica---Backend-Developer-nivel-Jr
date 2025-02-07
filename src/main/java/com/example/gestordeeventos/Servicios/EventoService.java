package com.example.gestordeeventos.servicios;

import com.example.gestordeeventos.entidades.Evento;
import com.example.gestordeeventos.repositorios.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    /**
     * Crear un nuevo evento en la base de datos.
     * 
     * @param evento Objeto evento que contiene los datos necesarios.
     * @return Evento registrado en la base de datos.
     * @throws IllegalArgumentException Si alguna validación falla.
     */
    public Evento crearEvento(Evento evento) {
        // Validar datos del evento
        validarDatosEvento(evento);

        // Realizar la inserción en la base de datos
        eventoRepository.crearEvento(
                evento.getNombre(),
                evento.getFechaInicio(),
                evento.getFechaFin(),
                evento.getBoletosDisponibles()
        );

        // Devolver el evento creado (simulado aquí, se podría mejorar retornando el ID generado)
        return evento;
    }

    /**
     * Validar los datos de un evento antes de crearlo.
     * 
     * @param evento Objeto evento a validar.
     * @throws IllegalArgumentException Si alguna validación falla.
     */
    private void validarDatosEvento(Evento evento) {
        // Validar que las fechas estén en formato correcto y coherentes
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaInicio;
        LocalDate fechaFin;

        try {
            fechaInicio = LocalDate.parse(evento.getFechaInicio(), formatter);
            fechaFin = LocalDate.parse(evento.getFechaFin(), formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Las fechas deben tener el formato 'yyyy-MM-dd'.");
        }

        // Validar que la fecha de inicio no sea anterior a la actual
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a la fecha actual.");
        }

        // Validar que la fecha de fin no sea anterior a la de inicio
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        // Validar que los boletos disponibles estén en el rango permitido
        if (evento.getBoletosDisponibles() < 1 || evento.getBoletosDisponibles() > 300) {
            throw new IllegalArgumentException("El número de boletos disponibles debe estar entre 1 y 300.");
        }
    }











 public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    /**
     * Actualizar un evento en la base de datos.
     *
     * @param evento Objeto evento que contiene los datos actualizados.
     * @return Evento actualizado.
     * @throws IllegalArgumentException Si alguna validación falla.
     */
    @Transactional
    public Evento actualizarEvento(Evento evento) {
        // Verificar si el evento existe
        Optional<Evento> eventoExistenteOpt = eventoRepository.findById(evento.getId());
        if (eventoExistenteOpt.isEmpty()) {
            throw new IllegalArgumentException("Evento no encontrado con el ID " + evento.getId());
        }

        Evento eventoExistente = eventoExistenteOpt.get();

        // Validar las fechas de inicio y fin
        LocalDate nuevaFechaInicio;
        LocalDate nuevaFechaFin;
        try {
            nuevaFechaInicio = LocalDate.parse(evento.getFechaInicio());
            nuevaFechaFin = LocalDate.parse(evento.getFechaFin());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Las fechas deben tener el formato 'yyyy-MM-dd'.");
        }

        // Verificar reglas de negocio para las fechas
        if (nuevaFechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a la fecha actual.");
        }

        if (nuevaFechaFin.isBefore(nuevaFechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        // Validar boletos disponibles
        if (evento.getBoletosDisponibles() < eventoExistente.getBoletosVendidos()) {
            throw new IllegalArgumentException("No puedes establecer un número de boletos disponibles menor a los vendidos.");
        }

        // Actualizar los datos del evento
        eventoExistente.setNombre(evento.getNombre());
        eventoExistente.setFechaInicio(evento.getFechaInicio());
        eventoExistente.setFechaFin(evento.getFechaFin());
        eventoExistente.setBoletosDisponibles(evento.getBoletosDisponibles());

        // Guardar el evento actualizado
        return eventoRepository.save(eventoExistente);
    }

    /**
     * Obtener un evento por su ID.
     *
     * @param id ID del evento.
     * @return Evento si existe, envuelto en un Optional.
     */
    public Optional<Evento> obtenerEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }






private static final Logger logger = LoggerFactory.getLogger(EventoService.class);

// Obtener todos los eventos
   public List<Evento> listarEventos() {
        return eventoRepository.findAll();  // Asegúrate de estar usando `Evento` y no `eventos`
    }




@Transactional
public void borrarEvento(Long id) {
    int filasAfectadas = eventoRepository.borrarEvento(id);
    if (filasAfectadas == 0) {
        throw new IllegalArgumentException("No se pudo borrar el evento. Puede que tenga boletos vendidos o aún no haya finalizado.");
    }
}









public void venderBoleto(Long id) {
    int rowsUpdated = eventoRepository.venderBoleto(id); // Llama al método del repositorio

    if (rowsUpdated == 0) {
        throw new IllegalArgumentException("No se pudo vender el boleto: evento no encontrado o sin boletos disponibles.");
    }
}





public void canjearBoleto(Long id) {
    int rowsUpdated = eventoRepository.canjearBoleto(id);
    if (rowsUpdated == 0) {
        throw new IllegalArgumentException("No se pudo canjear el boleto. Verifica las fechas o el estado del evento.");
    }
}




}
