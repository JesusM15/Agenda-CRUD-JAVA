package com.example.agendacrud.models;

import com.example.agendacrud.database;
import com.mysql.cj.xdevapi.Result;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {
    public ArrayList<Persona> consultarPersonas(){
        String sql = "SELECT DISTINCT p.id, p.nombre, p.direccion FROM personas p";
        ArrayList<Persona> result = new ArrayList<Persona>();

        try (Connection conn = database.conectar()){
            Statement smt = conn.createStatement();
            ResultSet rs = smt.executeQuery(sql);

            while (rs.next()) {
                Persona persona = new Persona(rs.getInt("id"), rs.getString("nombre"), rs.getString("direccion"));
                result.add(persona);
                System.out.println("ID: " + rs.getInt("id") + " Nombre: " + rs.getString("nombre"));
            }

            return result;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void crearPersona(Persona persona){
        String sql = "INSERT INTO personas (nombre, direccion) VALUES (?, ?)";

        try (Connection conn = database.conectar()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // preparedStatement cuando se usan parametros.
            preparedStatement.setString(1, persona.getNombre()); // indice del parametro + valor.
            preparedStatement.setString(2, persona.getDireccion());
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                persona.setId(rs.getInt(1));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void eliminarPersona(int id){
        String sql = "DELETE FROM personas WHERE id = (?)";


        try (Connection connection = database.conectar()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarPersona(Persona persona){
        String sql = "UPDATE personas SET nombre = ?, direccion = ? WHERE id = ?";

        try (Connection connection = database.conectar()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, persona.getNombre());
            preparedStatement.setString(2, persona.getDireccion());
            preparedStatement.setInt(3, persona.getId());

            preparedStatement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
