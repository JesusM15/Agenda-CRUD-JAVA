package com.example.agendacrud.models;

import java.util.List;

public interface ICrud<T> {
    boolean crear(T entidad);
    boolean eliminar(int id);
    boolean actualizar(T entidad);
    List<T> obtenerTodos();
}
