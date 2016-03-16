package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    private SharedPreferences sp;

    private static final String URL = "http://overant.es/clientes.php";
    private static final String URL_GUARDAR = "http://overant.es/json_guardar_cliente.php";

    // Respuestas del JSON php Script;
    private static final String TAG_CLIENTE = "clientes";
    private static final String TAG_ID_EMPRESA = "id_empresa";
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
    private List<Cliente> listaClientes;
    private ProgressDialog pDialog;

    // Vistas
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_clientes);

        sp = PreferenceManager.getDefaultSharedPreferences(AdmClientes.this);

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.AdmClientes);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

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

        joDatos = jsonParser.peticionHttp(URL, "POST", params);

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

                listaClientes.add(ctCli);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mostrarClientes() {
        // Crear un nuevo adaptador
        adapter = new ClienteAdapter(listaClientes, this);
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

    public void actualizarCliente(ClienteAdapter.ViewHolder hdl) {
        listaClientes.get(hdl.pos).setNombre(hdl.cNombre.getText().toString());
        listaClientes.get(hdl.pos).setApellido(hdl.cApellido.getText().toString());
        listaClientes.get(hdl.pos).setDni(hdl.cDNI.getText().toString());
        listaClientes.get(hdl.pos).setDireccion(hdl.cDirecc.getText().toString());
        listaClientes.get(hdl.pos).setLocalidad(hdl.cLocal.getText().toString());
        listaClientes.get(hdl.pos).setProvincia(hdl.cProv.getText().toString());
        listaClientes.get(hdl.pos).setcPostal(hdl.cCpostal.getText().toString());
        listaClientes.get(hdl.pos).setTelefono(hdl.cTelf.getText().toString());
        listaClientes.get(hdl.pos).setBaja(hdl.cBaja.isChecked());

        new GuardarCambios().execute(listaClientes.get(hdl.pos));

        mostrarClientes();
    }

    class GuardarCambios extends AsyncTask<Cliente, Void, String> {

        @Override
        protected String doInBackground(Cliente... cliente) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            Cliente cli = cliente[0];

            params.add(new BasicNameValuePair("app", "1"));

            if (cli.getId() == 0) {               //Nuevo cliente
                params.add(new BasicNameValuePair("json_accion", "1"));
            } else {                              //Modificacion
                params.add(new BasicNameValuePair("json_accion", "2"));
                params.add(new BasicNameValuePair(TAG_ID, "" + cli.getId()));
            }
            params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", null)));
            params.add(new BasicNameValuePair(TAG_NOMBRE, cli.getNombre()));
            params.add(new BasicNameValuePair(TAG_APELLIDOS, cli.getApellido()));
            params.add(new BasicNameValuePair(TAG_DNI, cli.getDni()));
            params.add(new BasicNameValuePair(TAG_DIRECCION, cli.getDireccion()));
            params.add(new BasicNameValuePair(TAG_LOCALIDAD, cli.getLocalidad()));
            params.add(new BasicNameValuePair(TAG_PROVINCIA, cli.getProvincia()));
            params.add(new BasicNameValuePair(TAG_C_POSTAL, cli.getcPostal()));
            params.add(new BasicNameValuePair(TAG_TELEFONO, cli.getTelefono()));
            params.add(new BasicNameValuePair(TAG_EMAIL, cli.getMail()));
            String baja = "";
            if (cli.isBaja()) baja = "S";
            params.add(new BasicNameValuePair(TAG_BAJA, baja));

            Log.d("request!", "starting");

            try {
                //Posting user data to script
                JSONObject json = jsonParser.peticionHttp(URL_GUARDAR, "POST", params);

                // full json response
                Log.d("Post Comment attempt", json.toString());

                // json success element
                int success;
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Cliente actualizado!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Fallo al guardar!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}


