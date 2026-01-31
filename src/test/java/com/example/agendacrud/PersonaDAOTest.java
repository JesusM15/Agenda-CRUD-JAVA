package com.example.agendacrud;

import com.example.agendacrud.models.Persona;
import com.example.agendacrud.models.PersonaDAO;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PersonaDAOTest {

    @Test
    void crearPersona() {
        PersonaDAO dao = new PersonaDAO();
        Persona p = new Persona(-1, "Juan Test", "Calle 1");

        dao.crearPersona(p);

        assertNotEquals(-1, p.getId());

        dao.eliminarPersona(p.getId());
    }

    @Test
    void editarPersona() {
        PersonaDAO dao = new PersonaDAO();

        Persona p = new Persona(-1, "Original", "Direccion");
        dao.crearPersona(p);

        p.setNombre("Editado");
        dao.editarPersona(p);

        ArrayList<Persona> lista = dao.consultarPersonas();
        boolean encontrado = lista.stream()
                .anyMatch(per -> per.getId() == p.getId() && per.getNombre().equals("Editado"));
        assertTrue(encontrado);

        dao.eliminarPersona(p.getId());
    }

    @Test
    void consultarPersonas() {
        PersonaDAO dao = new PersonaDAO();
        Persona p = new Persona(-1, "Consultar Test", "Direccion");
        dao.crearPersona(p);

        ArrayList<Persona> lista = dao.consultarPersonas();

        assertNotNull(lista);
        assertTrue(lista.size() > 0);

        dao.eliminarPersona(p.getId());
    }

    @Test
    void eliminarPersona() {
        PersonaDAO dao = new PersonaDAO();

        Persona p = new Persona(-1, "A Borrar", "Direccion");
        dao.crearPersona(p);

        dao.eliminarPersona(p.getId());

        ArrayList<Persona> lista = dao.consultarPersonas();
        boolean existe = lista.stream().anyMatch(per -> per.getId() == p.getId());
        assertFalse(existe);
    }
}