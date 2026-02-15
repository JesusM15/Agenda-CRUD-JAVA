package com.example.agendacrud;

import com.example.agendacrud.models.Direccion;
import com.example.agendacrud.models.ICrud;

import java.util.List;

public interface IDireccionDAO extends ICrud<Direccion> {

//    List<Direccion> obtenerPorUsuario(int personaID);
    boolean vincularAPersona(int direccionID, int personaID);
    boolean desvincularAPersona(int direccionID, int personaID);
}
