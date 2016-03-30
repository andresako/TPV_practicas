package overant.asako.tpv.Clases;


import java.io.Serializable;

public class StockPorArticulo  implements Serializable{

    private int id;
    private String nomAlmacen;
    private int idAlmacen;
    private int cantidad;
    private int cantidadMinima;
    private Articulo articulo;

    public StockPorArticulo(){
    }
    public StockPorArticulo(int id, int idAlmacen, String nomAlmacen, int cantidad, int cantidadMinima){
        this.id = id;
        this.nomAlmacen = nomAlmacen;
        this.idAlmacen = idAlmacen;
        this.cantidad = cantidad;
        this.cantidadMinima = cantidadMinima;

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }
    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getNomAlmacen() {
        return nomAlmacen;
    }
    public void setNomAlmacen(String nomAlmacen) {
        this.nomAlmacen = nomAlmacen;
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }
    public void setCantidadMinima(int cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public Articulo getArticulo() {
        return articulo;
    }
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}
