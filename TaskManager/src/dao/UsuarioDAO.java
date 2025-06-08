package dao;

import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO {
    private Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    public void guardarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario (id, nombre, tipo) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, usuario.getId());
        stmt.setString(2, usuario.getNombre());
        stmt.setString(3, usuario.getTipo());
        stmt.executeUpdate();
    }

    public ArrayList<Usuario> listarUsuarios() throws SQLException {
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Usuario u = new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("tipo")
            );
            lista.add(u);
        }
        return lista;
    }
} 
