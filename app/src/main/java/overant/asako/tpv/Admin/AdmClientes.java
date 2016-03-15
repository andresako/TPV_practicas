package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import overant.asako.tpv.R;
import overant.asako.tpv.Utils.ClienteAdapter;
import overant.asako.tpv.Clases.Cliente;
import overant.asako.tpv.Utils.JSONParser;

public class AdmClientes extends Activity {

    // Direccion de testeo      // Rayco PC
    private static final String URL_CLIENT = "http://overant.es/clientes.php";

    // Respuestas del JSON php Script;
    private static final String TAG_CLIENTE = "clientes";
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

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    //Datos
    private JSONParser jsonParser = new JSONParser();
    private JSONObject joDatos;
    private JSONArray mCliente = null;
    private ArrayList<HashMap<String, String>> userList;
    private List<Cliente> items;
    private ProgressDialog pDialog;

    // Vistas
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_clientes);

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.AdmClientes);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        new rellenarClientes().execute();
    }

    private void actualizarClientes() {
        //Clientes de prueba
        items = new ArrayList<>();
        items.add(new Cliente(1, "Andres", "Martinez", "12312312G", "Avd/ nas, 123", "Alicante", "Alicante", "03560", "666123123", "andresako@gmail.com"));
        items.add(new Cliente(2, "Jose", "Perez", "12312312G", "Avd/ nas, 123", "Alicante", "Alicante", "03560", "666123123", "andresako@gmail.com"));
        items.add(new Cliente(3, "Pablo", "Garcia", "12312312G", "Avd/ nas, 123", "Alicante", "Alicante", "03560", "666123123", "andresako@gmail.com"));

        //Clientes reales
        items = new ArrayList<>();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));

        joDatos = jsonParser.peticionHttp(URL_CLIENT, "POST", params);

        try {

            mCliente = joDatos.getJSONArray(TAG_CLIENTE);

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
                        c.getString(TAG_EMAIL));

                items.add(ctCli);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mostrarClientes(){
        // Crear un nuevo adaptador
        adapter = new ClienteAdapter(items);
        recycler.setAdapter(adapter);
    }

    public void editThis(View v) {
        final TextView ct = (TextView) v;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Editar el campo");
        alert.setMessage("Anterior: " + ct.getText());

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(ct.getText());
        input.setSelection(input.getText().length());
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ct.setText(input.getText());
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelado.
            }
        });
        alert.show();
    }

    public class rellenarClientes extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdmClientes.this);
            pDialog.setMessage("Cargando datos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            actualizarClientes();
            return Boolean.parseBoolean(null);
        }
        protected void onPostExecute(Boolean resultado) {
            // dismiss the dialog once product deleted
            super.onPostExecute(resultado);
            pDialog.dismiss();
            mostrarClientes();
        }
    }

    public void guardarCliente(int pos){
        System.out.println(pos);
    }

}


