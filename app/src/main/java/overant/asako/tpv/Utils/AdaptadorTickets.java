package overant.asako.tpv.Utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import overant.asako.tpv.TPV.ActividadPrincipal;
import overant.asako.tpv.TPV.VisualizarTicket;

public class AdaptadorTickets extends RecyclerView.Adapter<AdaptadorTickets.ViewHolder> {

    private final ArrayList<Carrito> lista;
    private ActividadPrincipal pa;

    public AdaptadorTickets(ActividadPrincipal pa, ArrayList<Carrito> lista) {
        this.pa = pa;
        this.lista = lista;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_tickets, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Carrito cr = lista.get(position);

        holder.numero.setText(cr.getNumero() + "");
        holder.fecha.setText(cr.getFecha());
        holder.total.setText(cr.getTotal() + " €");
        if (cr.isCerrado()) holder.candado.setImageResource(R.drawable.candado_lock);

        holder.caja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TICKETS", "Seleccionado ticket: " + cr.getNumero());

                PopupMenu popup = new PopupMenu(pa, v);
                popup.getMenuInflater().inflate(R.menu.popup_menu_ticket, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.accion_ticket_seleccion:
                                    cr.setMain(pa);
                                    new seleccionarCarro().execute(position);
                                break;
                            case R.id.accion_ticket_ver:
                                Intent i = new Intent(pa, VisualizarTicket.class);
                                i.putExtra("carro",cr);
                                i.putExtra("lista",new ArrayList<>(pa.carrito.getHashLineas().values()));
                                pa.startActivity(i);
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView caja;
        public TextView numero;
        public TextView fecha;
        public TextView total;
        public ImageView candado;

        public ViewHolder(View v) {
            super(v);
            caja = (CardView) v.findViewById(R.id.item_ticket_contenedor);
            numero = (TextView) v.findViewById(R.id.item_ticket_titulo);
            fecha = (TextView) v.findViewById(R.id.item_ticket_fecha);
            total = (TextView) v.findViewById(R.id.item_ticket_total);
            candado = (ImageView) v.findViewById(R.id.item_ticket_candado);
        }
    }

    private class seleccionarCarro extends AsyncTask<Integer, Void, Boolean> {

        private JSONParser jsonParser = new JSONParser();
        private HashMap<String, Linea> listaLineas = new HashMap<>();
        private String URL = "http://overant.es/TPV_java.php";

        @Override
        protected Boolean doInBackground(Integer... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "2"));
            params.add(new BasicNameValuePair("ticketId", lista.get(args[0]).getID() + ""));
            JSONObject json = jsonParser.peticionHttp(URL, "POST", params);

            int resp = 0;

            try {
                resp = json.getInt("Res");

                if (resp == 1) {
                    JSONArray jsonArray = json.getJSONArray("ticket");
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject c = jsonArray.getJSONObject(x);
                        Articulo ar = pa.datos.getArticuloId(c.getInt("articulo"));
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

                        Linea ln = new Linea(
                                c.getInt("id"),
                                c.getInt("cantidad"),
                                ar2
                        );
                        if (ar2.getNombre().equalsIgnoreCase("Varios")) {
                            ln.setPrecio(c.getDouble("precio"));
                            listaLineas.put(ar2.getNombre() + ln.getID(), ln);
                        } else {
                            listaLineas.put(ar2.getNombre(), ln);
                        }
                    }

                    pa.carrito = lista.get(args[0]);
                    pa.carrito.setListaLineas(listaLineas);

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
            super.onPostExecute(msg);


            pa.refreshCarro();
        }
    }
}
