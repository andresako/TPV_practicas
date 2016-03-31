package overant.asako.tpv.Clases;

import java.io.Serializable;

public class Almacen implements Serializable{

    private int ID = 0;
    private int ID_Empresa;
    private String nombre;
    private String direccion;
    private String localidad;
    private String provincia;
    private String cPostal;
    private boolean baja = false;

    public Almacen(){

    }
    public Almacen(int ID, int ID_Empresa, String nombre, String direccion, String localidad, String provincia, String cPostal) {
        this.ID = ID;
        this.ID_Empresa = ID_Empresa;
        this.nombre = nombre;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.cPostal = cPostal;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID_Empresa() {
        return ID_Empresa;
    }

    public void setID_Empresa(int ID_Empresa) {
        this.ID_Empresa = ID_Empresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getcPostal() {
        return cPostal;
    }

    public void setcPostal(String cPostal) {
        this.cPostal = cPostal;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
