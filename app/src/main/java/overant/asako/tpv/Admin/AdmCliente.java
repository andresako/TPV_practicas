package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.Cliente;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.Herramientas;
import overant.asako.tpv.Utils.JSONParser;

public class AdmCliente extends Activity {

    private SharedPreferences sp;

    // Tags de mi JSON php Script;
    private static final String URL_GUARDAR = "http://overant.es/json_guardar_cliente.php";

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

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private JSONParser jsonParser = new JSONParser();
    private Cliente cliente;

    public TextView cTitulo, cNombre, cApellido, cDNI, cDirecc, cLocal, cProv, cCpostal, cTelf, cMail, cNomComercial, cRazSocial, cCif;
    public ImageButton ibEmpresa, ibCliente;
    public Button cOk, cKo, cBaja;
    public LinearLayout llCliente, llEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_cliente);
        setUi();
        rellenarDatos();

    }

    private void setUi() {
        Intent i = getIntent();
        cliente = (Cliente) i.getSerializableExtra("cliente");
        sp = PreferenceManager.getDefaultSharedPreferences(AdmCliente.this);

        cTitulo = (TextView) findViewById(R.id.admCliTitulo);
        cNombre = (TextView) findViewById(R.id.admCliNombre);
        cApellido = (TextView) findViewById(R.id.admCliApellidos);
        cDNI = (TextView) findViewById(R.id.admCliDNI);
        cDirecc = (TextView) findViewById(R.id.admCliDireccion);
        cLocal = (TextView) findViewById(R.id.admCliLocalidad);
        cProv = (TextView) findViewById(R.id.admCliProvincia);
        cCpostal = (TextView) findViewById(R.id.admCliCPostal);
        cTelf = (TextView) findViewById(R.id.admCliTelefono);
        cMail = (TextView) findViewById(R.id.admCliEmail);
        cNomComercial = (TextView) findViewById(R.id.admCliNomComercial);
        cCif = (TextView) findViewById(R.id.admCliCif);
        cRazSocial = (TextView) findViewById(R.id.admCliRazSocial);

        llCliente = (LinearLayout) findViewById(R.id.admCliLayCli);
        llEmpresa = (LinearLayout) findViewById(R.id.admCliLayEmp);

        ibCliente = (ImageButton) findViewById(R.id.admCliCliente);
        ibEmpresa = (ImageButton) findViewById(R.id.admCliEmpresa);

        cOk = (Button) findViewById(R.id.admCliBtnOK);
        cKo = (Button) findViewById(R.id.admCliBtnCanc);
        cBaja = (Button) findViewById(R.id.admCliBtnBaja);

        cOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refrescarDatos();
                new GuardarCambios().execute(cliente);
                new Herramientas().mensaje("Cliente actualizado", AdmCliente.this);
            }
        });
        cKo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resp;
                if (cliente.isBaja()) {
                    cliente.setBaja(false);
                    resp = "alta";
                } else {
                    cliente.setBaja(true);
                    resp = "baja";
                }
                refrescarDatos();
                new GuardarCambios().execute(cliente);
                new Herramientas().mensaje("Cliente dado de " + resp, AdmCliente.this);
                finish();
            }
        });

        ibCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCliente.setVisibility(View.VISIBLE);
                llEmpresa.setVisibility(View.GONE);
                ibCliente.setVisibility(View.GONE);
                ibEmpresa.setVisibility(View.VISIBLE);
            }
        });
        ibEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCliente.setVisibility(View.GONE);
                llEmpresa.setVisibility(View.VISIBLE);
                ibCliente.setVisibility(View.VISIBLE);
                ibEmpresa.setVisibility(View.GONE);
            }
        });

        if (cliente.getId() == 0) cBaja.setVisibility(View.GONE);
    }

    private void rellenarDatos() {
        cTitulo.setText("Editando: " + cliente.getNombre());
        cNombre.setText(cliente.getNombre());
        cApellido.setText(cliente.getApellido());
        cDNI.setText(cliente.getDni());
        cDirecc.setText(cliente.getDireccion());
        cLocal.setText(cliente.getLocalidad());
        cProv.setText(cliente.getProvincia());
        cCpostal.setText(cliente.getcPostal());
        cTelf.setText(cliente.getTelefono());
        cMail.setText(cliente.getMail());
        cNomComercial.setText(cliente.getNomComercial());
        cRazSocial.setText(cliente.getRazSocial());
        cCif.setText(cliente.getCif());

        if (cliente.isBaja()) {
            cBaja.setText("Dar de alta");
            cTitulo.setPaintFlags(cTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void refrescarDatos() {
        cliente.setNombre(cNombre.getText().toString());
        cliente.setApellido(cApellido.getText().toString());
        cliente.setDni(cDNI.getText().toString());
        cliente.setDireccion(cDirecc.getText().toString());
        cliente.setLocalidad(cLocal.getText().toString());
        cliente.setProvincia(cProv.getText().toString());
        cliente.setcPostal(cCpostal.getText().toString());
        cliente.setTelefono(cTelf.getText().toString());
        cliente.setMail(cMail.getText().toString());
        cliente.setNomComercial(cNomComercial.getText().toString());
        cliente.setRazSocial(cRazSocial.getText().toString());
        cliente.setCif(cCif.getText().toString());

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
            params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));
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
            params.add(new BasicNameValuePair(TAG_NOMBRE_COMERCIAL, cli.getNomComercial()));
            params.add(new BasicNameValuePair(TAG_RAZON_SOCIAL, cli.getRazSocial()));
            params.add(new BasicNameValuePair(TAG_CIF, cli.getCif()));

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
