package overant.asako.tpv.Utils;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import overant.asako.tpv.Clases.Articulo;

public class Datos {

    private static Datos mDatos;
    private static int empresaId;
    public boolean haTerminado = false;
    private ArrayList<Articulo> listaArticulos;
    private HashMap<Integer, String> listaIdCat;
    private HashMap<Integer, Articulo> listaArt;
    private int variosID=0;

    public Datos() {new rellenarDatos().execute();}
    public synchronized static Datos getInstance(int empresa) {
        if (mDatos == null) {
            empresaId = empresa;
            mDatos = new Datos();
        }
        return mDatos;
    }

    public Articulo getArticuloId(int id) {return listaArt.get(id);}
    public ArrayList<Articulo> getListaArticulosPorCat(int key) {
        ArrayList<Articulo> fin = new ArrayList<>();

        for (int i = 0; i < listaArticulos.size(); i++) {
            if (listaArticulos.get(i).getIdCategoria() == key) {
                fin.add(listaArticulos.get(i));
            }
        }
        return fin;
    }
    public ArrayList<Integer> getListaIdCat() {return new ArrayList<>(listaIdCat.keySet());}
    public int getVariosID(){return this.variosID;}

    public class rellenarDatos extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... args) {
            listaArticulos = new ArrayList<>();
            listaIdCat = new HashMap<>();
            listaArt = new HashMap<>();

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "10"));
            params.add(new BasicNameValuePair("empresaId", empresaId + ""));

            JSONParser jsonParser = new JSONParser();
            JSONObject joDatos = jsonParser.peticionHttp("http://overant.es/TPV_java.php", "POST", params);
            try {

                JSONArray mArt = joDatos.getJSONArray("articulos");

                for (int i = 0; i < mArt.length(); i++) {

                    JSONObject c = mArt.getJSONObject(i);

                    if (!c.getString("nombre").equalsIgnoreCase("Varios")) {
                        Articulo ctArt = new Articulo(
                                c.getInt("id"),
                                c.getInt("id_empresa"),
                                c.getInt("id_familia"),
                                c.getInt("id_tipo_iva"),
                                c.getString("nombre"),
                                c.getString("nombre_familia"),
                                c.getString("nombre_iva"),
                                c.getString("ean"),
                                c.getString("foto"),
                                c.getDouble("precio"),
                                c.getDouble("dto"));

                        if (c.getString("baja").equals("S")) ctArt.setBaja(true);

                        listaIdCat.put(c.getInt("id_familia"), c.getString("nombre_familia"));
                        listaArticulos.add(ctArt);
                        listaArt.put(ctArt.getID(), ctArt);
                    } else {
                        Articulo ctArt = new Articulo(
                                c.getInt("id"),
                                c.getInt("id_empresa"),
                                0,
                                c.getInt("id_tipo_iva"),
                                c.getString("nombre"),
                                null,
                                c.getString("nombre_iva"),
                                null,
                                null,
                                0,
                                0);

                        Log.d("DATOS", "VariosId" +ctArt.getID());
                        variosID = ctArt.getID();
                        listaArticulos.add(ctArt);
                        listaArt.put(ctArt.getID(), ctArt);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Boolean.parseBoolean(null);
        }

        protected void onPostExecute(Boolean resultado) {
            haTerminado = true;
            Log.d("DATOS", "datos cargados");
            super.onPostExecute(resultado);
        }
    }
}
