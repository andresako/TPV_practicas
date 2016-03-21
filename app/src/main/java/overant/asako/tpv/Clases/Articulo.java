package overant.asako.tpv.Clases;

import java.io.Serializable;

public class Articulo  implements Serializable {

    private int ID = 0;
    private int idEmpresa;
    private int idCategoria;
    private int idIva;
    private String nombre;
    private String nombreCat;
    private String nombreIva;
    private String EAN;
    private String foto;
    private Long precio;
    private Long descuento;
    private Boolean baja = false;

    public Articulo() {
    }

    public Articulo(int ID, int idEmpresa, int idCategoria, int idIva, String nombre, String nombreCat, String nombreIva, String EAN, String foto, Long precio, Long descuento) {
        this.ID = ID;
        this.idEmpresa = idEmpresa;
        this.idCategoria = idCategoria;
        this.idIva = idIva;
        this.nombre = nombre;
        this.nombreCat = nombreCat;
        this.nombreIva = nombreIva;
        this.EAN = EAN;
        this.foto = foto;
        this.precio = precio;
        this.descuento = descuento;
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

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdIva() {
        return idIva;
    }

    public void setIdIva(int idIva) {
        this.idIva = idIva;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCat() {
        return nombreCat;
    }

    public void setNombreCat(String nombreCat) {
        this.nombreCat = nombreCat;
    }

    public String getNombreIva() {
        return nombreIva;
    }

    public void setNombreIva(String nombreIva) {
        this.nombreIva = nombreIva;
    }

    public String getEAN() {
        return EAN;
    }

    public void setEAN(String EAN) {
        this.EAN = EAN;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Long getDescuento() {
        return descuento;
    }

    public void setDescuento(Long descuento) {
        this.descuento = descuento;
    }

    public Boolean isBaja() {
        return baja;
    }

    public void setBaja(Boolean baja) {
        this.baja = baja;
    }
}