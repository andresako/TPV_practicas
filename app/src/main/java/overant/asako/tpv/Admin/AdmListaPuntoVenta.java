package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.PuntoVenta;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.JSONParser;

public class AdmListaPuntoVenta extends Activity {

    private static final String URL = "http://overant.es/puntos_venta.php";

    // Tags de mi JSON php Script;
    private static final String TAG_PUNTOS_VENTA = "puntos_venta";
    private static final String TAG_ID = "id";
    private static final String TAG_ID_EMPRESA = "id_empresa";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_LOCALIDAD = "localidad";
    private static final String TAG_PROVINCIA = "provincia";
    private static final String TAG_C_POSTAL = "codigo_postal";
    private static final String TAG_BAJA = "baja";

    private List<PuntoVenta> listaPuntosVenta;
    private SharedPreferences sp;
    private JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    private TextView tituloLista;
    private ListView lvPuntosVenta;
    private Button bNuevoPunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_lista);
        setUi();
    }

    private void setUi() {
        sp = PreferenceManager.getDefaultSharedPreferences(AdmListaPuntoVenta.this);

        tituloLista = (TextView) findViewById(R.id.AdmListaTitulo);
        bNuevoPunto = (Button) findViewById(R.id.AdmListaBoton);
        lvPuntosVenta = (ListView) findViewById(R.id.admListaView);

        tituloLista.setText("Selecciona Punto de venta");
        bNuevoPunto.setText("Nuevo punto");

        bNuevoPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdmListaPuntoVenta.this, AdmPuntoVenta.class);
                i.putExtra("pVenta", new PuntoVenta());
                startActivity(i);
            }
        });
        lvPuntosVenta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AdmListaPuntoVenta.this, AdmPuntoVenta.class);
                i.putExtra("pVenta", listaPuntosVenta.get(position));
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new rellenarPuntos().execute();
    }

    public class rellenarPuntos extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdmListaPuntoVenta.this);
            pDialog.setMessage("Cargando datos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            actualizarListaPuntos();
            return Boolean.parseBoolean(null);
        }

        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            mostrarListaPuntos();
            pDialog.dismiss();
        }
    }

    private void actualizarListaPuntos() {
        listaPuntosVenta = new ArrayList<>();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app", "1"));
        params.add(new BasicNameValuePair(TAG_ID_EMPRESA, sp.getString("empresaId", "0")));

        JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
        System.out.println(joDatos);
        try {

            JSONArray mPunto = joDatos.getJSONArray(TAG_PUNTOS_VENTA);

            for (int i = 0; i < mPunto.length(); i++) {
                JSONObject c = mPunto.getJSONObject(i);

                PuntoVenta ctPunto = new PuntoVenta(
                        c.getInt(TAG_ID),
                        c.getInt(TAG_ID_EMPRESA),
                        c.getString(TAG_NOMBRE),
                        c.getString(TAG_DIRECCION),
                        c.getString(TAG_LOCALIDAD),
                        c.getString(TAG_PROVINCIA),
                        c.getString(TAG_C_POSTAL));
                if (c.getString(TAG_BAJA).equals("S")) ctPunto.setBaja(true);

                listaPuntosVenta.add(ctPunto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mostrarListaPuntos() {
        List<String> listaCt = new ArrayList<>();
        for (int x = 0; x < listaPuntosVenta.size(); x++) {
            listaCt.add(listaPuntosVenta.get(x).getNombre());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCt);
        lvPuntosVenta.setAdapter(arrayAdapter);
        lvPuntosVenta.setDividerHeight(10);
    }
}
