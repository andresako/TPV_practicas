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
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.Proveedor;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.Herramientas;
import overant.asako.tpv.Utils.JSONParser;

public class AdmProveedor extends Activity {

    private static final String URL_GUARDAR = "http://overant.es/json_guardar_proveedores.php";

    private static final String TAG_ID = "id";
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_CIF = "cif";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_LOCALIDAD = "localidad";
    private static final String TAG_PROVINCIA = "provincia";
    private static final String TAG_C_POSTAL = "codigo_postal";
    private static final String TAG_MAIL = "email";
    private static final String TAG_BAJA = "baja";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private JSONParser jsonParser = new JSONParser();
    private SharedPreferences sp;

    public TextView pTitulo, pNombre, pCif, pDirecc, pLocal, pProv, pCpostal, pMail;
    public Button cOk, cKo, cBaja;

    Proveedor proveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_proveedor);

        setUi();
        rellenarDatos();

    }

    private void setUi() {
        Intent i = getIntent();
        proveedor = (Proveedor) i.getSerializableExtra("proveedor");
        sp = PreferenceManager.getDefaultSharedPreferences(AdmProveedor.this);

        pTitulo = (TextView) findViewById(R.id.admProTitulo);
        pNombre = (TextView) findViewById(R.id.admProNombre);
        pDirecc = (TextView) findViewById(R.id.admProDireccion);
        pLocal = (TextView) findViewById(R.id.admProLocalidad);
        pProv = (TextView) findViewById(R.id.admProProvincia);
        pCpostal = (TextView) findViewById(R.id.admProCodPostal);
        pMail = (TextView) findViewById(R.id.admProMail);
        pCif = (TextView) findViewById(R.id.admProCIF);

        cOk = (Button) findViewById(R.id.admProBtnOK);
        cKo = (Button) findViewById(R.id.admProBtnCanc);
        cBaja = (Button) findViewById(R.id.admProBtnBaja);

        cOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refrescarDatos();
                new GuardarCambios().execute(proveedor);
                new Herramientas().mensaje("Proveedor actualizado", AdmProveedor.this);
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
                if (proveedor.isBaja()) {
                    proveedor.setBaja(false);
                    resp = "alta";
                } else {
                    proveedor.setBaja(true);
                    resp = "baja";
                }
                refrescarDatos();
                new GuardarCambios().execute(proveedor);
                new Herramientas().mensaje("Proveedor dado de " + resp, AdmProveedor.this);
                finish();
            }
        });

        if (proveedor.getId() == 0) cBaja.setVisibility(View.GONE);
    }

    private void rellenarDatos() {
        pTitulo.setText(proveedor.getNombre());
        pNombre.setText(proveedor.getNombre());
        pDirecc.setText(proveedor.getDireccion());
        pLocal.setText(proveedor.getLocalidad());
        pProv.setText(proveedor.getProvincia());
        pCpostal.setText(proveedor.getCodPostal());
        pMail.setText(proveedor.getMail());
        pCif.setText(proveedor.getCif());

        if (proveedor.isBaja()) {
            cBaja.setText("Dar de alta");
            pTitulo.setPaintFlags(pTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void refrescarDatos() {
        proveedor.setNombre(pNombre.getText().toString());
        proveedor.setDireccion(pDirecc.getText().toString());
        proveedor.setLocalidad(pLocal.getText().toString());
        proveedor.setProvincia(pProv.getText().toString());
        proveedor.setCodPostal(pCpostal.getText().toString());
        proveedor.setMail(pMail.getText().toString());
        proveedor.setCif(pCif.getText().toString());
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

    class GuardarCambios extends AsyncTask<Proveedor, Void, String> {

        @Override
        protected String doInBackground(Proveedor... proveedor) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            Proveedor pro = proveedor[0];

            params.add(new BasicNameValuePair("app", "1"));

            if (pro.getId() == 0) {               //Nuevo proveedor
                params.add(new BasicNameValuePair("json_accion", "1"));
            } else {                              //Modificacion
                params.add(new BasicNameValuePair("json_accion", "2"));
                params.add(new BasicNameValuePair(TAG_ID, "" + pro.getId()));
            }
            params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));
            params.add(new BasicNameValuePair(TAG_NOMBRE, pro.getNombre()));
            params.add(new BasicNameValuePair(TAG_DIRECCION, pro.getDireccion()));
            params.add(new BasicNameValuePair(TAG_LOCALIDAD, pro.getLocalidad()));
            params.add(new BasicNameValuePair(TAG_PROVINCIA, pro.getProvincia()));
            params.add(new BasicNameValuePair(TAG_C_POSTAL, pro.getCodPostal()));
            params.add(new BasicNameValuePair(TAG_MAIL, pro.getMail()));
            params.add(new BasicNameValuePair(TAG_CIF, pro.getCif()));
            String baja = "";
            if (pro.isBaja()) baja = "S";
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
                    Log.d("Proveeedor actualizado!", json.toString());
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
