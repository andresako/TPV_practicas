package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
                // TODO: 18/03/2016 crear lista de o
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
                // TODO: 18/03/2016
            }
        });

        if (user.getID() == 0) btnBaja.setVisibility(View.GONE);

        String admin = sp.getString("admin", "");
        if(!admin.equals("S")) {
            tPass.setVisibility(View.GONE);
            rgPermisos.setVisibility(View.GONE);
        }

        // TODO: 18/03/2016
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
}
