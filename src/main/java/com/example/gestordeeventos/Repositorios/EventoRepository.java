package com.example.gestordeeventos.repositorios;

import com.example.gestordeeventos.entidades.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;



@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    /**
     * Inserta un nuevo evento en la base de datos.
     * 
     * @param nombre              Nombre del evento.
     * @param fechaInicio         Fecha de inicio en formato 'YYYY-MM-DD'.
     * @param fechaFin            Fecha de fin en formato 'YYYY-MM-DD'.
     * @param boletosDisponibles  Cantidad de boletos disponibles.
     */
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO evento (nombre, fecha_inicio, fecha_fin, boletos_disponibles) " +
                   "VALUES (:nombre, :fechaInicio, :fechaFin, :boletosDisponibles)", nativeQuery = true)
    void crearEvento(@Param("nombre") String nombre,
                     @Param("fechaInicio") String fechaInicio,
                     @Param("fechaFin") String fechaFin,
                     @Param("boletosDisponibles") int boletosDisponibles);



 @Modifying
@Transactional
@Query("UPDATE Evento e SET e.nombre = :nombre, e.fechaInicio = :fechaInicio, e.fechaFin = :fechaFin, e.boletosDisponibles = :boletosDisponibles WHERE e.id = :id")
void actualizarEvento(@Param("id") Long id, 
                      @Param("nombre") String nombre,
                      @Param("fechaInicio") String fechaInicio, 
                      @Param("fechaFin") String fechaFin, 
                      @Param("boletosDisponibles") int boletosDisponibles);





    @Modifying
    @Query("DELETE FROM Evento e WHERE e.id = :id AND (e.fechaFin < CURRENT_DATE OR e.boletosVendidos = 0)")
    int borrarEvento(@Param("id") Long id);



 @Query("SELECT e FROM Evento e WHERE e.id = :id")
    Optional<Evento> findById(@Param("id") Long id);

   

   @Modifying
@Transactional
@Query("UPDATE Evento e SET e.boletosDisponibles = e.boletosDisponibles - 1, e.boletosVendidos = e.boletosVendidos + 1 WHERE e.id = :id AND e.boletosDisponibles > 0")
int venderBoleto(@Param("id") Long id);



@Modifying
@Transactional
@Query("UPDATE Evento e SET e.boletosDisponibles = e.boletosDisponibles - 1, e.boletosCanjeados = e.boletosCanjeados + 1 WHERE e.id = :id AND e.fechaInicio <= CURRENT_DATE AND e.fechaFin >= CURRENT_DATE AND e.boletosVendidos > e.boletosCanjeados")
int canjearBoleto(@Param("id") Long id);


}
