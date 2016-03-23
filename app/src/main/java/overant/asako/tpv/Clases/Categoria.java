package overant.asako.tpv.Clases;

import java.io.Serializable;

public class Categoria implements Serializable{

    private int ID = 0;
    private int idEmpresa;
    private String nombre;
    private String foto;
    private boolean baja = false;

    public Categoria() {
    }

    public Categoria(int ID, int idEmpresa, String nombre, String foto) {
        this.ID = ID;
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.foto = foto;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
