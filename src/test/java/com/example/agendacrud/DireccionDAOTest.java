package com.example.agendacrud;

import com.example.agendacrud.models.Direccion;
import com.example.agendacrud.models.DireccionDAO;
import com.example.agendacrud.models.Persona;
import com.example.agendacrud.models.PersonaDAO;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class DireccionDAOTest {

    @Test
    void testFlujoCompletoDireccion() {
        DireccionDAO direccionDAO = new DireccionDAO();
        PersonaDAO personaDAO = new PersonaDAO();

        Persona p = new Persona(-1, "Persona Test");
        personaDAO.crearPersona(p);

        Direccion d = new Direccion(-1, "Calle Unica 123");
        direccionDAO.crearDireccion(d);

        assertNotEquals(-1, d.getId(), "La dirección debe tener un ID generado");

        boolean vinculado = direccionDAO.vincularDireccion(d, p);
        assertTrue(vinculado, "La vinculación debe ser exitosa");

        ArrayList<Direccion> lista = direccionDAO.obtenerDireccionesByUserId(p.getId());
        assertTrue(lista.stream().anyMatch(dir -> dir.getDireccion().equals("Calle Unica 123")));

        d.setDireccion("Calle Editada 456");
        direccionDAO.editarDireccion(d);

        ArrayList<Direccion> listaGlobal = direccionDAO.obtenerDirecciones();
        assertTrue(listaGlobal.stream().anyMatch(dir -> dir.getDireccion().equals("Calle Editada 456")));

        direccionDAO.desvincularDireccion(d, p);
        direccionDAO.eliminarDireccion(d);
        personaDAO.eliminarPersona(p.getId());
    }
}