package com.example.gestordeeventos.controlador;

import com.example.gestordeeventos.entidades.Evento;
import com.example.gestordeeventos.servicios.EventoService;
import com.example.gestordeeventos.repositorios.EventoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;




@Controller
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

      @Autowired
    private EventoRepository eventoRepository;

    /**
     * Método para mostrar el formulario de creación de eventos.
     * @param model Modelo para enviar un objeto evento vacío a la vista.
     * @return Nombre de la plantilla HTML a renderizar.
     */
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("evento", new Evento());  // Se envía un objeto evento vacío
        return "crear-evento";  // Nombre de la plantilla (crear-evento.html)
    }

    /**
     * Endpoint para crear un nuevo evento.
     * @param evento Objeto evento recibido desde el formulario.
     * @return Redirección a la página de éxito o mensaje de error en la vista.
     */
    @PostMapping("/crear")
    public String crearEvento(@ModelAttribute Evento evento, Model model) {
        try {
             eventoService.crearEvento(evento);
             model.addAttribute("successMessage", "El evento se registró exitosamente.");
        } catch (IllegalArgumentException e) {
             model.addAttribute("error", e.getMessage());
        }
          return "crear-evento";  // Retorna la misma vista con el mensaje
    }








  /**
     * Muestra el formulario para editar un evento existente.
     * @param id ID del evento a editar.
     * @param model Modelo para enviar el evento a la vista.
     * @return Nombre de la plantilla HTML a renderizar.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Optional<Evento> eventoOpt = eventoService.obtenerEventoPorId(id);
        if (eventoOpt.isEmpty()) {
            model.addAttribute("error", "Evento no encontrado");
            return "error";  // Nombre de la plantilla para errores
        }

        model.addAttribute("evento", eventoOpt.get());
        return "editar-evento";  // Nombre de la plantilla HTML (editar-evento.html)
    }

    /**
     * Actualiza un evento con las validaciones de negocio.
     * @param evento Objeto evento recibido desde el formulario.
     * @param model Modelo para enviar mensajes de éxito o error.
     * @return Redirección a la página de listado de eventos o formulario con mensaje.
     */
       @PostMapping("/actualizar")
@Transactional
public String actualizarEvento(@ModelAttribute Evento evento, Model model) {
    try {
        eventoService.actualizarEvento(evento);
        model.addAttribute("successMessage", "Evento actualizado correctamente");
        return "redirect:/api/eventos/listar"; // La redirección debe coincidir con la ruta del HTML
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        return "editar-evento";  // Retorna la misma vista con el mensaje de error
    } catch (Exception e) {
        e.printStackTrace();  // Log de la excepción para más detalles
        model.addAttribute("error", "Error inesperado: " + e.getMessage());
        return "editar-evento";
    }
}





  private static final Logger logger = LoggerFactory.getLogger(EventoController.class);

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/listar")
    public String listarEventos(Model model) {
        List<Evento> eventos = eventoService.listarEventos();  // Cambiar `eventos` por `Evento` si es necesario
        model.addAttribute("eventos", eventos);
        return "listar-eventos";
    }


@GetMapping("/borrar/{id}")
public String borrarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
        eventoService.borrarEvento(id);
        redirectAttributes.addFlashAttribute("successMessage", "Evento eliminado correctamente.");
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    }
    return "redirect:/api/eventos/listar";
}





@GetMapping("/vender/{id}")
public String mostrarVenderBoleto(@PathVariable Long id, Model model) {
    Evento evento = eventoService.obtenerEventoPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado."));
    
    model.addAttribute("evento", evento);
    return "vender-boleto";  // Asegúrate de que la vista "vender-boleto" esté configurada
}

@PostMapping("/vender-boleto/{id}")
public String venderBoleto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
        eventoService.venderBoleto(id);  // Llama al servicio
        redirectAttributes.addFlashAttribute("successMessage", "Boleto vendido exitosamente.");
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    }
    return "redirect:/api/eventos/vender/" + id;  // Redirige de vuelta al formulario
}









@GetMapping("/canjear/{id}")
public String mostrarCanjeBoleto(@PathVariable Long id, Model model) {
    Optional<Evento> optionalEvento = eventoService.obtenerEventoPorId(id);
    if (!optionalEvento.isPresent()) {
        throw new IllegalArgumentException("Evento no encontrado.");
    }
    Evento evento = optionalEvento.get();
    model.addAttribute("evento", evento);
    return "canjear-boleto";  // Página para mostrar los detalles del evento
}

@PostMapping("/canjear-boleto/{id}")
public String canjearBoleto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
        eventoService.canjearBoleto(id);
        redirectAttributes.addFlashAttribute("successMessage", "Boleto canjeado exitosamente.");
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    }
    return "redirect:/api/eventos/canjear/" + id; // Redirige a la misma página con el mensaje
}









}
