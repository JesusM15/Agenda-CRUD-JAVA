package com.example.agendacrud.models;

public class Telefono {
    private int idTelefono;
    private String numero;
    private int personaId;

    public Telefono(int idTelefono, String numero, int personaId) {
        this.numero = numero;
        this.personaId = personaId;
        this.idTelefono = idTelefono;
    }

    public int getPersonaId() {
        return personaId;
    }

    public void setPersonaId(int personaId) {
        this.personaId = personaId;
    }

    public Telefono(int idTelefono, String numero) {
        this.idTelefono = idTelefono;
        this.numero = numero;
    }

    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
