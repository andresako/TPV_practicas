package overant.asako.tpv.TPV;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import overant.asako.tpv.R;
import overant.asako.tpv.Utils.AdaptadorCarrito;

public class FragmentoCarrito extends Fragment {

    private ActividadPrincipal pa;
    private LinearLayoutManager linearLayout;
    private TextView total;

    public FragmentoCarrito() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_carrito, container, false);
        setHasOptionsMenu(true);

        pa = (ActividadPrincipal) getActivity();
        RecyclerView reciclador = (RecyclerView) v.findViewById(R.id.reciclador);
        linearLayout = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(linearLayout);

        total = (TextView) v.findViewById(R.id.car_total);
        total.setText(pa.carrito.getTotal() + " €");

        AdaptadorCarrito adapter = new AdaptadorCarrito(total, pa);
        reciclador.setAdapter(adapter);


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_carrito, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_carro_borrar:

                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
