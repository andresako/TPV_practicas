package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import overant.asako.tpv.Clases.Almacen;
import overant.asako.tpv.Clases.Articulo;
import overant.asako.tpv.Clases.StockPorArticulo;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.AdapterStockPorArticulo;
import overant.asako.tpv.Utils.JSONParser;

public class AdmListaStock extends Activity {

    private static final String URL = "http://overant.es/almacenes_stock.php";
    private static final String URL_GUARDAR = "http://overant.es/json_guardar_stock.php";
    private static final String TAG_ID_ARTICULO = "id_articulos_almacenes";
    private static final String TAG_STOCKS = "articulos_almacenes";

    private static final String TAG_ID = "id";
    private static final String TAG_ID_ALMACEN = "id_almacen";
    private static final String TAG_NOMBRE_ALMACEN = "nombre_almacen";
    private static final String TAG_STOCK_ACTUAL = "stock";
    private static final String TAG_STOCK_MINIMO = "stock_min";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private SharedPreferences sp;
    private Context context;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private ListView lv;
    private TextView titulo;
    private Button btn;
    private List<StockPorArticulo> listaStocks;
    private List<Almacen> listaAlmacenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_lista);

        new rellenarStocks().execute();
    }

    private void setUi() {
        context = this;
        lv = (ListView) findViewById(R.id.admListaView);
        lv.setAdapter(new AdapterStockPorArticulo(listaStocks, this));
        lv.setBackgroundColor(123);
        lv.setDividerHeight(20);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(AdmListaStock.this);
                alert.setTitle("Selecciona los nuevos valores");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText inputStock = new EditText(AdmListaStock.this);
                inputStock.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                inputStock.setHint("Stock");
                inputStock.setSingleLine(true);
                layout.addView(inputStock);

                final EditText inputStockMin = new EditText(AdmListaStock.this);
                inputStockMin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                inputStockMin.setHint("Stock minimo");
                inputStockMin.setSingleLine(true);
                layout.addView(inputStockMin);
                alert.setView(layout);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Aceptado.
                        boolean changed = false;

                        if (!inputStock.getText().toString().equals("")) {
                            listaStocks.get(position).setCantidad(Integer.parseInt(inputStock.getText().toString()));
                            changed = true;
                        }
                        if (!inputStockMin.getText().toString().equals("")) {
                            listaStocks.get(position).setCantidadMinima(Integer.parseInt(inputStockMin.getText().toString()));
                            changed = true;
                        }
                        if (changed) {
                            new GuardarStock().execute(listaStocks.get(position));
                        }
                    }
                });

                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancelado.
                    }
                });
                alert.show();
            }
        });

        titulo = (TextView) findViewById(R.id.AdmListaTitulo);
        titulo.setText("Stock del articulo:");

        btn = (Button) findViewById(R.id.AdmListaBoton);
        btn.setText("Nuevo stock");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(AdmListaStock.this);
                alert.setTitle("Selecciona los valores a añadir");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final Spinner spAlmacen = new Spinner(AdmListaStock.this);
                spAlmacen.setAdapter(new ArrayAdapter<>(AdmListaStock.this, android.R.layout.simple_spinner_item, listaAlmacenes));
                layout.addView(spAlmacen);

                final EditText inputStock = new EditText(AdmListaStock.this);
                inputStock.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                inputStock.setHint("Stock");
                inputStock.setSingleLine(true);
                layout.addView(inputStock);

                final EditText inputStockMin = new EditText(AdmListaStock.this);
                inputStockMin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                inputStockMin.setHint("Stock minimo");
                inputStockMin.setSingleLine(true);
                layout.addView(inputStockMin);
                alert.setView(layout);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Aceptado.
                        StockPorArticulo ctSPA = new StockPorArticulo();
                        ctSPA.setId(0);
                        ctSPA.setArticulo((Articulo) getIntent().getSerializableExtra("articulo"));
                        Almacen ctAlm = (Almacen) spAlmacen.getSelectedItem();
                        ctSPA.setIdAlmacen(ctAlm.getID());

                        int valores = 0;

                        if (!inputStock.getText().toString().equals("")) {
                            ctSPA.setCantidad(Integer.parseInt(inputStock.getText().toString()));
                            valores++;
                        }
                        if (!inputStockMin.getText().toString().equals("")) {
                            ctSPA.setCantidadMinima(Integer.parseInt(inputStockMin.getText().toString()));
                            valores++;
                        }
                        if (valores == 2) {
                            new GuardarStock().execute(ctSPA);
                        }
                    }
                });

                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancelado.
                    }
                });
                alert.show();
            }
        });
    }

    private void rellenarLista() {
        listaStocks = new ArrayList<>();

        Articulo art = (Articulo) getIntent().getSerializableExtra("articulo");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));
        params.add(new BasicNameValuePair(TAG_ID_ARTICULO, art.getID() + ""));

        JSONParser jsonParser = new JSONParser();
        JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
        try {
            JSONArray mStocks = joDatos.getJSONArray(TAG_STOCKS);
            for (int i = 0; i < mStocks.length(); i++) {
                JSONObject c = mStocks.getJSONObject(i);

                StockPorArticulo ctStck = new StockPorArticulo(
                        c.getInt(TAG_ID),
                        c.getInt(TAG_ID_ALMACEN),
                        c.getString(TAG_NOMBRE_ALMACEN),
                        c.getInt(TAG_STOCK_ACTUAL),
                        c.getInt(TAG_STOCK_MINIMO));
                ctStck.setArticulo(art);

                listaStocks.add(ctStck);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sp = PreferenceManager.getDefaultSharedPreferences(AdmListaStock.this);

        listaAlmacenes = new ArrayList<>();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));
        params.add(new BasicNameValuePair("id_empresa", sp.getString("empresaId", "0")));

        joDatos = jsonParser.peticionHttp("http://overant.es/almacenes.php", "POST", params);
        try {
            JSONArray mAlm = joDatos.getJSONArray("almacenes");

            for (int i = 0; i < mAlm.length(); i++) {
                JSONObject c = mAlm.getJSONObject(i);

                Almacen ctAlm = new Almacen();
                ctAlm.setID(c.getInt("id"));
                ctAlm.setNombre(c.getString("nombre"));

                listaAlmacenes.add(ctAlm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void borrarThis(View v) {
        final int pos = (int) v.getTag();
        AlertDialog.Builder alert = new AlertDialog.Builder(AdmListaStock.this);
        alert.setTitle("Desea borrar este stock?");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("Borrar", "" + pos);
                new BorrarStock().execute(listaStocks.get(pos));
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }


    public class rellenarStocks extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdmListaStock.this);
            pDialog.setMessage("Cargando datos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            rellenarLista();
            return Boolean.parseBoolean(null);
        }

        protected void onPostExecute(Boolean resultado) {
            // dismiss the dialog once product deleted
            super.onPostExecute(resultado);
            setUi();
            pDialog.dismiss();
        }
    }

    class GuardarStock extends AsyncTask<StockPorArticulo, Void, String> {

        @Override
        protected String doInBackground(StockPorArticulo... args) {
            // Building Parameters

            StockPorArticulo spa = args[0];

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app", "1"));

            if (spa.getId() == 0) {     //nuevo stock
                params.add(new BasicNameValuePair("json_accion", "1"));
            } else {                                     //actualizar stock
                params.add(new BasicNameValuePair("json_accion", "2"));
                params.add(new BasicNameValuePair(TAG_ID, "" + spa.getId()));
            }

            params.add(new BasicNameValuePair(TAG_ID_ALMACEN, spa.getIdAlmacen() + ""));
            params.add(new BasicNameValuePair(TAG_ID_ARTICULO, spa.getArticulo().getID() + ""));
            params.add(new BasicNameValuePair(TAG_STOCK_ACTUAL, spa.getCantidad() + ""));
            params.add(new BasicNameValuePair(TAG_STOCK_MINIMO, spa.getCantidadMinima() + ""));

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
                    Log.d("Stock actualizado!", json.toString());
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
                Toast.makeText(AdmListaStock.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    class BorrarStock extends AsyncTask<StockPorArticulo, Void, String> {

        @Override
        protected String doInBackground(StockPorArticulo... args) {
            // Building Parameters

            StockPorArticulo spa = args[0];

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app", "1"));
            params.add(new BasicNameValuePair("json_accion", "3"));
            params.add(new BasicNameValuePair(TAG_ID, "" + spa.getId()));

            Log.d("request!", "starting");

            try {
                JSONObject json = jsonParser.peticionHttp(URL_GUARDAR, "POST", params);

                // full json response
                Log.d("Post Comment attempt", json.toString());

                // json success element
                int success;
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Stock actualizado!", json.toString());
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
                Toast.makeText(AdmListaStock.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

}
