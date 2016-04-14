package overant.asako.tpv.Admin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import overant.asako.tpv.R;
import overant.asako.tpv.Utils.JSONParser;

public class AdmListaEmpresa extends Activity {

    private SharedPreferences sp;

    private static final String URL = "http://overant.es/empresas.php";
    private static final String TAG_EMPRESAS = "empresas";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_ID = "id_empresa";
    private static final String TAG_LOGO = "logo";

    private JSONArray joLista;
    private ArrayList<HashMap<String, String>> mEmpresasList;
    private JSONObject joDatos;
    private JSONParser jsonParser = new JSONParser();

    private TextView tituloLista;
    private ListView listaEmpresas;
    private Button bNuevaEmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_lista);
        setUi();
    }

    private void setUi() {
        sp = PreferenceManager.getDefaultSharedPreferences(AdmListaEmpresa.this);
        tituloLista = (TextView) findViewById(R.id.AdmListaTitulo);
        listaEmpresas = (ListView) findViewById(R.id.admListaView);
        bNuevaEmp = (Button) findViewById(R.id.AdmListaBoton);

        tituloLista.setText("Selecciona Empresa a editar.");
        bNuevaEmp.setText("Nueva empresa");

        listaEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // save user data
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("empresaId", mEmpresasList.get(position).get(TAG_ID));
                edit.putString("empresaNombre",mEmpresasList.get(position).get(TAG_NOMBRE));
                edit.putString("empresaLogo",mEmpresasList.get(position).get(TAG_LOGO));
                edit.apply();

                Intent i = new Intent(AdmListaEmpresa.this, Administracion.class);
                startActivity(i);
            }
        });

        bNuevaEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save user data
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("empresaId", "0");
                edit.putString("empresaNombre","");
                edit.putString("empresaLogo","");
                edit.apply();

                Intent i = new Intent(AdmListaEmpresa.this, AdmEmpresa.class);
                startActivity(i);
            }
        });


    }

    public class MostrarListaEmpresas extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app", "1"));
            params.add(new BasicNameValuePair("admin", "S"));
            Log.d("request!", "starting");

            try {
                joDatos = jsonParser.peticionHttp(URL, "POST", params);
                joLista = joDatos.getJSONArray(TAG_EMPRESAS);
                for (int i = 0; i < joLista.length(); i++) {
                    JSONObject c = joLista.getJSONObject(i);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<>();
                    map.put(TAG_NOMBRE, c.getString(TAG_NOMBRE));
                    map.put(TAG_ID, c.get(TAG_ID).toString());
                    map.put((TAG_LOGO), c.getString(TAG_LOGO));

                    // adding HashList to ArrayList
                    mEmpresasList.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            if (file_url != null) {
                Toast.makeText(AdmListaEmpresa.this, file_url, Toast.LENGTH_LONG).show();
            }
            pintarLista();
        }

    }

    private void pintarLista() {
        List<String> listaCt = new ArrayList<>();
        for (int x = 0; x < mEmpresasList.size(); x++) {
            listaCt.add(mEmpresasList.get(x).get(TAG_NOMBRE));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCt);

        listaEmpresas.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEmpresasList = new ArrayList<>();
        new MostrarListaEmpresas().execute();
    }
}
