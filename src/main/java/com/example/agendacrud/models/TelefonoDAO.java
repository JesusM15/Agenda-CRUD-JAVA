package com.example.agendacrud.models;

import com.example.agendacrud.database;
import java.sql.*;

import java.util.ArrayList;

public class TelefonoDAO {

    public ArrayList<Telefono> obtenerTelefonosPorUsuario(int user_id){
        String sql = "SELECT * FROM telefonos WHERE personaId = (?)";
        ArrayList<Telefono> telefonos = new ArrayList<>();

        try (Connection connection = database.conectar()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Telefono telefono = new Telefono(resultSet.getInt("id"), resultSet.getString("telefono"), resultSet.getInt("personaId"));
                System.out.println("telefono " + telefono.getNumero());
                telefonos.add(telefono);
            }

            return telefonos;
        } catch(SQLException e){
            e.printStackTrace();
            return telefonos;
        }
    }

    public void registrarTelefono(Telefono telefono){
        if (!Telefono.esValido(telefono.getNumero())) {
            throw new IllegalArgumentException("El formato del teléfono es inválido.");
        }

        String sql = "INSERT INTO telefonos(telefono, personaId) VALUES (?, ?)";

        try (Connection connection = database.conectar()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, telefono.getNumero());
            preparedStatement.setInt(2, telefono.getPersonaId());

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                telefono.setIdTelefono(rs.getInt(1));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void modificarTelefono(Telefono telefono){
        if (!Telefono.esValido(telefono.getNumero())) {
            throw new IllegalArgumentException("El formato del teléfono es inválido.");
        }
        String sql = "UPDATE telefonos SET telefono = ? WHERE id = ?";

        try (Connection connection = database.conectar()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, telefono.getNumero());
            preparedStatement.setInt(2, telefono.getIdTelefono());

            preparedStatement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void eliminarTelefono(Telefono telefono){
        String sql = "DELETE FROM telefonos WHERE id = ?";

        try (Connection connection = database.conectar()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, telefono.getIdTelefono());

            preparedStatement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
