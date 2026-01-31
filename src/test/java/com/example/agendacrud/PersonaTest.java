package com.example.agendacrud;

import com.example.agendacrud.models.Persona;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonaTest {

    @Test
    void testConstructorYGetters() {
        Persona p = new Persona(10, "Esteban Quito", "Av. Siempre Viva 123");

        assertEquals(10, p.getId(), "El ID no coincide");
        assertEquals("Esteban Quito", p.getNombre(), "El nombre no coincide");
        assertEquals("Av. Siempre Viva 123", p.getDireccion(), "La dirección no coincide");
    }

    @Test
    void testSetters() {
        Persona p = new Persona();
        p.setId(5);
        p.setNombre("Alan Brito");
        p.setDireccion("Calle Falsa 123");

        assertAll("Verificación de setters",
                () -> assertEquals(5, p.getId()),
                () -> assertEquals("Alan Brito", p.getNombre()),
                () -> assertEquals("Calle Falsa 123", p.getDireccion())
        );
    }
}