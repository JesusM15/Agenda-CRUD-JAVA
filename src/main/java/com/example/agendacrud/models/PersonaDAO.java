package com.example.agendacrud.models;

import com.example.agendacrud.database;
import com.mysql.cj.xdevapi.Result;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO implements ICrud<Persona> {
    @Override
    public boolean crear(Persona persona) {
        String sql = "INSERT INTO personas (nombre) VALUES (?)";

        try (Connection conn = database.conectar()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // preparedStatement cuando se usan parametros.
            preparedStatement.setString(1, persona.getNombre()); // indice del parametro + valor.
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                persona.setId(rs.getInt(1));
            }
            return true;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM personas WHERE id = (?)";


        try (Connection connection = database.conectar()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();


            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizar(Persona persona) {
        String sql = "UPDATE personas SET nombre = ? WHERE id = ?";

        try (Connection connection = database.conectar()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, persona.getNombre());
            preparedStatement.setInt(2, persona.getId());

            preparedStatement.executeUpdate();
            return true;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Persona> obtenerTodos() {
        String sql = "SELECT DISTINCT p.id, p.nombre FROM personas p";
        ArrayList<Persona> result = new ArrayList<Persona>();

        try (Connection conn = database.conectar()){
            Statement smt = conn.createStatement();
            ResultSet rs = smt.executeQuery(sql);

            while (rs.next()) {
                Persona persona = new Persona(rs.getInt("id"), rs.getString("nombre"));
                result.add(persona);
                System.out.println("ID: " + rs.getInt("id") + " Nombre: " + rs.getString("nombre"));
            }

            return result;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
