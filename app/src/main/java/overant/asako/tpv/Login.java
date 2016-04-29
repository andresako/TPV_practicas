package overant.asako.tpv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Admin.AdmListaEmpresa;
import overant.asako.tpv.Admin.Administracion;
import overant.asako.tpv.TPV.ActividadPrincipal;
import overant.asako.tpv.Utils.JSONParser;


public class Login extends Activity implements OnClickListener {

    private static final String LOGIN_URL = "http://overant.es/json_login.php";
    // Posibles respuestas del JSON php Script;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_ADMIN = "admin";
    private static final String TAG_EMPRESA = "nombre_empresa";
    private static final String TAG_LOGO = "logo_empresa";
    private EditText user, pass;
    private Button mSubmit;
    private ProgressDialog pDialog;
    // JSON parser class
    private JSONParser jsonParser = new JSONParser();

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

    class IntentoLogeo extends AsyncTask<String, String, Boolean> {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Login.this);
        Intent i;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isFinishing()) {
                pDialog = new ProgressDialog(Login.this);
                pDialog.setMessage("Logeando...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(String... args) {

            if(isNetworkAvailable()) {
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
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("username", username);
                        edit.putInt("userId", json.getInt("id_user"));
                        edit.putString("empresaId", json.getString("id_empresa"));

                        // starting intent
                        String admin = json.getString(TAG_ADMIN);
                        i = null;
                        if (admin.equalsIgnoreCase("A")) {
                            edit.putString("empresaNombre", json.getString(TAG_EMPRESA));
                            edit.putString("empresaLogo", json.getString(TAG_LOGO));
                            i = new Intent(Login.this, Administracion.class);
                            finish();
                        } else if (admin.equalsIgnoreCase("S")) {
                            i = new Intent(Login.this, AdmListaEmpresa.class);
                            finish();
                        } else {
                            i = new Intent(Login.this, ActividadPrincipal.class);
                            finish();
                        }

                        edit.putString("admin", admin);
                        edit.commit();

                        return true;
                    } else {
                        Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                        return false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        protected void onPostExecute(Boolean msg) {
            if(msg){
                startActivity(i);
            }
            super.onPostExecute(msg);
            pDialog.dismiss();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
