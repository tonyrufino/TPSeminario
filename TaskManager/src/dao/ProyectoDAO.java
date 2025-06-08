package dao;

import modelo.Proyecto;
import java.sql.*;
import java.util.ArrayList;

public class ProyectoDAO {
    private Connection conn;

    public ProyectoDAO(Connection conn) {
        this.conn = conn;
    }

    public void guardarProyecto(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO Proyecto (id, nombre, descripcion, vencimiento) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, proyecto.getId());
        stmt.setString(2, proyecto.getNombre());
        stmt.setString(3, proyecto.getDescripcion());
        stmt.setString(4, proyecto.getVencimiento());
        stmt.executeUpdate();
    }

    public ArrayList<Proyecto> listarProyectos() throws SQLException {
        ArrayList<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Proyecto";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Proyecto p = new Proyecto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getString("vencimiento")
            );
            lista.add(p);
        }
        return lista;
    }
}
