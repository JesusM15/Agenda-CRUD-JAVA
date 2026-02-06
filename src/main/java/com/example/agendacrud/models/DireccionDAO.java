package com.example.agendacrud.models;

import com.example.agendacrud.database;
import com.mysql.cj.xdevapi.Result;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DireccionDAO {

    public ArrayList<Direccion> obtenerDirecciones(){
        ArrayList<Direccion> direcciones = new ArrayList<>();
        String sql = "SELECT * FROM direcciones";

        try (Connection conn = database.conectar()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                Direccion direccion = new Direccion(rs.getInt("id"), rs.getString("direccion"));
                direcciones.add(direccion);
            }

            return direcciones;
        } catch(SQLException e){
            e.printStackTrace();
        }

        return direcciones;
    }

    public ArrayList<Direccion> obtenerDireccionesByUserId(int id){
        ArrayList<Direccion> direcciones = new ArrayList<>();
        String sql = "SELECT * FROM direcciones WHERE id IN " +
                "(SELECT direccion_id FROM persona_direccion WHERE persona_id = ?)";

        try (Connection conn = database.conectar()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                Direccion direccion = new Direccion(rs.getInt("id"), rs.getString("direccion"));
                direcciones.add(direccion);
            }

            return direcciones;
        } catch(SQLException e){
            e.printStackTrace();
        }

        return direcciones;
    }

    public boolean crearDireccion(Direccion direccion) {
        String sql = "INSERT INTO direcciones (direccion) VALUES (?)";

        try(Connection conn = database.conectar()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, direccion.getDireccion());
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                direccion.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean vincularDireccion(Direccion direccion, Persona persona) {
        String sql = "INSERT INTO persona_direccion (persona_id, direccion_id) VALUES (?, ?)";

        try (Connection conn = database.conectar()){
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, persona.getId());
            preparedStatement.setInt(2, direccion.getId());

            preparedStatement.executeUpdate();
            return true;
        } catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean editarDireccion(Direccion direccion){
        String sql = "UPDATE direcciones SET direccion = ? WHERE id = ?";

        try (Connection conn = database.conectar()){
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, direccion.getDireccion());
            preparedStatement.setInt(2, direccion.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }




}
