package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.Articulo;
import overant.asako.tpv.Clases.Categoria;
import overant.asako.tpv.Clases.TipoIVA;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.Herramientas;
import overant.asako.tpv.Utils.JSONParser;

public class AdmArticulo extends Activity {

    private SharedPreferences sp;

    // Tags de mi JSON php Script;
    private static final String URL = "http://overant.es/articulos.php";
    private static final String URL_GUARDAR = "http://overant.es/json_guardar_articulos.php";

    private static final String TAG_ID = "id";
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_ID_IVA = "id_tipo_iva";
    private static final String TAG_ID_CATEGORIA = "id_familia";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_EAN = "ean";
    private static final String TAG_FOTO = "foto";
    private static final String TAG_PRECIO = "precio";
    private static final String TAG_DESCUENTO = "descuento";
    private static final String TAG_BAJA = "baja";
    private static final String TAG_VALOR = "valor";
    private static final String TAG_CATEGORIAS = "familias";
    private static final String TAG_TIPOS_IVA = "tipos_iva";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private JSONParser jsonParser = new JSONParser();
    private Articulo articulo;
    private List<Categoria> listaCat;
    private List<TipoIVA> listaIva;

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
        iFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdmArticulo.this, AdmListaStock.class);
                i.putExtra("articulo", articulo);
                startActivity(i);
            }
        });

        if (articulo.getID() == 0) btnBaja.setVisibility(View.GONE);

        new rellenarLista().execute();

    }

    private void rellenarListas() {
        listaCat = new ArrayList<>();
        listaIva = new ArrayList<>();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));
        params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));

        JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
        try {
            JSONArray mCat = joDatos.getJSONArray(TAG_CATEGORIAS);

            for (int i = 0; i < mCat.length(); i++) {
                JSONObject c = mCat.getJSONObject(i);

                Categoria ctCat = new Categoria(
                        c.getInt(TAG_ID),
                        c.getInt(TAG_ID_EMPRESA),
                        c.getString(TAG_NOMBRE),
                        c.getString(TAG_FOTO));

                listaCat.add(ctCat);
            }

            JSONArray mIva = joDatos.getJSONArray(TAG_TIPOS_IVA);
            for (int i = 0; i < mIva.length(); i++) {
                JSONObject c = mIva.getJSONObject(i);

                TipoIVA ctIva = new TipoIVA(
                        c.getInt(TAG_ID),
                        c.getString(TAG_NOMBRE),
                        c.getInt(TAG_VALOR));
                listaIva.add(ctIva);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void rellenarDatos() {
        tTitulo.setText("Articulo: " + articulo.getID() + ", " + articulo.getNombre());
        tNombre.setText(articulo.getNombre());
        tEAN.setText(articulo.getEAN());
        tPrecio.setText(articulo.getPrecio()+"");
        tDescuento.setText(articulo.getDescuento()+"");

        tCategoria.setText(articulo.getNombreCat());
        tIva.setText(articulo.getNombreIva());

        if (articulo.isBaja()) {
            btnBaja.setText("Dar de alta");
            tTitulo.setPaintFlags(tTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

//        if (articulo.getFoto() != null) {
//            new Herramientas.ponerImagen(iFoto).execute(articulo.getFoto());
//        }

    }

    private void darBaja() {
        articulo.setBaja(!articulo.isBaja());
    }

    private void guardarDatos() {
        if (sIva.isShown()) {
            articulo.setIdIva(((TipoIVA) sIva.getSelectedItem()).getID());
        }
        if (sCategoria.isShown()) {
            articulo.setIdCategoria(((Categoria) sCategoria.getSelectedItem()).getID());
        }


        new GuardarArticulo().execute();
    }

    public void editThis(View v) {
        final TextView ct = (TextView) v;

        if (ct.getId() != R.id.admArtIvaT && ct.getId() != R.id.admArtCategoriaT) {
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
        } else if (ct.getId() == R.id.admArtIvaT) {
            tIva.setVisibility(View.GONE);
            sIva.setVisibility(View.VISIBLE);
            sIva.performClick();
        } else if (ct.getId() == R.id.admArtCategoriaT) {
            tCategoria.setVisibility(View.GONE);
            sCategoria.setVisibility(View.VISIBLE);
            sCategoria.performClick();
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
            params.add(new BasicNameValuePair(TAG_EAN, tEAN.getText().toString()));
            params.add(new BasicNameValuePair(TAG_FOTO, articulo.getFoto()));
            params.add(new BasicNameValuePair(TAG_PRECIO, tPrecio.getText().toString()));
            params.add(new BasicNameValuePair(TAG_DESCUENTO, tDescuento.getText().toString()));

            params.add(new BasicNameValuePair(TAG_ID_IVA, articulo.getIdIva() + ""));
            params.add(new BasicNameValuePair(TAG_ID_CATEGORIA, articulo.getIdCategoria() + ""));

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

    class rellenarLista extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... args) {
            rellenarListas();
            return Boolean.parseBoolean(null);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            ArrayAdapter<Categoria> adapterCat = new ArrayAdapter<>(AdmArticulo.this, android.R.layout.simple_spinner_item, listaCat);
            adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sCategoria.setAdapter(adapterCat);

            ArrayAdapter<TipoIVA> adapterIva = new ArrayAdapter<>(AdmArticulo.this, android.R.layout.simple_spinner_item, listaIva);
            adapterIva.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sIva.setAdapter(adapterIva);
        }
    }

}
