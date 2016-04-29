package overant.asako.tpv.Clases;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Carrito {

    private int ID;
    private int ID_empresa;
    private int ID_usuario;
    private int numero;
    private Date fecha;

    private double total;
    private HashMap<String,Linea> listaLineas;

    public Carrito(int empresa, int user){
        this.ID_empresa = empresa;
        this.ID_usuario = user;
        this.fecha = new Date();
        this.listaLineas = new HashMap<>();
        this.total = 0;
    }

    public void addItem(int cantidad, Articulo art){
        Linea l;
        if (!listaLineas.containsKey(art.getNombre())) {    //Si el articulo no está en el carro
            l = new Linea(cantidad, art);                       //creo nueva linea en el carrito
            this.listaLineas.put(art.getNombre(), l);           //la key de esa linea, es el nombre del articulo
            this.total += l.precioTotal;                        //actualizo el total del carrito
        }else{                                              //Si el articulo ESTÁ en el carrito
            l = listaLineas.get(art.getNombre());               //localizo la linea
            l.cantidad += cantidad;                             //añado la nueva cantidad del articulo
            this.total -= l.precioTotal;                        //resto la cantidad correspondiente al total (antiguo)
            l.precioTotal = l.art.getPrecio() * l.cantidad;     //actualizo la cantidad total de la linea
            this.total += l.precioTotal;                        //sumo el total de la lina al del carrito

        }
    }

    public void delItem(String nombreArt){
        if (listaLineas.containsKey(nombreArt)){
            Linea l = listaLineas.get(nombreArt);
            this.total -= l.precioTotal;
            this.listaLineas.remove(nombreArt);
        }
    }

    public void newCarro(){
        this.listaLineas = new HashMap<>();
        this.total = 0;
    }

    public int getID_empresa() {
        return ID_empresa;
    }
    public int getID_usuario() {
        return ID_usuario;
    }
    public Date getFecha() {
        return fecha;
    }
    public double getTotal() {
        return total;
    }
    public HashMap<String, Linea> getHashLineas() {
        return listaLineas;
    }


}

