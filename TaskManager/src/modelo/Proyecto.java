package modelo;

public class Proyecto {
    private int id;
    private String nombre;
    private String descripcion;
    private String vencimiento;

    public Proyecto(int id, String nombre, String descripcion, String vencimiento) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.vencimiento = vencimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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


    
}