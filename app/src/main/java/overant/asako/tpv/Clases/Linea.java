package overant.asako.tpv.Clases;

public class Linea {

    public int cantidad;
    public double precioTotal;
    public Articulo art;

    public Linea(int cantidad, Articulo art){
        this.cantidad = cantidad;
        this.art = art;
        precioTotal = art.getPrecio()*cantidad;
    }

}
