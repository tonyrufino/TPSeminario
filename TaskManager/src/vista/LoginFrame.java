package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.ConexionDB;
import modelo.Usuario;
import modelo.Tarea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import javax.swing.table.DefaultTableModel;

public class LoginFrame extends JFrame {
    private JButton btnUsuario;
    private JButton btnAdmin;
    private JComboBox<String> cbUsuarios;
    private JLabel lblSeleccion;

    public LoginFrame() {
        setTitle("TaskManager - Ingreso");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel(new GridLayout(1, 2));
        btnUsuario = new JButton("Entrar como Usuario");
        btnAdmin = new JButton("Entrar como Administrador");
        panelBotones.add(btnUsuario);
        panelBotones.add(btnAdmin);

        lblSeleccion = new JLabel("Seleccione un usuario", SwingConstants.CENTER);
        cbUsuarios = new JComboBox<>();
        cbUsuarios.setVisible(false);
        lblSeleccion.setVisible(false);

        add(panelBotones, BorderLayout.NORTH);
        add(lblSeleccion, BorderLayout.CENTER);
        add(cbUsuarios, BorderLayout.SOUTH);

        btnUsuario.addActionListener(e -> cargarUsuarios("Usuario"));
        btnAdmin.addActionListener(e -> solicitarContrasenaAdmin());

        cbUsuarios.addActionListener(e -> {
            if (cbUsuarios.getSelectedItem() != null && cbUsuarios.isVisible()) {
                int idUsuario = Integer.parseInt(cbUsuarios.getSelectedItem().toString().split(" - ")[0]);
                new VistaTareasUsuario(idUsuario).setVisible(true);
                dispose();
            }
        });
    }

    private void solicitarContrasenaAdmin() {
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(this, passwordField, "Ingrese contraseña de administrador ((admin))", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            if (password.equals("admin")) {
                new MenuPrincipal().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarUsuarios(String tipo) {
        try (Connection conn = ConexionDB.conectar()) {
            cbUsuarios.removeAllItems();
            PreparedStatement ps = conn.prepareStatement("SELECT id, nombre FROM Usuario WHERE tipo = ?");
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cbUsuarios.addItem(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
            lblSeleccion.setText("Seleccione un " + tipo.toLowerCase() + ":");
            lblSeleccion.setVisible(true);
            cbUsuarios.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

    class VistaTareasUsuario extends JFrame {
    private JTable tablaTareas;
    private JButton btnSalir;
    private DefaultTableModel modelo;
    private int idUsuario;

    public VistaTareasUsuario(int idUsuario) {
        this.idUsuario = idUsuario;

        setTitle("Tareas asignadas");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columnas = {"ID", "Descripción", "Vencimiento", "Prioridad", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaTareas = new JTable(modelo);
        tablaTareas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaTareas);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout());
        JButton btnCompletar = new JButton("Marcar como Completada");
        btnCompletar.addActionListener(e -> marcarSeleccionadaComoCompletada());
        panelInferior.add(btnCompletar);

        btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> System.exit(0));
        panelInferior.add(btnSalir);

        add(panelInferior, BorderLayout.SOUTH);

        cargarTareas();
    }

    private void cargarTareas() {
        modelo.setRowCount(0);
        List<Object[]> tareas = new ArrayList<>();

        try (Connection conn = ConexionDB.conectar()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT T.id, T.descripcion, T.vencimiento, T.prioridad, T.estado " +
                "FROM Tarea T INNER JOIN Usuario_Tarea UT ON T.id = UT.idTarea " +
                "WHERE UT.idUsuario = ? AND T.estado = 'Pendiente'");
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tareas.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("descripcion"),
                    rs.getString("vencimiento"),
                    rs.getString("prioridad"),
                    rs.getString("estado")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener tareas: " + e.getMessage());
        }

        tareas.sort(Comparator.comparing((Object[] row) -> row[2].toString()) // por fecha
            .thenComparing(row -> prioridadValor(row[3].toString()))); // por prioridad

        for (Object[] fila : tareas) {
            modelo.addRow(fila);
        }
    }

    private int prioridadValor(String prioridad) {
        switch (prioridad.toLowerCase()) {
            case "alta": return 1;
            case "media": return 2;
            case "baja": return 3;
            default: return 4;
        }
    }

    private void marcarSeleccionadaComoCompletada() {
        int fila = tablaTareas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una tarea para marcar como completada.");
            return;
        }
        int idTarea = (int) modelo.getValueAt(fila, 0);

        try (Connection conn = ConexionDB.conectar()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE Tarea SET estado = 'Completada' WHERE id = ?");
            ps.setInt(1, idTarea);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Tarea marcada como completada.");
            cargarTareas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar tarea: " + e.getMessage());
        }
    }
}


