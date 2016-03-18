package overant.asako.tpv.Clases;

import java.io.Serializable;

public class Cliente implements Serializable {

    private int ID;
    private String nombre, apellido, dni, direccion, localidad, provincia, cPostal, telefono, mail, nomComercial, razSocial, cif;
    private boolean baja = false;

    public Cliente(int ID, String nombre, String apellido, String dni, String direccion, String localidad, String provincia, String cPostal, String telefono, String mail, String nomComercial, String razSocial, String cif) {
        this.ID = ID;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.cPostal = cPostal;
        this.telefono = telefono;
        this.mail = mail;
        this.nomComercial = nomComercial;
        this.razSocial = razSocial;
        this.cif = cif;

    }

    public Cliente() {
        this.ID = 0;
    }

    public int getId() {
        return this.ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public String getNomComercial() {
        return nomComercial;
    }

    public void setNomComercial(String nomComercial) {
        this.nomComercial = nomComercial;
    }

    public String getRazSocial() {
        return razSocial;
    }

    public void setRazSocial(String razSocial) {
        this.razSocial = razSocial;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }
}
