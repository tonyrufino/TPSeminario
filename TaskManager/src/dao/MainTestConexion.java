package dao;

import java.sql.Connection;

public class MainTestConexion {
    public static void main(String[] args) {
        try {
            Connection conn = ConexionDB.conectar();
            if (conn != null) {
                System.out.println("Conexión exitosa a la base de datos.");
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
}
