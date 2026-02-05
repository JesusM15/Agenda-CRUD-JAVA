package com.example.agendacrud.models;

import java.util.ArrayList;
import java.util.List;

public class Persona {
    private int id;
    private String nombre;
    private List<Direccion> direcciones;

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    public Persona() {
        this.id = -1;
        this.nombre = null;
        this.direcciones = new ArrayList<>(); // Inicializamos la lista vacía
   }

    public Persona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.direcciones = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
