package overant.asako.tpv.TPV;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import overant.asako.tpv.R;
import overant.asako.tpv.Utils.AdaptadorCarrito;

public class FragmentoCarrito extends Fragment {

    private ActividadPrincipal pa;
    private LinearLayoutManager linearLayout;
    private TextView total;
    private RecyclerView reciclador;

    public FragmentoCarrito() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_carrito, container, false);
        setHasOptionsMenu(true);

        pa = (ActividadPrincipal) getActivity();
        reciclador = (RecyclerView) v.findViewById(R.id.reciclador);
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

        AlertDialog.Builder alert;

        switch (item.getItemId()) {
            case R.id.accion_carro_borrar:
                Log.d("Carrito ", "Borrar carro");

                alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("ATENCION!\nEl carrito se vaciará");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                pa.carrito.vaciarTicket();
                                total.setText(pa.carrito.getTotal() + " €");
                                pa.refreshCarro();
                                AdaptadorCarrito adapter = new AdaptadorCarrito(total, pa);
                                reciclador.setAdapter(adapter);
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
                return true;

            case R.id.accion_carro_cerrar:
                Log.d("Carrito ", "Cerrar carro");
                Snackbar.make(getView()," Cerrando Ticket.... NO IMPLEMENTADO", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                return true;
            case R.id.accion_carro_nuevo:
                Log.d("Carrito ", "Nuevo carro");
                Snackbar.make(getView(),"Creando nuevo Ticket.... NO IMPLEMENTADO", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                return true;
            case R.id.accion_carro_detalles:
                alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Detalles del carrito");

                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);

                TextView detNum = new TextView(pa);
                detNum.setText("Numero: " + pa.carrito.getNumero());

                TextView detEmp = new TextView(pa);
                detEmp.setText("Empresa: " + pa.carrito.getID_empresa());

                TextView detUser = new TextView(pa);
                detUser.setText("Usuario: " + pa.carrito.getID_usuario());

                TextView detFecha = new TextView(pa);
                detFecha.setText("Fecha: " + pa.carrito.getFecha());

                layout.addView(detNum);
                layout.addView(detEmp);
                layout.addView(detUser);
                layout.addView(detFecha);

                alert.setView(layout);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Do nothing
                            }
                        }
                );

                alert.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
