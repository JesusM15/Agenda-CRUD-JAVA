package com.example.agendacrud;
import com.example.agendacrud.models.Telefono;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TelefonoTest {

    @Test
    void testConstructorYGetters() {
        Telefono t = new Telefono(1, "1234567890", 5);

        assertEquals(1, t.getIdTelefono());
        assertEquals("1234567890", t.getNumero());
        assertEquals(5, t.getPersonaId());
    }

    @Test
    void testValidacionNumerosValidos() {
        assertTrue(Telefono.esValido("1234567890"), "10 dígitos debería ser válido");
        assertTrue(Telefono.esValido("1"), "Un solo dígito debería ser válido");
    }

    @Test
    void testValidacionNumerosInvalidos() {
        assertFalse(Telefono.esValido("12345678901"), "Más de 10 dígitos debe ser inválido");
        assertFalse(Telefono.esValido("abc12345"), "Letras deben ser inválidas");
        assertFalse(Telefono.esValido(null), "Null debe ser inválido");
        assertFalse(Telefono.esValido(""), "Vacío debe ser inválido");
    }

}
