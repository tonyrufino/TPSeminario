package dao;

import modelo.Tarea;
import java.sql.*;
import java.util.ArrayList;

public class TareaDAO {
    private Connection conn;

    public TareaDAO(Connection conn) {
        this.conn = conn;
    }

    public void guardarTarea(Tarea tarea) throws SQLException {
        String sql = "INSERT INTO Tarea (id, descripcion, vencimiento, prioridad, estado, idProyecto) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, tarea.getId());
        stmt.setString(2, tarea.getDescripcion());
        stmt.setString(3, tarea.getVencimiento());
        stmt.setString(4, tarea.getPrioridad());
        stmt.setString(5, tarea.getEstado());
        stmt.setInt(6, tarea.getIdProyecto());
        stmt.executeUpdate();
    }

    public ArrayList<Tarea> listarTareas() throws SQLException {
        ArrayList<Tarea> lista = new ArrayList<>();
        String sql = "SELECT * FROM Tarea";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Tarea t = new Tarea(
                rs.getInt("id"),
                rs.getString("descripcion"),
                rs.getString("vencimiento"),
                rs.getString("prioridad"),
                rs.getString("estado"),
                rs.getInt("idProyecto")
            );
            lista.add(t);
        }
        return lista;
    }
}
