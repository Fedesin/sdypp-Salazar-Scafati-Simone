package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostgresController {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public void insertUsuario(int userId, String ip, int port) {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);


            // Insert data into usuario table
            String usuarioInsert = "INSERT INTO usuario (id_usuario, ip, puerto) VALUES (?, ?, ?)";
            PreparedStatement usuarioStatement = conn.prepareStatement(usuarioInsert);
            usuarioStatement.setInt(1, userId);
            usuarioStatement.setString(2, ip);
            usuarioStatement.setInt(3, port);
            usuarioStatement.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            // Handle exceptions
        }
    }

    public void insertArchivo(int userId, String fileName) {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);

            // Insert data into archivo table
            String archivoInsert = "INSERT INTO archivo (id_usuario, nombre_archivo) VALUES (?, ?)";
            PreparedStatement archivoStatement = conn.prepareStatement(archivoInsert);
            archivoStatement.setInt(1,userId);
            archivoStatement.setString(2, fileName);
            System.out.println(archivoInsert);
            archivoStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }   
    }
     
    public String[] getUsuario(int userId) {
    String[] result = new String[2];
    try {
        Connection conn = DriverManager.getConnection(url, username, password);

        // Retrieve data from usuario table
        String usuarioSelect = "SELECT ip, puerto FROM usuario WHERE id_usuario = ?";
        PreparedStatement usuarioStatement = conn.prepareStatement(usuarioSelect);
        usuarioStatement.setInt(1, userId);
        ResultSet usuarioResult = usuarioStatement.executeQuery();
        if (usuarioResult.next()) {
            result[0] = usuarioResult.getString("ip");
            result[1] = usuarioResult.getInt("puerto") + "";
        } else {
           result[0] = "Usuario no existe";
            result[1] = "0";
        }

        usuarioResult.close();
         usuarioStatement.close();
         conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }   
    return result;
    }

    public String[] getUsuario(String filename) {
        String[] result = new String[2];
        try {
            Connection conn = DriverManager.getConnection(url, username, password);

            // Retrieve data from usuario table
            //String usuarioSelect = "SELECT ip, puerto FROM usuario WHERE nombre_archivo ="+filename;
            //String archivoSelect = "SELECT ip, puerto FROM archivo WHERE nombre_archivo = ?";
            String archivoSelect = "SELECT u.id_usuario, u.puerto FROM archivo a JOIN usuario u ON a.id_usuario = u.id_usuario WHERE a.nombre_archivo ="+filename;
            PreparedStatement archivoStatement = conn.prepareStatement(archivoSelect);
            archivoStatement.setString(1, filename);
            ResultSet archivoResult = archivoStatement.executeQuery();
            if (archivoResult.next()) {
                result[0] = archivoResult.getString("ip");
                result[1] = archivoResult.getInt("puerto") + "";
            } else {
                result[0] = "Archivo no existe";
                result[1] = "0";
            }

            archivoResult.close();
                archivoStatement.close();
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }   
        return result;
    }
}