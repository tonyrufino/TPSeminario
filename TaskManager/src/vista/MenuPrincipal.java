package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.UsuarioDAO;
import dao.ProyectoDAO;
import dao.TareaDAO;
import modelo.Usuario;
import modelo.Proyecto;
import modelo.Tarea;
import modelo.Grupo;
import dao.ConexionDB;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

class MenuPrincipal extends JFrame {
    private JButton btnTareas;
    private JButton btnProyectos;
    private JButton btnGrupos;
    private JButton btnUsuarios;

    public MenuPrincipal() {
        setTitle("Panel de Administración - TaskManager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2, 10, 10));

        btnTareas = new JButton("Gestión Tareas");
        btnProyectos = new JButton("Gestión Proyectos");
        btnGrupos = new JButton("Gestión Grupos");
        btnUsuarios = new JButton("Gestión Usuarios");

        add(btnTareas);
        add(btnProyectos);
        add(btnGrupos);
        add(btnUsuarios);

        btnTareas.addActionListener(e -> new FormGestionTareas().setVisible(true));
        btnProyectos.addActionListener(e -> new FormGestionProyectos().setVisible(true));
        btnGrupos.addActionListener(e -> new FormGestionGrupos().setVisible(true));
        btnUsuarios.addActionListener(e -> new FormGestionUsuarios().setVisible(true));
    }
}

class FormGestionTareas extends JFrame {
    public FormGestionTareas() {
        setTitle("Gestión de Tareas");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JButton btnCrear = new JButton("Crear Tarea");
        btnCrear.addActionListener(e -> new FormCrearTarea().setVisible(true));
        add(btnCrear);
    }
}


class FormCrearTarea extends JFrame {
    private JComboBox<String> cbProyectos, cbGrupos, cbPrioridad;
    private JTextField txtDescripcion, txtVencimiento;
    private JButton btnGuardar;

    public FormCrearTarea() {
        setTitle("Nueva Tarea");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2));

        cbProyectos = new JComboBox<>();
        cbGrupos = new JComboBox<>();
        cbPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        txtDescripcion = new JTextField();
        txtVencimiento = new JTextField();
        btnGuardar = new JButton("Guardar");

        add(new JLabel("Proyecto:"));
        add(cbProyectos);
        add(new JLabel("Descripción:"));
        add(txtDescripcion);
        add(new JLabel("Vencimiento (YYYY-MM-DD):"));
        add(txtVencimiento);
        add(new JLabel("Prioridad:"));
        add(cbPrioridad);
        add(new JLabel("Asignar a Grupo:"));
        add(cbGrupos);
        add(new JLabel(""));
        add(btnGuardar);

