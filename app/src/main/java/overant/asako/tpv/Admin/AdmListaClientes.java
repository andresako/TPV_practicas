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

import overant.asako.tpv.R;
import overant.asako.tpv.Clases.Cliente;
import overant.asako.tpv.Utils.JSONParser;

public class AdmListaClientes extends Activity {

    private SharedPreferences sp;

    private static final String URL = "http://overant.es/clientes.php";

    // Tags de mi JSON php Script;
    private static final String TAG_CLIENTE = "clientes";
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_NOMBRE_COMERCIAL = "nombre_comercial";
    private static final String TAG_RAZON_SOCIAL = "razon_social";
    private static final String TAG_CIF = "cif";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_APELLIDOS = "apellidos";
    private static final String TAG_DNI = "dni";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_LOCALIDAD = "localidad";
    private static final String TAG_PROVINCIA = "provincia";
    private static final String TAG_C_POSTAL = "cpostal";
    private static final String TAG_TELEFONO = "telefono";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_BAJA = "baja";

    //Datos
    private JSONParser jsonParser = new JSONParser();
    public List<Cliente> listaClientes;
    private ProgressDialog pDialog;

    private TextView tituloLista;
    private ListView lvClientes;
    private Button bNuevoCli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_lista);

        setUi();

    }

    private void setUi() {
        sp = PreferenceManager.getDefaultSharedPreferences(AdmListaClientes.this);

        tituloLista = (TextView) findViewById(R.id.AdmListaTitulo);
        bNuevoCli = (Button) findViewById(R.id.AdmListaBoton);
        lvClientes = (ListView) findViewById(R.id.admListaView);

        tituloLista.setText("Selecciona Cliente");
        bNuevoCli.setText("Nuevo cliente");

        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AdmListaClientes.this, AdmCliente.class);
                i.putExtra("cliente", listaClientes.get(position));
                startActivity(i);
            }
        });
        bNuevoCli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdmListaClientes.this, AdmCliente.class);
                i.putExtra("cliente", new Cliente());
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new rellenarClientes().execute();
    }

    private void actualizarClientes() {
        //Clientes directamente de la BD
        listaClientes = new ArrayList<>();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));
        params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));

        JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
        try {

            JSONArray mCliente = joDatos.getJSONArray(TAG_CLIENTE);

            for (int i = 0; i < mCliente.length(); i++) {
                JSONObject c = mCliente.getJSONObject(i);

                Cliente ctCli = new Cliente(
                        c.getInt(TAG_ID),
                        c.getString(TAG_NOMBRE),
                        c.getString(TAG_APELLIDOS),
                        c.getString(TAG_DNI),
                        c.getString(TAG_DIRECCION),
                        c.getString(TAG_LOCALIDAD),
                        c.getString(TAG_PROVINCIA),
                        c.getString(TAG_C_POSTAL),
                        c.getString(TAG_TELEFONO),
                        c.getString(TAG_EMAIL),
                        c.getString(TAG_NOMBRE_COMERCIAL),
                        c.getString(TAG_RAZON_SOCIAL),
                        c.getString(TAG_CIF));

                if (c.getString(TAG_BAJA).equals("S")) ctCli.setBaja(true);

                listaClientes.add(ctCli);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mostrarClientes() {
        List<String> listaCt = new ArrayList<>();
        for (int x = 0; x < listaClientes.size(); x++) {
            listaCt.add(listaClientes.get(x).getNombre() + " " + listaClientes.get(x).getApellido());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCt);
        lvClientes.setAdapter(arrayAdapter);
        lvClientes.setDividerHeight(10);
    }

    public class rellenarClientes extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isFinishing()) {
                pDialog = new ProgressDialog(AdmListaClientes.this);
                pDialog.setMessage("Cargando datos...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            actualizarClientes();
            return Boolean.parseBoolean(null);
        }

        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            mostrarClientes();
            if (!isFinishing()) pDialog.dismiss();
        }
    }
}


