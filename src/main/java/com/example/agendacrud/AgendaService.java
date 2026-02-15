package com.example.agendacrud;

import com.example.agendacrud.models.*;
import java.util.List;

public class AgendaService {
    // Usamos las interfaces donde existan, y clases donde no (por ahora)
    private final PersonaDAO personaDAO;
    private final TelefonoDAO telefonoDAO;
    private final DireccionDAO direccionDAO;

    public AgendaService() {
        // En una etapa más avanzada (DIP), estos se recibirían por constructor
        this.personaDAO = new PersonaDAO();
        this.telefonoDAO = new TelefonoDAO();
        this.direccionDAO = new DireccionDAO();
    }

    // --- MÉTODOS DE PERSONA ---
    public List<Persona> obtenerTodasLasPersonas() {
        return personaDAO.obtenerTodos();
    }

    public boolean guardarPersona(Persona p) {
        return personaDAO.crear(p);
    }

    public boolean eliminarPersona(int id) {
        // Como tienes ON DELETE CASCADE, esto borrará teléfonos y vínculos de dirección automáticamente
        return personaDAO.eliminar(id);
    }

    public boolean actualizarPersona(Persona p) {
        return personaDAO.actualizar(p);
    }

    // --- MÉTODOS DE TELÉFONO ---
    public List<Telefono> getTelefonosPorUsuario(int userId) {
        return telefonoDAO.obtenerPorUsuario(userId);
    }

    public boolean registrarTelefono(Telefono t) {
        return telefonoDAO.crear(t);
    }

    public boolean eliminarTelefono(int id) {
        return telefonoDAO.eliminar(id);
    }

    public boolean editarTelefono(Telefono t) {
        return telefonoDAO.actualizar(t);
    }

    // --- MÉTODOS DE DIRECCIÓN ---
    public List<Direccion> getDireccionesPorUsuario(int userId) {
        return direccionDAO.obtenerPorUsuario(userId);
    }

    public List<Direccion> getTodasLasDirecciones() {
        return direccionDAO.obtenerTodos();
    }

    public boolean vincularDireccion(Direccion d, Persona p) {
        return direccionDAO.vincularAPersona(d.getId(), p.getId());
    }

    public boolean desvincularDireccion(Direccion d, Persona p) {
        return direccionDAO.desvincularAPersona(d.getId(), p.getId());
    }

    public boolean eliminarDireccion(int id) {
        return direccionDAO.eliminar(id);
    }

    public boolean actualizarDireccion(Direccion d) {
        return direccionDAO.actualizar(d);
    }

    public boolean crearDireccion(Direccion d) {
        return direccionDAO.crear(d);
    }
}