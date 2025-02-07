package com.example.gestordeeventos.entidades;

import javax.persistence.*;

@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 255)
    private String nombre;

    @Column(name = "fecha_inicio")
    private String fechaInicio;

    @Column(name = "fecha_fin")
    private String fechaFin;

    @Column(name = "boletos_disponibles")
    private Integer boletosDisponibles;

    @Column(name = "boletos_vendidos")
    private Integer boletosVendidos;

    @Column(name = "boletos_canjeados")
    private Integer boletosCanjeados;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getBoletosDisponibles() {
        return boletosDisponibles;
    }

    public void setBoletosDisponibles(Integer boletosDisponibles) {
        this.boletosDisponibles = boletosDisponibles;
    }

    public Integer getBoletosVendidos() {
        return boletosVendidos;
    }

    public void setBoletosVendidos(Integer boletosVendidos) {
        this.boletosVendidos = boletosVendidos;
    }

    public Integer getBoletosCanjeados() {
        return boletosCanjeados;
    }

    public void setBoletosCanjeados(Integer boletosCanjeados) {
        this.boletosCanjeados = boletosCanjeados;
    }
}
