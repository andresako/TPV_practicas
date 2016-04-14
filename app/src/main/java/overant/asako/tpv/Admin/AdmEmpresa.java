package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.R;
import overant.asako.tpv.Utils.Herramientas;
import overant.asako.tpv.Utils.JSONParser;

public class AdmEmpresa extends Activity {

    private static final String URL = "http://overant.es/empresas.php";
    private static final String URL_GUARDAR = "http://overant.es/json_guardar_empresa.php";
    private static final String RUTA_GALERIA = "http://overant.es/galeria/";

    // Respuestas del JSON php Script;
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_RAZON = "razon";
    private static final String TAG_CIF = "cif";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_LOCALIDAD = "localidad";
    private static final String TAG_PROVINCIA = "provincia";
    private static final String TAG_C_POSTAL = "cpostal";
    private static final String TAG_TELEFONO = "telefono";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_LOGO = "logo";
    private static final String TAG_BAJA = "baja";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    //Componentes
    private SharedPreferences sp;
    private boolean eBaja = false;
    private String ID;
    private TextView eTitulo, eNombre, eRazon, eCif, eDireccion, eLocalidad, eProvincia, eCpostal, eTelefono, eEmail;
    private Button bOk, bCanc, bBaja;
    private ImageView eLogo;

    //Datos
    private JSONParser jsonParser;
    private Herramientas tools;
    private JSONObject joDatos;
    private ProgressDialog pDialog;
    private Bitmap photobmp;
    private boolean fotonueva = false;
    private String nombreFoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_empresa);
        setUI();
    }

    private void setUI() {
        sp = PreferenceManager.getDefaultSharedPreferences(AdmEmpresa.this);
        ID = sp.getString("empresaId", "0");

        //inicializar componentes
        eTitulo = (TextView) findViewById(R.id.admEmpTitulo);
        eNombre = (TextView) findViewById(R.id.admEmpNombre);
        eRazon = (TextView) findViewById(R.id.admEmpRazon);
        eCif = (TextView) findViewById(R.id.admEmpCIF);
        eDireccion = (TextView) findViewById(R.id.admEmpDireccion);
        eLocalidad = (TextView) findViewById(R.id.admEmpLocalidad);
        eProvincia = (TextView) findViewById(R.id.admEmpProvincia);
        eCpostal = (TextView) findViewById(R.id.admEmpCodPostal);
        eTelefono = (TextView) findViewById(R.id.admEmpTelf);
        eEmail = (TextView) findViewById(R.id.admEmpMail);
        eLogo = (ImageView) findViewById(R.id.admEmpLogo);

        bOk = (Button) findViewById(R.id.admEmpBtnOK);
        bCanc = (Button) findViewById(R.id.admEmpBtnCanc);
        bBaja = (Button) findViewById(R.id.admEmpBtnBaja);

        jsonParser = new JSONParser();
        tools = new Herramientas();

        //Click Listeners
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
                finish();
            }
        });
        bCanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darBaja();
                guardarDatos();
                finish();
            }
        });
        eLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Complete la acción usando..."), 1);
            }
        });

        if (!ID.equals("0")) {
            // Relleno datos
            new IntentoRellenarDatos().execute();
        } else {
            // Creo nueva empresa
            bBaja.setVisibility(View.GONE);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            try {
                photobmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                eLogo.setImageBitmap(photobmp);
                fotonueva = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void darBaja() {
        eBaja = !eBaja;
    }

    private void guardarDatos() {
        new GuardarEmpresa().execute();
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

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    class IntentoRellenarDatos extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isFinishing()) {
                pDialog = new ProgressDialog(AdmEmpresa.this);
                pDialog.setMessage("Cargando datos...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app", "1"));
            params.add(new BasicNameValuePair("id_empresa", ID));

            joDatos = jsonParser.peticionHttp(URL, "POST", params);
            return null;
        }

        protected void onPostExecute(String msg) {
            // dismiss the dialog once product deleted
            super.onPostExecute(msg);
            if (msg != null) {
                Toast.makeText(AdmEmpresa.this, msg, Toast.LENGTH_LONG).show();
            }

            JSONArray mEmpresa;

            try {
                mEmpresa = joDatos.getJSONArray("empresas");

                if (mEmpresa.length() == 1) {
                    joDatos = mEmpresa.getJSONObject(0);
                } else {
                    joDatos = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (joDatos != null) {
                    eNombre.setText(joDatos.get(TAG_NOMBRE).toString());
                    eRazon.setText(joDatos.get(TAG_RAZON).toString());
                    eCif.setText(joDatos.get(TAG_CIF).toString());
                    eDireccion.setText(joDatos.get(TAG_DIRECCION).toString());
                    eLocalidad.setText(joDatos.get(TAG_LOCALIDAD).toString());
                    eProvincia.setText(joDatos.get(TAG_PROVINCIA).toString());
                    eCpostal.setText(joDatos.get(TAG_C_POSTAL).toString());
                    eTelefono.setText(joDatos.get(TAG_TELEFONO).toString());
                    eEmail.setText(joDatos.get(TAG_EMAIL).toString());
                    if (!joDatos.get(TAG_BAJA).toString().equalsIgnoreCase("S")) {
                        eBaja = false;
                        bBaja.setText("Dar de baja");
                    } else {
                        eBaja = true;
                        bBaja.setText("Dar de alta");
                        eTitulo.setPaintFlags(eTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    nombreFoto = joDatos.getString(TAG_LOGO);
                    if (nombreFoto != null&& !nombreFoto.equals("null"))
                        new Herramientas.ponerImagen(eLogo).execute(RUTA_GALERIA+nombreFoto);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!isFinishing()) pDialog.dismiss();
        }
    }

    class GuardarEmpresa extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app", "1"));

            if (ID.equals("0")) {     //nueva empresa
                params.add(new BasicNameValuePair("json_accion", "1"));
            } else {                  //actualizar empresa
                params.add(new BasicNameValuePair("json_accion", "2"));
                params.add(new BasicNameValuePair("id", ID));

                String ctBaja = "";
                if (eBaja) ctBaja = "S";
                params.add(new BasicNameValuePair(TAG_BAJA, ctBaja));
            }

            params.add(new BasicNameValuePair(TAG_NOMBRE, eNombre.getText().toString()));
            params.add(new BasicNameValuePair(TAG_RAZON, eRazon.getText().toString()));
            params.add(new BasicNameValuePair(TAG_CIF, eCif.getText().toString()));
            params.add(new BasicNameValuePair(TAG_DIRECCION, eDireccion.getText().toString()));
            params.add(new BasicNameValuePair(TAG_LOCALIDAD, eLocalidad.getText().toString()));
            params.add(new BasicNameValuePair(TAG_PROVINCIA, eProvincia.getText().toString()));
            params.add(new BasicNameValuePair(TAG_C_POSTAL, eCpostal.getText().toString()));
            params.add(new BasicNameValuePair(TAG_TELEFONO, eTelefono.getText().toString()));
            params.add(new BasicNameValuePair(TAG_EMAIL, eEmail.getText().toString()));

            if (fotonueva) {
                params.add(new BasicNameValuePair("foto64", getStringImage(photobmp)));
                params.add(new BasicNameValuePair("foto", eNombre.getText()+".JPEG"));
            }else{
                params.add(new BasicNameValuePair("foto", nombreFoto));
            }

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
                    Log.d("Empresa actualizada!", json.toString());
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
                Toast.makeText(AdmEmpresa.this, file_url, Toast.LENGTH_LONG).show();
            }

            new IntentoRellenarDatos().execute();

        }
    }
}

