package overant.asako.tpv.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.Carrito;
import overant.asako.tpv.Clases.Linea;
import overant.asako.tpv.R;
import overant.asako.tpv.TPV.ActividadPrincipal;

public class AdaptadorCarrito extends RecyclerView.Adapter<AdaptadorCarrito.ViewHolder> implements View.OnClickListener {

    private final ActividadPrincipal pa;
    private Carrito carrito;
    private ArrayList<String> listaLineas;
    private TextView total;

    public AdaptadorCarrito(TextView total, ActividadPrincipal pa) {
        this.carrito = pa.carrito;
        this.pa = pa;
        this.total = total;
        listaLineas = new ArrayList<>(carrito.getHashLineas().keySet());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_carrito, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(this);
        holder.titulo.setText((carrito.getHashLineas().get(listaLineas.get(position)).getArt()).getNombre());
        holder.precio.setText((carrito.getHashLineas().get(listaLineas.get(position)).getArt()).getPrecio() + " €");
        holder.cantidad.setText(carrito.getHashLineas().get(listaLineas.get(position)).getCantidad() + "");
        holder.total.setText(carrito.getHashLineas().get(listaLineas.get(position)).getPrecioTotal() + " €");
    }

    @Override
    public int getItemCount() {
        return listaLineas.size();
    }

    @Override
    public void onClick(View v) {

        if (!pa.carrito.isCerrado()) {
            final int pos = (int) v.getTag();

            PopupMenu popup = new PopupMenu(pa, v);
            popup.getMenuInflater().inflate(R.menu.popup_menu_carro, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.item_carro_editar:
                            editarItem(pos);
                            break;
                        case R.id.item_carro_borrar:
                            carrito.delItem(listaLineas.get(pos));
                            listaLineas.remove(pos);
                            pa.refreshCarro();
                            total.setText(carrito.getTotal() + " €");
                            notifyDataSetChanged();
                            break;
                    }
                    return false;
                }
            });
            popup.show();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(pa);
            alert.setTitle("ATENCION!\nEl carro está cerrado");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }
            );
            alert.show();
        }
    }

    private void editarItem(final int pos) {

        Context cntx = pa.getApplication();

        AlertDialog.Builder alert = new AlertDialog.Builder(pa);
        alert.setTitle("Selecciona el nuevo valor");

        LinearLayout layout = new LinearLayout(cntx);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText newValue = new EditText(cntx);
        newValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        newValue.setHint(carrito.getHashLineas().get(listaLineas.get(pos)).getCantidad() + "");
        newValue.setSingleLine(true);
        newValue.setHintTextColor(Color.LTGRAY);
        newValue.setTextColor(Color.RED);
        layout.addView(newValue);
        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Aceptado.
                if (!newValue.getText().toString().equals("")) {

                    Linea linea = carrito.getHashLineas().get(listaLineas.get(pos));
                    int cantidad = Integer.valueOf(newValue.getText().toString());

                    carrito.modItem(linea, cantidad);

                    new modLinea().execute(new Integer[]{linea.getID(), cantidad});

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView precio;
        public TextView cantidad;
        public TextView titulo;
        public TextView total;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v;
            titulo = (TextView) v.findViewById(R.id.cArt_titulo);
            precio = (TextView) v.findViewById(R.id.cArt_precio);
            cantidad = (TextView) v.findViewById(R.id.cArt_cantidad);
            total = (TextView) v.findViewById(R.id.cArt_total);
        }
    }

    private class modLinea extends AsyncTask<Integer[], Void, Boolean> {

        JSONParser jsonParser = new JSONParser();
        String URL = "http://overant.es/TPV_java.php";

        @Override
        protected Boolean doInBackground(Integer[]... args) {
            Integer[] array = args[0];

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "5"));
            params.add(new BasicNameValuePair("lineaId", array[0] + ""));
            params.add(new BasicNameValuePair("cantidad", array[1] + ""));

            JSONObject joDatos = jsonParser.peticionHttp(URL, "POST", params);
            try {
                int result = joDatos.getInt("Res");
                if (result == 1) {
                    carrito.setTotal(joDatos.getDouble("Total"));
                } else {
                    return false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            if (msg) {
                notifyDataSetChanged();
                total.setText(carrito.getTotal() + " €");
                pa.refreshCarro();
            }

            super.onPostExecute(msg);
        }
    }
}
