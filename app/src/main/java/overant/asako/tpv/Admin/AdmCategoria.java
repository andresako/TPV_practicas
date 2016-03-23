package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.Categoria;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.Herramientas;
import overant.asako.tpv.Utils.JSONParser;

public class AdmCategoria extends Activity {

    private static final String URL_GUARDAR = "http://overant.es/json_guardar_familias.php";

    // Tags de mi JSON php Script;
    private static final String TAG_ID = "id";
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_FOTO = "foto";
    private static final String TAG_BAJA = "baja";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private SharedPreferences sp;
    private JSONParser jsonParser = new JSONParser();
    private Categoria categoria;

    private TextView tTitulo, tNombre;
    private ImageView iFoto;
    private Button btnOk, btnKo, btnBaja;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_categoria);
        setUi();
        rellenarDatos();
    }

    private void setUi() {
        Intent i = getIntent();
        categoria = (Categoria) i.getSerializableExtra("categoria");
        sp = PreferenceManager.getDefaultSharedPreferences(AdmCategoria.this);

        tTitulo = (TextView) findViewById(R.id.admCatTitulo);
        tNombre = (TextView) findViewById(R.id.admCatNombre);
        iFoto = (ImageView)findViewById(R.id.admCatFoto);

        btnOk = (Button) findViewById(R.id.admCatBtnOK);
        btnKo = (Button) findViewById(R.id.admCatBtnCanc);
        btnBaja = (Button) findViewById(R.id.admCatBtnBaja);

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
        if (categoria.getID() == 0) btnBaja.setVisibility(View.GONE);
    }

    private void rellenarDatos() {
        tTitulo.setText("Categoria id: " + categoria.getID());
        tNombre.setText(categoria.getNombre());

        if(categoria.getFoto() != null && !categoria.getFoto().isEmpty()) {
            new Herramientas.ponerImagen(iFoto).execute(categoria.getFoto());
        }
        
        if (categoria.isBaja()) {
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
        categoria.setBaja(!categoria.isBaja());
    }

    private void guardarDatos() {
        new GuardarCategoria().execute();
    }

    class GuardarCategoria extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app", "1"));

            if (categoria.getID() == 0) {     //nueva categoria
                params.add(new BasicNameValuePair("json_accion", "1"));
            } else {                  //actualizar categoria
                params.add(new BasicNameValuePair("json_accion", "2"));
                params.add(new BasicNameValuePair(TAG_ID, "" + categoria.getID()));

                String ctBaja = "";
                if (categoria.isBaja()) ctBaja = "S";
                params.add(new BasicNameValuePair(TAG_BAJA, ctBaja));
            }

            params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));
            params.add(new BasicNameValuePair(TAG_NOMBRE, tNombre.getText().toString()));
            params.add(new BasicNameValuePair(TAG_FOTO, categoria.getFoto()));

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
                    Log.d("Categoria actualizada!", json.toString());
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
            if (file_url != null) {
                Toast.makeText(AdmCategoria.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
