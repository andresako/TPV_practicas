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

import overant.asako.tpv.Clases.Usuario;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.JSONParser;

public class AdmListaUsuarios extends Activity {

    private static final String URL = "http://overant.es/usuarios.php";

    // Tags de mi JSON php Script;
    private static final String TAG_USUARIOS = "usuarios";
    private static final String TAG_ID = "id";
    private static final String TAG_ID_PUNTO = "id_punto_venta";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_APELLIDO = "apellidos";
    private static final String TAG_DNI = "dni";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_LOCALIDAD = "localidad";
    private static final String TAG_PROVINCIA = "provincia";
    private static final String TAG_CPOSTAL = "cpostal";
    private static final String TAG_TELEFONO = "telefono";
    private static final String TAG_MAIL = "email";
    private static final String TAG_PASS = "contrasena";
    private static final String TAG_ADMIN = "admin";
    private static final String TAG_BAJA = "baja";

    private List<Usuario> listaUsuarios;
    private SharedPreferences sp;
    private JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    private TextView tituloLista;
    private ListView lvUsuarios;
    private Button bNuevoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_lista);
        setUi();
    }

    private void setUi() {
        sp = PreferenceManager.getDefaultSharedPreferences(AdmListaUsuarios.this);

        tituloLista = (TextView) findViewById(R.id.AdmListaTitulo);
        bNuevoUser = (Button) findViewById(R.id.AdmListaBoton);
        lvUsuarios = (ListView) findViewById(R.id.admListaView);

        tituloLista.setText("Selecciona Usuario");
        bNuevoUser.setText("Nuevo user");

        bNuevoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdmListaUsuarios.this, AdmUsuario.class);
                i.putExtra("user", new Usuario());
                startActivity(i);
            }
        });
        lvUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AdmListaUsuarios.this, AdmUsuario.class);
                i.putExtra("user", listaUsuarios.get(position));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new rellenarUsers().execute();
    }

    private void actualizarListaUsers() {
        listaUsuarios = new ArrayList<>();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));
        params.add(new BasicNameValuePair(TAG_ID_PUNTO, sp.getString("puntoId", "0")));

        JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
        System.err.println(joDatos);
        try {

            JSONArray mUser = joDatos.getJSONArray(TAG_USUARIOS);

            for (int i = 0; i < mUser.length(); i++) {
                JSONObject c = mUser.getJSONObject(i);

                Usuario ctUser = new Usuario(
                        c.getInt(TAG_ID),
                        c.getInt(TAG_ID_PUNTO),
                        c.getString(TAG_NOMBRE),
                        c.getString(TAG_APELLIDO),
                        c.getString(TAG_DNI),
                        c.getString(TAG_DIRECCION),
                        c.getString(TAG_LOCALIDAD),
                        c.getString(TAG_PROVINCIA),
                        c.getString(TAG_CPOSTAL),
                        c.getString(TAG_TELEFONO),
                        c.getString(TAG_MAIL),
                        c.getString(TAG_PASS),
                        c.getString(TAG_ADMIN));
                if (c.getString(TAG_BAJA).equals("S")) ctUser.setBaja(true);

                listaUsuarios.add(ctUser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mostrarListaUsers() {
        List<String> listaCt = new ArrayList<>();
        for (int x = 0; x < listaUsuarios.size(); x++) {
            listaCt.add(listaUsuarios.get(x).getNombre() + " " + listaUsuarios.get(x).getApellidos());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCt);
        lvUsuarios.setAdapter(arrayAdapter);
        lvUsuarios.setDividerHeight(10);
    }

    public class rellenarUsers extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isFinishing()) {
                pDialog = new ProgressDialog(AdmListaUsuarios.this);
                pDialog.setMessage("Cargando datos...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            actualizarListaUsers();
            return Boolean.parseBoolean(null);
        }

        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            mostrarListaUsers();
            if (!isFinishing()) pDialog.dismiss();
        }
    }
}
