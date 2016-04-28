package overant.asako.tpv.Utils;

import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import overant.asako.tpv.Clases.Carrito;
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
        holder.titulo.setText((carrito.getHashLineas().get(listaLineas.get(position)).art).getNombre());
        holder.precio.setText((carrito.getHashLineas().get(listaLineas.get(position)).art).getPrecio() + " €");
        holder.cantidad.setText(carrito.getHashLineas().get(listaLineas.get(position)).cantidad + "");
        holder.total.setText(carrito.getHashLineas().get(listaLineas.get(position)).precioTotal + " €");
    }

    @Override
    public int getItemCount() {
        return listaLineas.size();
    }

    @Override
    public void onClick(View v) {
        final int pos = (int) v.getTag();

        PopupMenu popup = new PopupMenu(pa, v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_carro_editar:

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
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imagen;
        public TextView precio;
        public TextView cantidad;
        public TextView titulo;
        public TextView total;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView)v;
            imagen = (ImageView) v.findViewById(R.id.cArt_foto);
            titulo = (TextView) v.findViewById(R.id.cArt_titulo);
            precio = (TextView) v.findViewById(R.id.cArt_precio);
            cantidad = (TextView) v.findViewById(R.id.cArt_cantidad);
            total = (TextView) v.findViewById(R.id.cArt_total);
        }
    }
}
