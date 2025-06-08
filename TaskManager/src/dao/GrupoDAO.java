package dao;

import modelo.Grupo;
import java.sql.*;
import java.util.ArrayList;

public class GrupoDAO {
    private Connection conn;

    public GrupoDAO(Connection conn) {
        this.conn = conn;
    }

    public void guardarGrupo(Grupo grupo) throws SQLException {
        String sql = "INSERT INTO Grupo (id, nombre) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, grupo.getId());
        stmt.setString(2, grupo.getNombre());
        stmt.executeUpdate();
    }

    public ArrayList<Grupo> listarGrupos() throws SQLException {
        ArrayList<Grupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Grupo";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Grupo g = new Grupo(
                rs.getInt("id"),
                rs.getString("nombre")
            );
            lista.add(g);
        }
        return lista;
    }
}
