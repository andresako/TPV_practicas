package overant.asako.tpv.Clases;

import java.util.ArrayList;
import java.util.Date;

public class Carrito {

    private int ID;
    private int ID_empresa;
    private int ID_usuario;
    private int numero;
    private Date fecha;
    private double total;
    private ArrayList<Articulo> listaItems;

    public Carrito(int empresa, int user){
        this.ID_empresa = empresa;
        this.ID_usuario = user;
        this.fecha = new Date();
    }


    //TODO por aqui me he quedado
}

