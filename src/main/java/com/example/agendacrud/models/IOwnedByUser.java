package com.example.agendacrud.models;

import java.util.List;

public interface IOwnedByUser<T> {
    List<T> obtenerPorUsuario(int userId);
}
