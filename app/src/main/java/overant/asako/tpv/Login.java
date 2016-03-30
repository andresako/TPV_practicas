package overant.asako.tpv;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Admin.AdmListaEmpresa;
import overant.asako.tpv.Admin.Administracion;
import overant.asako.tpv.Utils.JSONParser;


public class Login extends Activity implements OnClickListener {

    private EditText user, pass;
    private Button mSubmit;

    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String LOGIN_URL = "http://overant.es/json_login.php";

    // Posibles respuestas del JSON php Script;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_ADMIN = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // declarar componentes e inicializar
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);

        // boton
        mSubmit = (Button) findViewById(R.id.btnLogin);

        // listeners
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                new IntentoLogeo().execute();
                break;

            default:
                break;
        }
    }

    class IntentoLogeo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Logeando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting user:" + username + ", pass:" + password);

                // getting product details by making HTTP request
                JSONObject json = jsonParser.peticionHttp(LOGIN_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    Log.d("Login Successful!", json.toString());

                    // save user data
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Login.this);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.putString("empresaId", json.getString("id_empresa"));

                    // starting intent
                    String admin = json.getString(TAG_ADMIN);
                    Intent i = null;
                    if (admin.equalsIgnoreCase("A")) {
                        i = new Intent(Login.this, Administracion.class);
                        finish();
                    } else if (admin.equalsIgnoreCase("S")) {
                        i = new Intent(Login.this, AdmListaEmpresa.class);
                        finish();
                    }

                    edit.putString("admin", admin);
                    edit.commit();
                    startActivity(i);

                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {
            // dismiss the dialog once product deleted
            super.onPostExecute(message);
        }
    }
}
