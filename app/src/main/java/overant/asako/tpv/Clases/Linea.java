package overant.asako.tpv.Clases;

public class Linea {

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

    public int getID() {
        return ID;
    }
    public int getCantidad() {
        return cantidad;
    }
    public double getPrecioTotal() {
        return precioTotal;
    }
    public Articulo getArt() {
        return art;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.precioTotal = art.getPrecio()*cantidad;
    }
}
