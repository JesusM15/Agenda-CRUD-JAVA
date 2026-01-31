package com.example.agendacrud;
import com.example.agendacrud.models.Telefono;
import com.example.agendacrud.models.TelefonoDAO;
import com.example.agendacrud.models.Persona;
import com.example.agendacrud.models.PersonaDAO;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TelefonoDAOTest {
    @Test
    void obtenerTelefonosPorPersona() {
        PersonaDAO personaDAO = new PersonaDAO();
        TelefonoDAO telefonoDAO = new TelefonoDAO();

        Persona p = new Persona(-1, "Usuario Test", "Direccion");
        personaDAO.crearPersona(p);

        Telefono t1 = new Telefono(-1, "111111", p.getId());
        Telefono t2 = new Telefono(-1, "222222", p.getId());
        telefonoDAO.registrarTelefono(t1);
        telefonoDAO.registrarTelefono(t2);

        ArrayList<Telefono> resultado = telefonoDAO.obtenerTelefonosPorUsuario(p.getId());

        assertNotNull(resultado, "La lista no debe ser nula");
        assertEquals(2, resultado.size(), "Debe retornar exactamente 2 teléfonos");
        assertEquals("111111", resultado.get(0).getNumero());

        telefonoDAO.eliminarTelefono(t1);
        telefonoDAO.eliminarTelefono(t2);
        personaDAO.eliminarPersona(p.getId());
    }

    @Test
    public void registrarTelefono() {
        PersonaDAO personaDAO = new PersonaDAO();
        TelefonoDAO telefonoDAO = new TelefonoDAO();

        Persona p = new Persona(-1, "Usuario Test", "Direccion");
        personaDAO.crearPersona(p);

        Telefono t = new Telefono(-1, "111111", p.getId());
        telefonoDAO.registrarTelefono(t);

        assertEquals("111111", t.getNumero(), "El numero no es igual");
        assertEquals(t.getPersonaId(), p.getId(), "El telefono no pertenece a la persona correcta");
        assertNotEquals(t.getIdTelefono(), -1, "El telefono no tiene un ID real");
        telefonoDAO.eliminarTelefono(t);
        personaDAO.eliminarPersona(p.getId());
    }

    @Test
    public void eliminarTelefono() {
        PersonaDAO personaDAO = new PersonaDAO();
        TelefonoDAO telefonoDAO = new TelefonoDAO();

        Persona p = new Persona(-1, "Usuario Test", "Direccion");
        personaDAO.crearPersona(p);
        Telefono t = new Telefono(-1, "111111", p.getId());
        telefonoDAO.registrarTelefono(t);

        telefonoDAO.eliminarTelefono(t);

        ArrayList<Telefono> resultado = telefonoDAO.obtenerTelefonosPorUsuario(p.getId());

        assertEquals(0, resultado.size(), "La lista debería estar vacía tras eliminar el único teléfono");

        personaDAO.eliminarPersona(p.getId());
    }

    @Test
    public void modificarTelefono() {
        PersonaDAO personaDAO = new PersonaDAO();
        TelefonoDAO telefonoDAO = new TelefonoDAO();

        Persona p = new Persona(-1, "Test Update", "Direccion");
        personaDAO.crearPersona(p);

        Telefono t = new Telefono(-1, "1111111111", p.getId());
        telefonoDAO.registrarTelefono(t);

        t.setNumero("2222222222");
        telefonoDAO.modificarTelefono(t);

        ArrayList<Telefono> lista = telefonoDAO.obtenerTelefonosPorUsuario(p.getId());
        assertEquals("2222222222", lista.get(0).getNumero(), "El número en la DB debería ser el nuevo");

        telefonoDAO.eliminarTelefono(t);
        personaDAO.eliminarPersona(p.getId());
    }

}
