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

import overant.asako.tpv.Clases.Almacen;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.JSONParser;

public class AdmListaAlmacenes extends Activity {

    private SharedPreferences sp;

    private static final String URL = "http://overant.es/almacenes.php";

    // Tags de mi JSON php Script;
    private static final String TAG_ALMACEN = "almacenes";
    private static final String TAG_ID = "id";
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_LOCALIDAD = "localidad";
    private static final String TAG_PROVINCIA = "provincia";
    private static final String TAG_C_POSTAL = "cpostal";
    private static final String TAG_BAJA = "baja";

    //Datos
    private JSONParser jsonParser = new JSONParser();
    public List<Almacen> listaAlmacenes;
    private ProgressDialog pDialog;

    private TextView tituloLista;
    private ListView lvAlmacenes;
    private Button bNuevoAlm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_lista);

        setUi();
    }

    private void setUi() {
        sp = PreferenceManager.getDefaultSharedPreferences(AdmListaAlmacenes.this);

        tituloLista = (TextView) findViewById(R.id.AdmListaTitulo);
        bNuevoAlm = (Button) findViewById(R.id.AdmListaBoton);
        lvAlmacenes = (ListView) findViewById(R.id.admListaView);

        tituloLista.setText("Selecciona Almacen");
        bNuevoAlm.setText("Nuevo almacen");

        lvAlmacenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AdmListaAlmacenes.this, AdmAlmacen.class);
                i.putExtra("almacen", listaAlmacenes.get(position));
                startActivity(i);
            }
        });
        bNuevoAlm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdmListaAlmacenes.this, AdmAlmacen.class);
                i.putExtra("almacen", new Almacen());
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new rellenarAlmacenes().execute();
    }

    public class rellenarAlmacenes extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdmListaAlmacenes.this);
            pDialog.setMessage("Cargando datos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            actualizarAlmacenes();
            return Boolean.parseBoolean(null);
        }

        protected void onPostExecute(Boolean resultado) {
            // dismiss the dialog once product deleted
            super.onPostExecute(resultado);
            mostrarAlmacenes();
            pDialog.dismiss();
        }
    }

    private void mostrarAlmacenes() {
        List<String> listaCt = new ArrayList<>();
        for (int x = 0; x < listaAlmacenes.size(); x++) {
            listaCt.add(listaAlmacenes.get(x).getID() + ". " + listaAlmacenes.get(x).getNombre());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCt);
        lvAlmacenes.setAdapter(arrayAdapter);
        lvAlmacenes.setDividerHeight(10);

    }

    private void actualizarAlmacenes() {
        //Clientes directamente de la BD
        listaAlmacenes = new ArrayList<>();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));
        params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));

        JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);

        try {

            JSONArray mAlmacen = joDatos.getJSONArray(TAG_ALMACEN);

            for (int i = 0; i < mAlmacen.length(); i++) {
                JSONObject c = mAlmacen.getJSONObject(i);

                Almacen ctAlm = new Almacen(
                        c.getInt(TAG_ID),
                        c.getInt(TAG_ID_EMPRESA),
                        c.getString(TAG_NOMBRE),
                        c.getString(TAG_DIRECCION),
                        c.getString(TAG_LOCALIDAD),
                        c.getString(TAG_PROVINCIA),
                        c.getString(TAG_C_POSTAL));

                if (c.getString(TAG_BAJA).equals("S")) ctAlm.setBaja(true);
                listaAlmacenes.add(ctAlm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
