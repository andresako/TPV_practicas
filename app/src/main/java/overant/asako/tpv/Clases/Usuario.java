package overant.asako.tpv.Clases;

import java.io.Serializable;

public class Usuario implements Serializable{

    private int ID;
    private int idPuntoVenta;
    private String nombre;
    private String apellidos;
    private String DNI;
    private String direccion;
    private String localidad;
    private String provincia;
    private String cPostal;
    private String telefono;
    private String mail;
    private String contrasena;
    private String admin;
    private boolean baja = false;

    public Usuario (){
        this.ID = 0;
    }
    public Usuario(int ID, int idPuntoVenta, String nombre, String apellidos, String DNI, String direccion, String localidad, String provincia, String cPostal, String telefono, String mail, String contrasena, String admin) {
        this.ID = ID;
        this.idPuntoVenta = idPuntoVenta;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.DNI = DNI;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.cPostal = cPostal;
        this.telefono = telefono;
        this.mail = mail;
        this.contrasena = contrasena;
        this.admin = admin;
    }

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIdPuntoVenta() {
        return idPuntoVenta;
    }

    public void setIdPuntoVenta(int idPuntoVenta) {
        this.idPuntoVenta = idPuntoVenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }
}
