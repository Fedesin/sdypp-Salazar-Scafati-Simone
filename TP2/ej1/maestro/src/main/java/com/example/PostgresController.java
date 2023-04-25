package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
}