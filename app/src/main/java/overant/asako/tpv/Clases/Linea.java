package overant.asako.tpv.Clases;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Linea implements Serializable{

    private int ID;
    private int cantidad;
    private double precioTotal;
    private Articulo art;

    public Linea(int ID, int cantidad, Articulo art){
        this.ID = ID;
        this.cantidad = cantidad;
        this.art = art;
        this.precioTotal = art.getPrecio()*cantidad;
    }

    public void setPrecio(double precioUnitario){
        this.precioTotal = precioUnitario * this.cantidad;
    }
    public int getID() {
        return ID;
    }
    public int getCantidad() {
        return cantidad;
    }
    public double getPrecioTotal() {

        return precioTotal = Math.round(precioTotal*1e2)/1e2;

    }
    public Articulo getArt() {
        return art;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.precioTotal = art.getPrecio()*cantidad;
    }
}
