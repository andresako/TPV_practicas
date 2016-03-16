package overant.asako.tpv.Clases;

public class Cliente {

    private int ID;
    private String nombre, apellido, dni, direccion, localidad, provincia, cPostal, telefono, mail;
    private boolean baja = false;

    public Cliente(int ID, String nombre, String apellido, String dni, String direccion, String localidad, String provincia, String cPostal, String telefono, String mail) {
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
    }

    public Cliente(String nombre, String apellido, String dni, String direccion, String localidad, String provincia, String cPostal, String telefono, String mail) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.cPostal = cPostal;
        this.telefono = telefono;
        this.mail = mail;
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
}
