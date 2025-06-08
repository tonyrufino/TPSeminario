package modelo;

import java.util.ArrayList;

public class Grupo {
    private int id;
    private String nombre;
    private ArrayList<Usuario> miembros;

    public Grupo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.miembros = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public ArrayList<Usuario> getMiembros() { return miembros; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void agregarMiembro(Usuario usuario) {
        miembros.add(usuario);
    }

    public void eliminarMiembro(Usuario usuario) {
        miembros.remove(usuario);
    }
} 
