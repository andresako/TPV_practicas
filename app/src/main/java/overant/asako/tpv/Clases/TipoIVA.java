package overant.asako.tpv.Clases;

import java.io.Serializable;

public class TipoIVA  implements Serializable {

    private int ID;
    private String nombre;
    private int valor;

    public TipoIVA() {
    }
    public TipoIVA(int ID, String nombre, int valor) {
        this.ID = ID;
        this.nombre = nombre;
        this.valor = valor;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}

