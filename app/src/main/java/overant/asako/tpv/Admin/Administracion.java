package overant.asako.tpv.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import overant.asako.tpv.R;
import overant.asako.tpv.Utils.Herramientas;
import overant.asako.tpv.Utils.JSONParser;

public class Administracion extends Activity implements View.OnClickListener {

    private static final String RUTA_GALERIA = "http://overant.es/galeria/";
    private LinearLayout llArtExt;
    private HashMap<Integer, String> listaPuntosVenta;
    private SharedPreferences sp;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion);
        sp = PreferenceManager.getDefaultSharedPreferences(Administracion.this);

        //rellenar lista
        new RellenarListaPuntos().execute();

        // setear botones
        Button mEmpresa = (Button) findViewById(R.id.btnAdmEmpresa);
        Button mPuntVenta = (Button) findViewById(R.id.btnAdmPuntoVenta);
        Button mArticulo = (Button) findViewById(R.id.btnAdmArticulo);
        Button mCliente = (Button) findViewById(R.id.btnAdmCliente);
        Button mAlmacen = (Button) findViewById(R.id.btnAdmAlmacen);
        Button mProveedor = (Button) findViewById(R.id.btnAdmProveedor);
        Button mArticuloCat = (Button) findViewById(R.id.btnAdmArticuloCat);
        Button mArticuloLista = (Button) findViewById(R.id.btnAdmArticuloLista);
        Button mUsuarios = (Button) findViewById(R.id.btnAdmUsuario);

        mEmpresa.setOnClickListener(this);
        mPuntVenta.setOnClickListener(this);
        mArticulo.setOnClickListener(this);
        mCliente.setOnClickListener(this);
        mAlmacen.setOnClickListener(this);
        mProveedor.setOnClickListener(this);
        mArticuloCat.setOnClickListener(this);
        mArticuloLista.setOnClickListener(this);
        mUsuarios.setOnClickListener(this);

        TextView titulo = (TextView) findViewById(R.id.tituloEmpresa);
        titulo.setText(sp.getString("empresaNombre", "Empresa"));

        ImageView img = (ImageView) findViewById(R.id.imgTituloEmpresa);
        new Herramientas.ponerImagen(img).execute(RUTA_GALERIA + sp.getString("empresaLogo", ""));
        llArtExt = (LinearLayout) findViewById(R.id.admArticuloExt);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnAdmEmpresa:
                Intent admEmp = new Intent(Administracion.this, AdmEmpresa.class);
                admEmp.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admEmp);
                break;

            case R.id.btnAdmPuntoVenta:
                Intent admPVenta = new Intent(Administracion.this, AdmListaPuntoVenta.class);
                admPVenta.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admPVenta);
                break;

            case R.id.btnAdmArticulo:
                if (llArtExt.isShown()) llArtExt.setVisibility(View.GONE);
                else llArtExt.setVisibility(View.VISIBLE);
                break;

            case R.id.btnAdmCliente:
                Intent admCli = new Intent(Administracion.this, AdmListaClientes.class);
                admCli.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admCli);
                break;

            case R.id.btnAdmAlmacen:
                Intent admAlm = new Intent(Administracion.this, AdmListaAlmacenes.class);
                admAlm.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admAlm);
                break;

            case R.id.btnAdmProveedor:
                Intent admPro = new Intent(Administracion.this, AdmListaProveedores.class);
                admPro.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admPro);
                break;

            case R.id.btnAdmArticuloLista:
                Intent admArtLis = new Intent(Administracion.this, AdmListaArticulos.class);
                admArtLis.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admArtLis);
                break;

            case R.id.btnAdmArticuloCat:
                Intent admArtLisCat = new Intent(Administracion.this, AdmListaCategorias.class);
                admArtLisCat.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admArtLisCat);
                break;

            case R.id.btnAdmUsuario:

                alert.show();
                break;

            default:
                break;
        }
    }

    private class RellenarListaPuntos extends AsyncTask<Void, Void, Void> {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Administracion.this);

        @Override
        protected Void doInBackground(Void... args) {

            listaPuntosVenta = new HashMap<>();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("app", "1"));
            params.add(new BasicNameValuePair("id_empresa", sp.getString("empresaId", "0")));

            JSONParser jsonParser = new JSONParser();
            JSONObject joDatos = jsonParser.peticionHttp("http://overant.es/puntos_venta.php", "POST", params);
            try {

                JSONArray mPunto = joDatos.getJSONArray("puntos_venta");

                for (int i = 0; i < mPunto.length(); i++) {
                    JSONObject c = mPunto.getJSONObject(i);

                    listaPuntosVenta.put(c.getInt("id"), c.getString("nombre"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Administracion.this);
            builder.setTitle("Seleccione punto de venta");
            ListView lv = new ListView(Administracion.this);
            lv.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    new ArrayList<>(listaPuntosVenta.values())));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int idPunto = new ArrayList<>(listaPuntosVenta.keySet()).get(position);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("puntoId", "" + idPunto);
                    edit.apply();

                    alert.dismiss();

                    Intent i = new Intent(Administracion.this, AdmListaUsuarios.class);
                    startActivity(i);
                }
            });
            builder.setView(lv);
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Do nothing
                        }
                    }
            );
            alert = builder.create();

            super.onPostExecute(aVoid);

        }
    }
}
