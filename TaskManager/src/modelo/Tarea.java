package modelo;

public class Tarea {
    private int id;
    private String descripcion;
    private String vencimiento;
    private String prioridad;
    private String estado;
    private int idProyecto;

    public Tarea(int id, String descripcion, String vencimiento, String prioridad, String estado, int idProyecto) {
        this.id = id;
        this.descripcion = descripcion;
        this.vencimiento = vencimiento;
        this.prioridad = prioridad;
        this.estado = estado;
        this.idProyecto = idProyecto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }
}
