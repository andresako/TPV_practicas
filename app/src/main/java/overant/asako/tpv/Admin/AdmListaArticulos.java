package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.Articulo;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.JSONParser;

public class AdmListaArticulos extends Activity {

    private SharedPreferences sp;

    private static final String URL = "http://overant.es/articulos.php";

    // Tags de mi JSON php Script;
    private static final String TAG_ARTICULOS = "articulos";
    private static final String TAG_ID = "id";
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_ID_CATEGORIA = "id_familia";
    private static final String TAG_ID_IVA = "id_tipo_iva";
    private static final String TAG_NOMBRE_CATEGORIA = "nombre_familia";
    private static final String TAG_NOMBRE_IVA = "nombre_iva";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_EAN = "ean";
    private static final String TAG_FOTO = "foto";
    private static final String TAG_PRECIO = "precio";
    private static final String TAG_DESCUENTO = "dto";
    private static final String TAG_BAJA = "baja";

    //Datos
    private JSONParser jsonParser = new JSONParser();
    public List<Articulo> listaArticulos;
    private ProgressDialog pDialog;

    private TextView tituloLista;
    private ListView lvArticulos;
    private Button bNuevoArt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_lista);
        setUi();
    }

    private void setUi() {
        sp = PreferenceManager.getDefaultSharedPreferences(AdmListaArticulos.this);

        tituloLista = (TextView) findViewById(R.id.AdmListaTitulo);
        bNuevoArt = (Button) findViewById(R.id.AdmListaBoton);
        lvArticulos = (ListView) findViewById(R.id.admListaView);

        tituloLista.setText("Selecciona Articulo");
        bNuevoArt.setText("Nuevo Articulo");

        lvArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AdmListaArticulos.this, AdmArticulo.class);
                i.putExtra("articulo", listaArticulos.get(position));
                startActivity(i);
            }
        });
        bNuevoArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdmListaArticulos.this, AdmArticulo.class);
                i.putExtra("categoria", new Articulo());
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new rellenarArticulos().execute();
    }

    private void actualizarArticulos() {
        listaArticulos = new ArrayList<>();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));
        params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));

        JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
        System.out.println(joDatos);
        try {

            JSONArray mArt = joDatos.getJSONArray(TAG_ARTICULOS);

            for (int i = 0; i < mArt.length(); i++) {
                JSONObject c = mArt.getJSONObject(i);

                Articulo ctArt = new Articulo(
                        c.getInt(TAG_ID),
                        c.getInt(TAG_ID_EMPRESA),
                        c.getInt(TAG_ID_CATEGORIA),
                        c.getInt(TAG_ID_IVA),
                        c.getString(TAG_NOMBRE),
                        c.getString(TAG_NOMBRE_CATEGORIA),
                        c.getString(TAG_NOMBRE_IVA),
                        c.getString(TAG_EAN),
                        c.getString(TAG_FOTO),
                        c.getLong(TAG_PRECIO),
                        c.getLong(TAG_DESCUENTO));

                if (c.getString(TAG_BAJA).equals("S")) ctArt.setBaja(true);

                listaArticulos.add(ctArt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void mostrarArticulos() {
        List<String> listaCt = new ArrayList<>();
        for (int x = 0; x < listaArticulos.size(); x++) {
            listaCt.add(listaArticulos.get(x).getNombre());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCt);
        lvArticulos.setAdapter(arrayAdapter);
        lvArticulos.setDividerHeight(10);
    }

    public class rellenarArticulos extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdmListaArticulos.this);
            pDialog.setMessage("Cargando datos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            actualizarArticulos();
            return Boolean.parseBoolean(null);
        }

        protected void onPostExecute(Boolean resultado) {
            // dismiss the dialog once product deleted
            super.onPostExecute(resultado);
            mostrarArticulos();
            pDialog.dismiss();
        }
    }
}
