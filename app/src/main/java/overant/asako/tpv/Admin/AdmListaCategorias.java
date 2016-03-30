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

import overant.asako.tpv.Clases.Categoria;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.JSONParser;

public class AdmListaCategorias extends Activity {

    private SharedPreferences sp;

    private static final String URL = "http://overant.es/familias.php";

    // Tags de mi JSON php Script;
    private static final String TAG_FAMILIAS = "familias";
    private static final String TAG_ID = "id";
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_FOTO = "foto";
    private static final String TAG_BAJA = "baja";

    //Datos
    private JSONParser jsonParser = new JSONParser();
    public List<Categoria> listaCategorias;
    private ProgressDialog pDialog;

    private TextView tituloLista;
    private ListView lvCategorias;
    private Button bNuevaCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_lista);
        setUi();
    }

    private void setUi() {
        sp = PreferenceManager.getDefaultSharedPreferences(AdmListaCategorias.this);

        tituloLista = (TextView) findViewById(R.id.AdmListaTitulo);
        bNuevaCat = (Button) findViewById(R.id.AdmListaBoton);
        lvCategorias = (ListView) findViewById(R.id.admListaView);

        tituloLista.setText("Selecciona Categoria");
        bNuevaCat.setText("Nueva categoria");

        lvCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AdmListaCategorias.this, AdmCategoria.class);
                i.putExtra("categoria", listaCategorias.get(position));
                startActivity(i);
            }
        });
        bNuevaCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdmListaCategorias.this, AdmCategoria.class);
                i.putExtra("categoria", new Categoria());
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new rellenarCategorias().execute();
    }

    private void actualizarCategorias() {
        listaCategorias = new ArrayList<>();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));
        params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));

        JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
        try {

            JSONArray mCat = joDatos.getJSONArray(TAG_FAMILIAS);

            for (int i = 0; i < mCat.length(); i++) {
                JSONObject c = mCat.getJSONObject(i);

                Categoria ctCat = new Categoria(
                        c.getInt(TAG_ID),
                        c.getInt(TAG_ID_EMPRESA),
                        c.getString(TAG_NOMBRE),
                        c.getString(TAG_FOTO));

                if (c.getString(TAG_BAJA).equals("S")) ctCat.setBaja(true);

                listaCategorias.add(ctCat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mostrarCategorias() {
        List<String> listaCt = new ArrayList<>();
        for (int x = 0; x < listaCategorias.size(); x++) {
            listaCt.add(listaCategorias.get(x).getNombre());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCt);
        lvCategorias.setAdapter(arrayAdapter);
        lvCategorias.setDividerHeight(10);
    }

    public class rellenarCategorias extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdmListaCategorias.this);
            pDialog.setMessage("Cargando datos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            actualizarCategorias();
            return Boolean.parseBoolean(null);
        }

        protected void onPostExecute(Boolean resultado) {
            // dismiss the dialog once product deleted
            super.onPostExecute(resultado);
            mostrarCategorias();
            pDialog.dismiss();
        }
    }

}
