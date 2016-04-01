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

import overant.asako.tpv.Clases.Proveedor;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.JSONParser;

public class AdmListaProveedores extends Activity {

    private static final String URL = "http://overant.es/proveedores.php";

    // Tags de mi JSON php Script;
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_PROVEEDORES = "proveedores";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_CIF = "cif";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_LOCALIDAD = "localidad";
    private static final String TAG_PROVINCIA = "provincia";
    private static final String TAG_C_POSTAL = "codigo_postal";
    private static final String TAG_MAIL = "email";
    private static final String TAG_BAJA = "baja";

    private SharedPreferences sp;
    private ProgressDialog pDialog;

    private TextView tituloLista;
    private ListView lvProveedores;
    private Button bNuevoPro;

    private JSONParser jsonParser = new JSONParser();
    private List<Proveedor> listaProveedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_lista);
        setUi();
    }

    private void setUi() {
        sp = PreferenceManager.getDefaultSharedPreferences(AdmListaProveedores.this);

        tituloLista = (TextView) findViewById(R.id.AdmListaTitulo);
        bNuevoPro = (Button) findViewById(R.id.AdmListaBoton);
        lvProveedores = (ListView) findViewById(R.id.admListaView);

        tituloLista.setText("Selecciona Proveedor");
        bNuevoPro.setText("Nuevo proveedor");

        lvProveedores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AdmListaProveedores.this, AdmProveedor.class);
                i.putExtra("proveedor", listaProveedores.get(position));
                startActivity(i);
            }
        });
        bNuevoPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdmListaProveedores.this, AdmProveedor.class);
                i.putExtra("proveedor", new Proveedor());
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new rellenarProveedores().execute();
    }

    public class rellenarProveedores extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isFinishing()) {
                pDialog = new ProgressDialog(AdmListaProveedores.this);
                pDialog.setMessage("Cargando datos...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            actualizarProveedores();
            return Boolean.parseBoolean(null);
        }

        protected void onPostExecute(Boolean resultado) {
            // dismiss the dialog once product deleted
            super.onPostExecute(resultado);
            mostrarProveedores();
            pDialog.dismiss();
        }
    }

    private void mostrarProveedores() {
        List<String> listaCt = new ArrayList<>();
        for (int x = 0; x < listaProveedores.size(); x++) {
            listaCt.add(listaProveedores.get(x).getNombre());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCt);
        lvProveedores.setAdapter(arrayAdapter);
        lvProveedores.setDividerHeight(10);
    }

    private void actualizarProveedores() {

        listaProveedores = new ArrayList<>();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));
        params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));

        JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
        try {

            JSONArray mProveed = joDatos.getJSONArray(TAG_PROVEEDORES);

            for (int i = 0; i < mProveed.length(); i++) {
                JSONObject c = mProveed.getJSONObject(i);

                Proveedor ctPro = new Proveedor(
                        c.getInt(TAG_ID),
                        c.getInt(TAG_ID_EMPRESA),
                        c.getString(TAG_NOMBRE),
                        c.getString(TAG_CIF),
                        c.getString(TAG_DIRECCION),
                        c.getString(TAG_LOCALIDAD),
                        c.getString(TAG_PROVINCIA),
                        c.getString(TAG_C_POSTAL),
                        c.getString(TAG_MAIL));

                if (c.getString(TAG_BAJA).equals("S")) ctPro.setBaja(true);

                listaProveedores.add(ctPro);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
