package overant.asako.tpv.TPV;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.Articulo;
import overant.asako.tpv.R;

public class FragmentoCategorias extends Fragment {
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;
    private ActividadPrincipal ap;

    public FragmentoCategorias() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_categorias, container, false);
        ap = ((ActividadPrincipal) getActivity());
        setHasOptionsMenu(true);

        if (savedInstanceState == null) {
            insertarTabs(container);

            viewPager = (ViewPager) view.findViewById(R.id.pager);
            poblarViewPager(viewPager);

            tabLayout.setupWithViewPager(viewPager);
        }

        return view;
    }

    private void insertarTabs(ViewGroup container) {
        View padre = (View) container.getParent();
        appBarLayout = (AppBarLayout) padre.findViewById(R.id.appbar);

        tabLayout = new TabLayout(getActivity());
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        appBarLayout.addView(tabLayout);
    }

    private void poblarViewPager(ViewPager viewPager) {
        AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());

        ArrayList<Integer> cats = ap.datos.getListaIdCat();
        for (int i = 0; i < cats.size(); i++) {
            adapter.addFragment(FragmentoCategoria.nuevaInstancia(cats.get(i)), (ap.datos.getListaArticulosPorCat(cats.get(i)).get(0)).getNombreCat());
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_categorias, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_varios:
                Log.d("menuItem ", "Añadiendo varios");
//                Intent i = new Intent(getContext(), VisualizarTicket.class);
//                startActivityForResult(i, 111);


                addVarios();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addVarios() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ap);
        alert.setTitle("Añadir Varios");

        LinearLayout layout = new LinearLayout(ap.getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputCantidad = new EditText(ap);
        inputCantidad.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        inputCantidad.setHint("Cantidad");
        inputCantidad.setSingleLine(true);
        layout.addView(inputCantidad);

        final EditText inputPrecio = new EditText(ap);
        inputPrecio.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputPrecio.setHint("Precio unitario");
        inputPrecio.setSingleLine(true);
        layout.addView(inputPrecio);
        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Aceptado.
                int todoOk = 0;
                Articulo art = ap.datos.getArticuloId(ap.datos.getVariosID());
                int cantidad = 0;
                double precio = 0;

                if (!inputCantidad.getText().toString().equals("")
                        && Integer.valueOf(inputCantidad.getText().toString()) > 0) {
                    cantidad = Integer.valueOf(inputCantidad.getText().toString());
                    todoOk++;
                }
                if (!inputPrecio.getText().toString().equals("")
                        && Double.valueOf(inputPrecio.getText().toString()) > 0) {
                    precio = Double.valueOf(inputPrecio.getText().toString());
                    todoOk++;
                }
                if (todoOk == 2) {
                    Articulo ar2 = new Articulo(
                            art.getID(),
                            art.getIdEmpresa(),
                            art.getIdCategoria(),
                            art.getIdIva(),
                            art.getNombre(),
                            art.getNombreCat(),
                            art.getNombreIva(),
                            art.getEAN(),
                            art.getFoto(),
                            precio,
                            art.getDescuento()
                    );

                    ap.carrito.addItem(cantidad,ar2);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBarLayout.removeView(tabLayout);
    }

    public class AdaptadorSecciones extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentos = new ArrayList<>();
        private final List<String> titulosFragmentos = new ArrayList<>();

        public AdaptadorSecciones(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentos.get(position);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentos.add(fragment);
            titulosFragmentos.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulosFragmentos.get(position);
        }
    }
}
