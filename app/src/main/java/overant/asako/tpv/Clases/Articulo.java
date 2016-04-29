package overant.asako.tpv.Clases;

import java.io.Serializable;

import overant.asako.tpv.R;

public class Articulo implements Serializable {

    private int ID = 0;
    private int idEmpresa;
    private int idCategoria;
    private int idIva;
    private String nombre;
    private String nombreCat;
    private String nombreIva;
    private String EAN;
    private String foto = null;
    private double precio;
    private double descuento;
    private Boolean baja = false;
    private int stockTotal = 0;

    public Articulo() {
    }

    public Articulo(int ID, int idEmpresa, int idCategoria, int idIva, String nombre, String nombreCat, String nombreIva, String EAN, String foto, double precio, double descuento) {
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public Boolean isBaja() {
        return baja;
    }

    public void setBaja(Boolean baja) {
        this.baja = baja;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

}
