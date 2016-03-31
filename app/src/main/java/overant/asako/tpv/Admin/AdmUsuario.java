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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.Usuario;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.JSONParser;

public class AdmUsuario extends Activity {

    private static final String URL_GUARDAR = "http://overant.es/json_guardar_usuarios.php";

    // Tags de mi JSON php Script;
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

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private SharedPreferences sp;
    private JSONParser jsonParser = new JSONParser();
    private Usuario user;

    private TextView tTitulo, tNombre, tApellido, tDni, tDireccion, tLocalidad, tProvincia, tCPostal, tTelefono, tMail, tPass;
    private RadioGroup rgPermisos;
    private RadioButton rbS, rbA, rbB;      //S -> SuperAdmin; A -> Admin; B -> Basic
    private Button btnOk, btnKo, btnBaja;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_usuario);
        setUi();
        rellenarDatos();
    }

    private void setUi() {
        Intent i = getIntent();
        user = (Usuario) i.getSerializableExtra("user");
        sp = PreferenceManager.getDefaultSharedPreferences(AdmUsuario.this);

        tTitulo = (TextView) findViewById(R.id.admUsrTitulo);
        tNombre = (TextView) findViewById(R.id.admUsrNombre);
        tApellido = (TextView) findViewById(R.id.admUsrApellido);
        tDni = (TextView) findViewById(R.id.admUsrDni);
        tDireccion = (TextView) findViewById(R.id.admUsrDireccion);
        tLocalidad = (TextView) findViewById(R.id.admUsrLocalidad);
        tProvincia = (TextView) findViewById(R.id.admUsrProvincia);
        tCPostal = (TextView) findViewById(R.id.admUsrCPostal);
        tTelefono = (TextView) findViewById(R.id.admUsrTelefono);
        tMail = (TextView) findViewById(R.id.admUsrMail);
        tPass = (TextView) findViewById(R.id.admUsrPass);

        rgPermisos = (RadioGroup) findViewById(R.id.admUsrrGrupo);
        rbA = (RadioButton) findViewById(R.id.admUsrrbAdmin);
        rbB = (RadioButton) findViewById(R.id.admUsrrbBasic);
        rbS = (RadioButton) findViewById(R.id.admUsrrbSuper);

        btnOk = (Button) findViewById(R.id.admUsrBtnOK);
        btnKo = (Button) findViewById(R.id.admUsrBtnCanc);
        btnBaja = (Button) findViewById(R.id.admUsrBtnBaja);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
                finish();
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
                darBaja();
                guardarDatos();
                finish();
            }
        });

        if (user.getID() == 0) btnBaja.setVisibility(View.GONE);

        String admin = sp.getString("admin", "");
        if (!admin.equals("S")) {
            tPass.setVisibility(View.GONE);
            rgPermisos.setVisibility(View.GONE);
        }
    }

    private void rellenarDatos() {
        tTitulo.setText("Usuario Sr/a: " + user.getNombre() + " " + user.getApellidos());
        tNombre.setText(user.getNombre());
        tApellido.setText(user.getApellidos());
        tDni.setText(user.getDNI());
        tDireccion.setText(user.getDireccion());
        tLocalidad.setText(user.getLocalidad());
        tProvincia.setText(user.getProvincia());
        tCPostal.setText(user.getcPostal());
        tTelefono.setText(user.getTelefono());
        tMail.setText(user.getMail());
        tPass.setText(user.getContrasena());

        switch (user.getAdmin()) {
            case "A":
                rbA.setChecked(true);
                break;
            case "S":
                rbS.setChecked(true);
                break;
            case "B":
                rbB.setChecked(true);
                break;
            default:
                rbB.setChecked(true);
                break;
        }
        if (user.isBaja()) {
            btnBaja.setText("Dar de alta");
            tTitulo.setPaintFlags(tTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
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
        input.setSingleLine(true);
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

    private void darBaja() {
        user.setBaja(!user.isBaja());
    }

    private void guardarDatos() {
        new GuardarUser().execute();
    }

    class GuardarUser extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app", "1"));

            if (user.getID() == 0) {     //nueva usuario
                params.add(new BasicNameValuePair("json_accion", "1"));
            } else {                  //actualizar usuario
                params.add(new BasicNameValuePair("json_accion", "2"));
                params.add(new BasicNameValuePair("id", "" + user.getID()));

                String ctBaja = "";
                if (user.isBaja()) ctBaja = "S";
                params.add(new BasicNameValuePair(TAG_BAJA, ctBaja));
            }

            params.add(new BasicNameValuePair(TAG_ID_PUNTO, sp.getString("puntoId", "0")));
            params.add(new BasicNameValuePair(TAG_NOMBRE, tNombre.getText().toString()));
            params.add(new BasicNameValuePair(TAG_APELLIDO, tApellido.getText().toString()));
            params.add(new BasicNameValuePair(TAG_DNI, tDni.getText().toString()));
            params.add(new BasicNameValuePair(TAG_DIRECCION, tDireccion.getText().toString()));
            params.add(new BasicNameValuePair(TAG_LOCALIDAD, tLocalidad.getText().toString()));
            params.add(new BasicNameValuePair(TAG_PROVINCIA, tProvincia.getText().toString()));
            params.add(new BasicNameValuePair(TAG_CPOSTAL, tCPostal.getText().toString()));
            params.add(new BasicNameValuePair(TAG_TELEFONO, tTelefono.getText().toString()));
            params.add(new BasicNameValuePair(TAG_MAIL, tMail.getText().toString()));
            params.add(new BasicNameValuePair(TAG_PASS, tPass.getText().toString()));

            String permCt = "";
            switch (rgPermisos.getCheckedRadioButtonId()) {
                case R.id.admUsrrbAdmin:
                    permCt = "A";
                    break;
                case R.id.admUsrrbBasic:
                    permCt = "B";
                    break;
                case R.id.admUsrrbSuper:
                    permCt = "S";
                    break;
            }
            params.add(new BasicNameValuePair(TAG_ADMIN, permCt));

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
                    Log.d("Usuario actualizado!", json.toString());
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

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            // pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(AdmUsuario.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
