package overant.asako.tpv.TPV;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import overant.asako.tpv.Clases.Articulo;
import overant.asako.tpv.Clases.Carrito;
import overant.asako.tpv.Clases.Linea;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.Datos;
import overant.asako.tpv.Utils.JSONParser;

public class ActividadPrincipal extends AppCompatActivity {

    public Datos datos;
    public Carrito carrito;
    public int empresaId;
    private DrawerLayout drawerLayout;
    private TextView totalCarrito;
    private SharedPreferences sp;
    private JSONParser jsonParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        sp = PreferenceManager.getDefaultSharedPreferences(ActividadPrincipal.this);
        jsonParser = new JSONParser();

        empresaId = sp.getInt("empresaId", 0);
        datos = Datos.getInstance(empresaId);
        //carrito = new Carrito(sp.getInt("empresaId", 0), sp.getInt("userId", 0));

        agregarToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(navigationView.getMenu().size()-1));

            View header = navigationView.getHeaderView(0);
            totalCarrito = (TextView) header.findViewById(R.id.texto_total_carrito);
        }
        new recuperarCarrito().execute();
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.drawer_toggle);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }

    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) {
//            case R.id.item_inicio:
//                //fragmentoGenerico = new FragmentoInicio();
//                break;
            case R.id.item_categorias:
                fragmentoGenerico = new FragmentoCategorias();
                break;
            case R.id.item_carrito:
                fragmentoGenerico = new FragmentoCarrito();
                break;
            case R.id.item_tickets:
                fragmentoGenerico = new FragmentoTickets();
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragmentoGenerico)
                    .commit();
        }

        // Setear título actual
        setTitle(itemDrawer.getTitle());
    }

    public void refreshCarro() {
        totalCarrito.setText(carrito.getTotal() + " €");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ActividadPrincipal.this);
        alert.setTitle("Quiere salir de la apicación?");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                }
        );
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Do nothing
                    }
                }
        );
        alert.show();
    }

    class recuperarCarrito extends AsyncTask<Void, Void, Boolean> {

        String URL = "http://overant.es/TPV_java.php";

        @Override
        protected Boolean doInBackground(Void... args) {

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "8"));
            params.add(new BasicNameValuePair("empresaId", empresaId + ""));
            JSONObject json = jsonParser.peticionHttp(URL, "POST", params);

            try {
                int resp = json.getInt("Res");

                if (resp == 1) {

                    HashMap<String, Linea> listaLineas = new HashMap<>();

                    carrito = new Carrito(
                            json.getInt("id"),
                            json.getInt("empresa"),
                            json.getInt("user"),
                            json.getInt("numero"),
                            json.getString("fecha"),
                            json.getDouble("total"));

                    JSONArray jsonArray = json.getJSONArray("ticket");
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject c = jsonArray.getJSONObject(x);

                        Articulo ar = datos.getArticuloId(c.getInt("articulo"));
                        Articulo ar2 = new Articulo(
                                ar.getID(),
                                ar.getIdEmpresa(),
                                ar.getIdCategoria(),
                                ar.getIdIva(),
                                ar.getNombre(),
                                ar.getNombreCat(),
                                ar.getNombreIva(),
                                ar.getEAN(),
                                ar.getFoto(),
                                c.getDouble("precio"),
                                ar.getDescuento()
                        );

                        if (c.getInt("articulo") != datos.getVariosID()) {
                            listaLineas.put(datos.getArticuloId(c.getInt("articulo")).getNombre(),
                                    new Linea(c.getInt("id"), c.getInt("cantidad"), ar));
                        } else {
                            listaLineas.put(ar.getNombre() + c.getInt("id"),
                                    new Linea(c.getInt("id"), c.getInt("cantidad"), ar2));
                        }
                    }
                    carrito.setListaLineas(listaLineas);
                } else
                    carrito = new Carrito(sp.getInt("empresaId", 0), sp.getInt("userId", 0));

                carrito.setMain(ActividadPrincipal.this);

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            refreshCarro();
            super.onPostExecute(aBoolean);
        }
    }
}
