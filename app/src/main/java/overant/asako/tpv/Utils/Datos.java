package overant.asako.tpv.Utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import overant.asako.tpv.Clases.Articulo;

public class Datos extends Application {

    private static Context context;
    private static Datos mDatos = null;
    public boolean haTerminado = false;
    private ArrayList<Articulo> listaArticulos;
    private HashMap<Integer, String> listaIdCat;

    public Datos(Context context) {
        Datos.context = context;
        new rellenarDatos().execute();
    }

    public static Datos getInstance() {
        if (mDatos == null) {
            mDatos = new Datos(context);
        }
        return mDatos;
    }

    public ArrayList<Articulo> getListaArticulosPorCat(int key) {
        ArrayList<Articulo> fin = new ArrayList<>();

        for (int i = 0; i < listaArticulos.size(); i++) {
            if (listaArticulos.get(i).getIdCategoria() == key) {
                fin.add(listaArticulos.get(i));
            }
        }
        return fin;
    }

    public ArrayList<Integer> getListaIdCat() {
        return new ArrayList<>(listaIdCat.keySet());
    }

    public class rellenarDatos extends AsyncTask<Void, Void, Boolean> {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        @Override
        protected Boolean doInBackground(Void... args) {
            listaArticulos = new ArrayList<>();
            listaIdCat = new HashMap<>();

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app", "1"));
            params.add(new BasicNameValuePair("id_empresa", sp.getString("empresaId", "0")));

            JSONParser jsonParser = new JSONParser();
            JSONObject joDatos = jsonParser.peticionHttp("http://overant.es/articulos.php", "POST", params);
            try {

                JSONArray mArt = joDatos.getJSONArray("articulos");

                for (int i = 0; i < mArt.length(); i++) {
                    JSONObject c = mArt.getJSONObject(i);

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

                    if (!c.getString("stock").equals("null")) {
                        ctArt.setStockTotal(c.getInt("stock"));
                    } else {
                        ctArt.setStockTotal(0);
                    }

                    listaIdCat.put(c.getInt("id_familia"), c.getString("nombre_familia"));
                    listaArticulos.add(ctArt);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Boolean.parseBoolean(null);
        }

        protected void onPostExecute(Boolean resultado) {
            haTerminado = true;
            super.onPostExecute(resultado);
        }
    }
}