        cargarDatos();
        btnGuardar.addActionListener(e -> guardarTarea());
    }

    private void cargarDatos() {
        try (Connection conn = ConexionDB.conectar()) {
            Statement st1 = conn.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT id, nombre FROM Proyecto");
            while (rs1.next()) {
                cbProyectos.addItem(rs1.getInt("id") + " - " + rs1.getString("nombre"));
            }
            Statement st2 = conn.createStatement();
            ResultSet rs2 = st2.executeQuery("SELECT id, nombre FROM Grupo");
            while (rs2.next()) {
                cbGrupos.addItem(rs2.getInt("id") + " - " + rs2.getString("nombre"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }

    private void guardarTarea() {
        try (Connection conn = ConexionDB.conectar()) {
            int idProyecto = Integer.parseInt(cbProyectos.getSelectedItem().toString().split(" - ")[0]);
            int idGrupo = Integer.parseInt(cbGrupos.getSelectedItem().toString().split(" - ")[0]);
            String descripcion = txtDescripcion.getText();
            String vencimiento = txtVencimiento.getText();
            String prioridad = cbPrioridad.getSelectedItem().toString();

            PreparedStatement ps = conn.prepareStatement("INSERT INTO Tarea (descripcion, vencimiento, prioridad, estado, idProyecto) VALUES (?, ?, ?, 'Pendiente', ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, descripcion);
            ps.setString(2, vencimiento);
            ps.setString(3, prioridad);
            ps.setInt(4, idProyecto);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int idTarea = 0;
            if (rs.next()) idTarea = rs.getInt(1);

            PreparedStatement psUG = conn.prepareStatement("SELECT idUsuario FROM Usuario_Grupo WHERE idGrupo = ?");
            psUG.setInt(1, idGrupo);
            ResultSet rsUG = psUG.executeQuery();
            while (rsUG.next()) {
                int idUsuario = rsUG.getInt("idUsuario");
                PreparedStatement psUT = conn.prepareStatement("INSERT INTO Usuario_Tarea (idUsuario, idTarea) VALUES (?, ?)");
                psUT.setInt(1, idUsuario);
                psUT.setInt(2, idTarea);
                psUT.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Tarea creada y asignada exitosamente.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar tarea: " + e.getMessage());
        }
    }
}

class FormGestionProyectos extends JFrame {
    public FormGestionProyectos() {
        setTitle("Gestión de Proyectos");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JButton btnCrear = new JButton("Crear Proyecto");
        JButton btnVer = new JButton("Ver Proyectos");

        btnCrear.addActionListener(e -> new FormCrearProyecto().setVisible(true));
        btnVer.addActionListener(e -> new FormVerProyectos().setVisible(true));

        add(btnCrear);
        add(btnVer);
    }
}

class FormCrearProyecto extends JFrame {
    private JTextField txtNombre, txtDescripcion, txtVencimiento;
    private JButton btnGuardar;

    public FormCrearProyecto() {
        setTitle("Nuevo Proyecto");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2));

        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtVencimiento = new JTextField();
        btnGuardar = new JButton("Guardar");

        add(new JLabel("Nombre:"));
        add(txtNombre);
        add(new JLabel("Descripción:"));
        add(txtDescripcion);
        add(new JLabel("Vencimiento (YYYY-MM-DD):"));
        add(txtVencimiento);
        add(new JLabel(""));
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarProyecto());
    }

    private void guardarProyecto() {
        try (Connection conn = ConexionDB.conectar()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Proyecto (nombre, descripcion, vencimiento) VALUES (?, ?, ?)");
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtDescripcion.getText());
            ps.setString(3, txtVencimiento.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Proyecto creado exitosamente.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar proyecto: " + e.getMessage());
        }
    }
}

class FormVerProyectos extends JFrame {
    public FormVerProyectos() {
        setTitle("Proyectos Existentes");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Nombre", "Descripción", "Vencimiento", "% Avance"}, 0);
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        try (Connection conn = ConexionDB.conectar()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, nombre, descripcion, vencimiento FROM Proyecto");
            while (rs.next()) {
                int idProyecto = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                String vencimiento = rs.getString("vencimiento");

                PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Tarea WHERE idProyecto = ?");
                ps.setInt(1, idProyecto);
                ResultSet rsT = ps.executeQuery();
                int total = rsT.next() ? rsT.getInt(1) : 0;

                ps = conn.prepareStatement("SELECT COUNT(*) FROM Tarea WHERE idProyecto = ? AND estado = 'Completada'");
                ps.setInt(1, idProyecto);
                rsT = ps.executeQuery();
                int completadas = rsT.next() ? rsT.getInt(1) : 0;

                int avance = (total == 0) ? 0 : (int) ((completadas * 100.0f) / total);

                modelo.addRow(new Object[]{nombre, descripcion, vencimiento, avance + "%"});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proyectos: " + e.getMessage());
        }
    }
}

class FormGestionUsuarios extends JFrame {
    public FormGestionUsuarios() {
        setTitle("Gestión de Usuarios");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JButton btnCrear = new JButton("Crear Usuario");
        btnCrear.addActionListener(e -> new FormCrearUsuario().setVisible(true));
        add(btnCrear);

        JButton btnEliminar = new JButton("Eliminar Usuario");
        btnEliminar.addActionListener(e -> new FormEliminarUsuario().setVisible(true));
        add(btnEliminar);
    }
}

class FormCrearUsuario extends JFrame {
    private JTextField txtNombre;
    private JComboBox<String> cbGrupos;
    private JButton btnGuardar;

    public FormCrearUsuario() {
        setTitle("Nuevo Usuario");
        setSize(350, 180);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        txtNombre = new JTextField();
        cbGrupos = new JComboBox<>();
        btnGuardar = new JButton("Guardar");

        add(new JLabel("Nombre:"));
        add(txtNombre);
        add(new JLabel("Grupo:"));
        add(cbGrupos);
        add(new JLabel(""));
        add(btnGuardar);

        cargarGrupos();
        btnGuardar.addActionListener(e -> guardarUsuario());
    }

    private void cargarGrupos() {
        try (Connection conn = ConexionDB.conectar()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, nombre FROM Grupo");
            while (rs.next()) {
                cbGrupos.addItem(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar grupos: " + e.getMessage());
        }
    }

    private void guardarUsuario() {
        try (Connection conn = ConexionDB.conectar()) {
            String nombre = txtNombre.getText();
            int idGrupo = Integer.parseInt(cbGrupos.getSelectedItem().toString().split(" - ")[0]);

            PreparedStatement ps = conn.prepareStatement("INSERT INTO Usuario (nombre, tipo) VALUES (?, 'Usuario')", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombre);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int idUsuario = rs.next() ? rs.getInt(1) : 0;

            PreparedStatement psUG = conn.prepareStatement("INSERT INTO Usuario_Grupo (idUsuario, idGrupo) VALUES (?, ?)");
            psUG.setInt(1, idUsuario);
            psUG.setInt(2, idGrupo);
            psUG.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuario creado y asignado a grupo exitosamente.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar usuario: " + e.getMessage());
        }
    }
}

class FormEliminarUsuario extends JFrame {
    private JComboBox<String> cbUsuarios;
    private JButton btnEliminar;

    public FormEliminarUsuario() {
        setTitle("Eliminar Usuario");
        setSize(350, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2));

        cbUsuarios = new JComboBox<>();
        btnEliminar = new JButton("Eliminar");

        add(new JLabel("Seleccione Usuario:"));
        add(cbUsuarios);
        add(new JLabel(""));
        add(btnEliminar);

        cargarUsuarios();
        btnEliminar.addActionListener(e -> eliminarUsuario());
    }

    private void cargarUsuarios() {
        try (Connection conn = ConexionDB.conectar()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, nombre FROM Usuario WHERE tipo = 'Usuario'");
            while (rs.next()) {
                cbUsuarios.addItem(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        try (Connection conn = ConexionDB.conectar()) {
            String selected = (String) cbUsuarios.getSelectedItem();
            if (selected == null) return;
            int idUsuario = Integer.parseInt(selected.split(" - ")[0]);

            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM Usuario_Tarea WHERE idUsuario = ?");
            ps1.setInt(1, idUsuario);
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM Usuario_Grupo WHERE idUsuario = ?");
            ps2.setInt(1, idUsuario);
            ps2.executeUpdate();

            PreparedStatement ps3 = conn.prepareStatement("DELETE FROM Usuario WHERE id = ?");
            ps3.setInt(1, idUsuario);
            ps3.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + e.getMessage());
        }
    }
}

class FormGestionGrupos extends JFrame {
    public FormGestionGrupos() {
        setTitle("Gestión de Grupos");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JButton btnCrear = new JButton("Crear Grupo");
        JButton btnVer = new JButton("Ver Grupos");

        btnCrear.addActionListener(e -> new FormCrearGrupo().setVisible(true));
        btnVer.addActionListener(e -> new FormVerGrupos().setVisible(true));

        add(btnCrear);
        add(btnVer);
    }
}

class FormCrearGrupo extends JFrame {
    private JTextField txtNombre;
    private JButton btnGuardar;

    public FormCrearGrupo() {
        setTitle("Nuevo Grupo");
        setSize(300, 120);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2));

        txtNombre = new JTextField();
        btnGuardar = new JButton("Guardar");

        add(new JLabel("Nombre del grupo:"));
        add(txtNombre);
        add(new JLabel(""));
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarGrupo());
    }

    private void guardarGrupo() {
        try (Connection conn = ConexionDB.conectar()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Grupo (nombre) VALUES (?)");
            ps.setString(1, txtNombre.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Grupo creado exitosamente.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar grupo: " + e.getMessage());
        }
    }
}

class FormVerGrupos extends JFrame {
    public FormVerGrupos() {
        setTitle("Listado de Grupos");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Acción"}, 0);
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        try (Connection conn = ConexionDB.conectar()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, nombre FROM Grupo");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                modelo.addRow(new Object[]{id, nombre, "Ver miembros"});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar grupos: " + e.getMessage());
        }

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabla.rowAtPoint(e.getPoint());
                int col = tabla.columnAtPoint(e.getPoint());
                if (col == 2) {
                    int idGrupo = (int) tabla.getValueAt(row, 0);
                    new FormVerMiembrosGrupo(idGrupo).setVisible(true);
                }
            }
        });
    }
}

class FormVerMiembrosGrupo extends JFrame {
    public FormVerMiembrosGrupo(int idGrupo) {
        setTitle("Miembros del Grupo " + idGrupo);
        setSize(300, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DefaultListModel<String> modelo = new DefaultListModel<>();
        JList<String> lista = new JList<>(modelo);
        add(new JScrollPane(lista), BorderLayout.CENTER);

        try (Connection conn = ConexionDB.conectar()) {
            PreparedStatement ps = conn.prepareStatement("SELECT U.nombre FROM Usuario U INNER JOIN Usuario_Grupo UG ON U.id = UG.idUsuario WHERE UG.idGrupo = ?");
            ps.setInt(1, idGrupo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addElement(rs.getString("nombre"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar miembros: " + e.getMessage());
        }
    }
}
