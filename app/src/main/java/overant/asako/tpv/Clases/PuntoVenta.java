package overant.asako.tpv.Clases;


import java.io.Serializable;

public class PuntoVenta implements Serializable {

    private int ID;
    private int idEmpresa;
    private String nombre;
    private String direccion;
    private String localidad;
    private String provincia;
    private String codPostal;
    private boolean baja = false;

    public PuntoVenta(){
        this.ID = 0;
    }
    public PuntoVenta(int ID, int idEmpresa, String nombre, String direccion, String localidad, String provincia, String codPostal) {
        this.ID = ID;
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.codPostal = codPostal;
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

    public String getCodPostal() {
        return codPostal;
    }
    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public boolean isBaja() {
        return baja;
    }
    public void setBaja(boolean baja) {
        this.baja = baja;
    }
}
