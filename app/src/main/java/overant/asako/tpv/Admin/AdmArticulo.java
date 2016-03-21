package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.Articulo;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.Herramientas;
import overant.asako.tpv.Utils.JSONParser;

public class AdmArticulo extends Activity {

    private SharedPreferences sp;

    // Tags de mi JSON php Script;
    private static final String URL_GUARDAR = "http://overant.es/json_guardar_articulos.php";

    private static final String TAG_ID = "id";
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_ID_IVA = "id_tipo_iva";
    private static final String TAG_ID_CATEGORIA = "id_categoria";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_NOMBRE_IVA = "nombre_iva";
    private static final String TAG_NOMBRE_CATEGORIA = "nombre_familia";
    private static final String TAG_EAN = "ean";
    private static final String TAG_FOTO = "foto";
    private static final String TAG_PRECIO = "precio";
    private static final String TAG_DESCUENTO = "descuento";
    private static final String TAG_BAJA = "baja";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private JSONParser jsonParser = new JSONParser();
    private Articulo articulo;

    private TextView tTitulo, tNombre, tEAN, tPrecio, tDescuento, tCategoria, tIva;
    private ImageView iFoto;
    private Button btnOk, btnKo, btnBaja;
    private Spinner sCategoria, sIva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_articulo);
        setUi();
        rellenarDatos();
    }

    private void setUi() {
        Intent i = getIntent();
        articulo = (Articulo) i.getSerializableExtra("articulo");
        sp = PreferenceManager.getDefaultSharedPreferences(AdmArticulo.this);

        tTitulo = (TextView) findViewById(R.id.admArtTitulo);
        tNombre = (TextView) findViewById(R.id.admArtNombre);
        tEAN = (TextView) findViewById(R.id.admArtEAN);
        tPrecio = (TextView) findViewById(R.id.admArtPrecio);
        tDescuento = (TextView) findViewById(R.id.admArtDescuento);
        tCategoria = (TextView) findViewById(R.id.admArtCategoriaT);
        tIva = (TextView) findViewById(R.id.admArtIvaT);

        sCategoria = (Spinner) findViewById(R.id.admArtCategoriaS);
        sIva = (Spinner) findViewById(R.id.admArtIvaS);

        iFoto = (ImageView) findViewById(R.id.admArtFoto);

        btnOk = (Button) findViewById(R.id.admArtBtnOK);
        btnKo = (Button) findViewById(R.id.admArtBtnCanc);
        btnBaja = (Button) findViewById(R.id.admArtBtnBaja);

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
        if (articulo.getID() == 0) btnBaja.setVisibility(View.GONE);
    }

    private void rellenarDatos() {
        tTitulo.setText("Articulo: " + articulo.getID() + ", " + articulo.getNombre());
        tNombre.setText(articulo.getNombre());
        tEAN.setText(articulo.getEAN());
        tPrecio.setText(articulo.getPrecio().toString());
        tDescuento.setText(articulo.getDescuento().toString());
        tCategoria.setText(articulo.getNombreCat());
        tIva.setText(articulo.getNombreIva());

        if (articulo.isBaja()) {
            btnBaja.setText("Dar de alta");
            tTitulo.setPaintFlags(tTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if(!articulo.getFoto().equals("")) {
            new Herramientas.ponerImagen(iFoto).execute(articulo.getFoto());
        }

    }

    private void darBaja() {
        articulo.setBaja(!articulo.isBaja());
    }

    private void guardarDatos() {
        new GuardarArticulo().execute();
    }

    public void editThis(View v) {
        final TextView ct = (TextView) v;

        if(ct.getId() != R.id.admArtIvaT && ct.getId() != R.id.admArtCategoriaT) {
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
        }else{
//TODO
        }
    }


    class GuardarArticulo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app", "1"));

            if (articulo.getID() == 0) {     //nueva articulo
                params.add(new BasicNameValuePair("json_accion", "1"));
            } else {                  //actualizar articulo
                params.add(new BasicNameValuePair("json_accion", "2"));
                params.add(new BasicNameValuePair(TAG_ID, "" + articulo.getID()));

                String ctBaja = "";
                if (articulo.isBaja()) ctBaja = "S";
                params.add(new BasicNameValuePair(TAG_BAJA, ctBaja));
            }

            params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));
            params.add(new BasicNameValuePair(TAG_NOMBRE, tNombre.getText().toString()));
            params.add(new BasicNameValuePair(TAG_NOMBRE_IVA, tIva.getText().toString()));
            params.add(new BasicNameValuePair(TAG_NOMBRE_CATEGORIA, tCategoria.getText().toString()));
            params.add(new BasicNameValuePair(TAG_EAN, tEAN.getText().toString()));
            params.add(new BasicNameValuePair(TAG_FOTO, articulo.getFoto()));
            params.add(new BasicNameValuePair(TAG_PRECIO, tPrecio.getText().toString()));
            params.add(new BasicNameValuePair(TAG_DESCUENTO, tDescuento.getText().toString()));
            params.add(new BasicNameValuePair(TAG_ID_IVA, articulo.getIdCategoria() + ""));
            params.add(new BasicNameValuePair(TAG_ID_CATEGORIA, articulo.getIdIva() + ""));

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
                    Log.d("Articulo actualizado!", json.toString());
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
                Toast.makeText(AdmArticulo.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
