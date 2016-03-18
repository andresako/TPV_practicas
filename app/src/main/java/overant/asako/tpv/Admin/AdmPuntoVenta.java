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
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.PuntoVenta;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.Herramientas;
import overant.asako.tpv.Utils.JSONParser;

public class AdmPuntoVenta extends Activity {

    private static final String URL_GUARDAR = "http://overant.es/json_guardar_puntos.php";

    // Tags de mi JSON php Script;
    private static final String TAG_ID = "id";
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_LOCALIDAD = "localidad";
    private static final String TAG_PROVINCIA = "provincia";
    private static final String TAG_C_POSTAL = "codigo_postal";
    private static final String TAG_BAJA = "baja";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private SharedPreferences sp;
    private JSONParser jsonParser = new JSONParser();
    private PuntoVenta puntoVenta;

    private TextView tTitulo, tNombre, tDireccion, tLocalidad, tProvincia, tCPostal;
    private Button btnOk, btnKo, btnBaja;
    private ImageButton ibUsuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_punto_venta);
        setUi();
        rellenarDatos();
    }

    private void setUi() {
        Intent i = getIntent();
        puntoVenta = (PuntoVenta) i.getSerializableExtra("pVenta");
        sp = PreferenceManager.getDefaultSharedPreferences(AdmPuntoVenta.this);

        tTitulo = (TextView) findViewById(R.id.admPVTitulo);
        tNombre = (TextView) findViewById(R.id.admPVNombre);
        tDireccion = (TextView) findViewById(R.id.admPVDireccion);
        tLocalidad = (TextView) findViewById(R.id.admPVLocalidad);
        tProvincia = (TextView) findViewById(R.id.admPVProvincia);
        tCPostal = (TextView) findViewById(R.id.admPVCPostal);

        btnOk = (Button) findViewById(R.id.admPVBtnOK);
        btnKo = (Button) findViewById(R.id.admPVBtnCanc);
        btnBaja = (Button) findViewById(R.id.admPVBtnBaja);

        ibUsuarios = (ImageButton) findViewById(R.id.admPVbtnUsuarios);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refrescarDatos();
                new GuardarCambios().execute(puntoVenta);
                new Herramientas().mensaje("Punto de venta actualizado", AdmPuntoVenta.this);
            }
        });
        btnKo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resp;
                if (puntoVenta.isBaja()) {
                    puntoVenta.setBaja(false);
                    resp = "alta";
                } else {
                    puntoVenta.setBaja(true);
                    resp = "baja";
                }
                refrescarDatos();
                new GuardarCambios().execute(puntoVenta);
                new Herramientas().mensaje("Punto de venta dado de " + resp, AdmPuntoVenta.this);
                finish();
            }
        });
        ibUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("puntoId", "" + puntoVenta.getID());
                edit.apply();

                Intent i = new Intent(AdmPuntoVenta.this, AdmListaUsuarios.class);
                startActivity(i);
            }
        });

        if(puntoVenta.getID() == 0)btnBaja.setVisibility(View.GONE);
    }

    private void rellenarDatos() {
        tTitulo.setText("Punto nº: " + puntoVenta.getID());
        tNombre.setText(puntoVenta.getNombre());
        tDireccion.setText(puntoVenta.getDireccion());
        tLocalidad.setText(puntoVenta.getLocalidad());
        tProvincia.setText(puntoVenta.getProvincia());
        tCPostal.setText(puntoVenta.getCodPostal());

        if (puntoVenta.isBaja()) {
            btnBaja.setText("Dar de alta");
            tTitulo.setPaintFlags(tTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void refrescarDatos() {
        puntoVenta.setNombre(tNombre.getText().toString());
        puntoVenta.setDireccion(tDireccion.getText().toString());
        puntoVenta.setLocalidad(tLocalidad.getText().toString());
        puntoVenta.setProvincia(tProvincia.getText().toString());
        puntoVenta.setCodPostal(tCPostal.getText().toString());
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

    class GuardarCambios extends AsyncTask<PuntoVenta, Void, String> {

        @Override
        protected String doInBackground(PuntoVenta... punto) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            PuntoVenta ctPunto = punto[0];

            params.add(new BasicNameValuePair("app", "1"));

            if (ctPunto.getID() == 0) {               //Nuevo punto
                params.add(new BasicNameValuePair("json_accion", "1"));
            } else {                              //Modificacion
                params.add(new BasicNameValuePair("json_accion", "2"));
                params.add(new BasicNameValuePair(TAG_ID, "" + ctPunto.getID()));
            }
            params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));
            params.add(new BasicNameValuePair(TAG_NOMBRE, ctPunto.getNombre()));
            params.add(new BasicNameValuePair(TAG_DIRECCION, ctPunto.getDireccion()));
            params.add(new BasicNameValuePair(TAG_LOCALIDAD, ctPunto.getLocalidad()));
            params.add(new BasicNameValuePair(TAG_PROVINCIA, ctPunto.getProvincia()));
            params.add(new BasicNameValuePair(TAG_C_POSTAL, ctPunto.getCodPostal()));
            String baja = "";
            if (ctPunto.isBaja()) baja = "S";
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
