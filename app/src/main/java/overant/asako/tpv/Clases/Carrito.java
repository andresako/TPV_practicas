package overant.asako.tpv.Clases;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import overant.asako.tpv.TPV.ActividadPrincipal;
import overant.asako.tpv.Utils.JSONParser;

public class Carrito implements Parcelable{

    private String URL = "http://overant.es/TPV_java.php";
    private JSONParser jsonParser = new JSONParser();

    private int ID;
    private int ID_empresa;
    private int ID_usuario;
    private int numero;
    private String fecha;
    private double total;
    private boolean cerrado = false;

    private HashMap<String, Linea> listaLineas;
    private ActividadPrincipal AP;

    public Carrito(int empresa, int user) {
        this.ID_empresa = empresa;
        this.ID_usuario = user;

        newCarro();
    }

    public Carrito(int ID, int ID_empresa, int ID_usuario, int numero, String fecha, double total) {
        this.ID = ID;
        this.ID_empresa = ID_empresa;
        this.ID_usuario = ID_usuario;
        this.numero = numero;
        this.fecha = fecha;
        this.total = total;
        listaLineas = new HashMap<>();
    }

    protected Carrito(Parcel in) {
        URL = in.readString();
        ID = in.readInt();
        ID_empresa = in.readInt();
        ID_usuario = in.readInt();
        numero = in.readInt();
        fecha = in.readString();
        total = in.readDouble();
        cerrado = in.readByte() != 0;
    }

    public static final Creator<Carrito> CREATOR = new Creator<Carrito>() {
        @Override
        public Carrito createFromParcel(Parcel in) {
            return new Carrito(in);
        }

        @Override
        public Carrito[] newArray(int size) {
            return new Carrito[size];
        }
    };

    public void addItem(int cantidad, Articulo art) {
        Linea l;
        if (!listaLineas.containsKey(art.getNombre())
                || art.getNombre().equalsIgnoreCase("Varios")) {            //Si el articulo no está en el carro
            addLinea task = new addLinea();
            task.params(new Integer[]{cantidad, art.getID()}, art);
            task.execute();
        } else {                                                            //Si el articulo ESTÁ en el carrito
            l = listaLineas.get(art.getNombre());
            l.setCantidad(l.getCantidad() + cantidad);
            new modLinea().execute(new Integer[]{l.getID(), l.getCantidad()});
        }
    }
    public void modItem(Linea linea, int cantidad) {linea.setCantidad(cantidad);}
    public void delItem(String nombreArt) {
        if (listaLineas.containsKey(nombreArt)) {
            Linea l = listaLineas.get(nombreArt);
            this.total -= l.getPrecioTotal();
            this.listaLineas.remove(nombreArt);

            new delLinea().execute(l.getID());

        }
    }

    public void newCarro() {
        listaLineas = new HashMap<>();
        total = 0;
        new newTicket().execute();
    }
    public void cerrarCarro(){
        listaLineas = new HashMap<>();
        total = 0;
        new cerrarTicket().execute();
    }

    public int getID(){ return this.ID; }
    public int getNumero() {return this.numero;}
    public int getID_empresa() {return ID_empresa;}
    public int getID_usuario() {return ID_usuario;}
    public String getFecha() {return fecha;}
    public double getTotal() {return total;}
    public boolean isCerrado(){return this.cerrado;}
    public void setCerrado(boolean opc){this.cerrado = opc;}

    public void setTotal(double total) {this.total = total;}

    public HashMap<String, Linea> getHashLineas() {return listaLineas;}
    public void setListaLineas(HashMap<String, Linea> lista) {this.listaLineas = lista;}
    public void vaciarTicket() {
        listaLineas = new HashMap<>();
        total = 0;
        new resetTicket().execute();
    }

    public void setMain(ActividadPrincipal main) {this.AP = main;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(URL);
        dest.writeInt(ID);
        dest.writeInt(ID_empresa);
        dest.writeInt(ID_usuario);
        dest.writeInt(numero);
        dest.writeString(fecha);
        dest.writeDouble(total);
        dest.writeByte((byte) (cerrado ? 1 : 0));
    }

    private class newTicket extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... args) {

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "3"));
            params.add(new BasicNameValuePair("empresaId", ID_empresa + ""));
            params.add(new BasicNameValuePair("usuarioId", ID_usuario + ""));

            JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);

            Log.d("CARRITO", "Nuevo ticket " + joDatos.toString());
            try {
                int result = joDatos.getInt("Res");

                if (result == 1) {
                    ID = joDatos.getInt("Id");
                    fecha = joDatos.getString("Fecha");
                    numero = joDatos.getInt("Numero");
                } else {
                    return false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            if(msg)AP.refreshCarro();
            super.onPostExecute(msg);
        }
    }
    private class resetTicket extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... args) {

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "9"));
            params.add(new BasicNameValuePair("ticketId", ID + ""));

            JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);

            Log.d("CARRITO", "Reset ticket " + joDatos.toString());

            return true;
        }
    }
    private class cerrarTicket extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "7"));
            params.add(new BasicNameValuePair("ticketId", ID + ""));

            JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);

            Log.d("CARRITO", "Cerrar ticket "+ ID + joDatos.toString());

            return true;
        }
        @Override
        protected void onPostExecute(Boolean msg) {
            if (msg){
                newCarro();
            }

            super.onPostExecute(msg);
        }
    }
    private class addLinea extends AsyncTask<Void, Void, Boolean> {

        private int articuloId, cantidad;
        private Articulo art;

        public void params(Integer[] args, Articulo art) {
            this.art = art;
            this.cantidad = args[0];
            this.articuloId = args[1];
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "4"));
            params.add(new BasicNameValuePair("ticketId", ID + ""));
            params.add(new BasicNameValuePair("cantidad", cantidad + ""));
            params.add(new BasicNameValuePair("articuloId", articuloId + ""));

            if(art.getNombre().equalsIgnoreCase("Varios"))params.add(new BasicNameValuePair("precio", art.getPrecio() + ""));

            JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
            try {
                int result = joDatos.getInt("Res");
                if (result == 1) {
                    Linea ln = new Linea(joDatos.getInt("Id"), cantidad, art);
                    if(art.getNombre().equalsIgnoreCase("Varios")){
                        listaLineas.put(art.getNombre()+ln.getID(), ln);
                    }else {
                        listaLineas.put(art.getNombre(), ln);
                    }
                    total = joDatos.getDouble("Total");
                } else {
                    return false;
                }


            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            if (msg){
                AP.refreshCarro();
            }

            super.onPostExecute(msg);
        }

    }
    private class modLinea extends AsyncTask<Integer[], Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer[]... args) {
            Integer[] array = args[0];

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "5"));
            params.add(new BasicNameValuePair("lineaId", array[0] + ""));
            params.add(new BasicNameValuePair("cantidad", array[1] + ""));

            JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
            try {
                int result = joDatos.getInt("Res");
                if (result == 1) {
                    setTotal(joDatos.getDouble("Total"));
                } else {
                    return false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            if (msg){
                AP.refreshCarro();
            }

            super.onPostExecute(msg);
        }
    }
    private class delLinea extends AsyncTask<Integer,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Integer... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "6"));
            params.add(new BasicNameValuePair("lineaId", args[0] + ""));

            JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);

            Log.d("CARRITO", "Eliminada la linea "+ args[0] + joDatos.toString());

            return true;
        }
    }
}

