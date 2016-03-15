package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    private static final String URL = "http://overant.es/empresas.php";
    private static final String TAG_EMPRESAS = "empresas";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_ID = "id_empresa";

    private JSONArray joLista;
    private ArrayList<HashMap<String, String>> mEmpresasList;
    private JSONObject joDatos;
    private JSONParser jsonParser = new JSONParser();

    private ListView listaEmpresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_lista_empresa);
        setUi();

    }

    private void setUi() {
        listaEmpresas = (ListView) findViewById(R.id.admListaEmpresas);
        mEmpresasList = new ArrayList<>();
        listaEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // save user data
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AdmListaEmpresa.this);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("empresaId", mEmpresasList.get(position).get(TAG_ID));
                edit.commit();

                Intent i = new Intent(AdmListaEmpresa.this, Administracion.class);
                startActivity(i);

            }
        });
        new MostrarListaEmpresas().execute();

    }

    public class MostrarListaEmpresas extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_NOMBRE, c.getString(TAG_NOMBRE));
                    map.put(TAG_ID, c.get(TAG_ID).toString());

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
        List<String> listaCt = new ArrayList<String>();
        for (int x = 0; x < mEmpresasList.size(); x++) {
            listaCt.add(mEmpresasList.get(x).get(TAG_NOMBRE));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaCt);

        listaEmpresas.setAdapter(arrayAdapter);
    }


}
