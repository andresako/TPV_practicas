package overant.asako.tpv.Clases;

import java.io.Serializable;

public class Proveedor  implements Serializable{

    private int id = 0;
    private int idEmpresa;
    private String nombre, cif, direccion, localidad, provincia, codPostal, mail;
    private boolean baja = false;

    public Proveedor() {
    }

    public Proveedor(int id, int idEmpresa, String nombre, String cif, String direccion, String localidad, String provincia, String codPostal, String mail) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.cif = cif;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.codPostal = codPostal;
        this.mail = mail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
