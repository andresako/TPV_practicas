package overant.asako.tpv.Utils;


import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import overant.asako.tpv.Clases.Carrito;
import overant.asako.tpv.R;

public class AdaptadorCarrito extends RecyclerView.Adapter<AdaptadorCarrito.ViewHolder> {

    private Carrito carrito;
    private ArrayList<String> listaLineas;

    public AdaptadorCarrito(Carrito carrito){
        this.carrito = carrito;
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
        holder.titulo.setText((carrito.getHashLineas().get(listaLineas.get(position)).art).getNombre());
        holder.precio.setText((carrito.getHashLineas().get(listaLineas.get(position)).art).getPrecio()+ " €");
        holder.cantidad.setText(carrito.getHashLineas().get(listaLineas.get(position)).cantidad+"");
        holder.total.setText(carrito.getHashLineas().get(listaLineas.get(position)).precioTotal+" €");
    }

    @Override
    public int getItemCount() {
        return listaLineas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public ImageView imagen;
        public TextView precio;
        public TextView cantidad;
        public TextView titulo;
        public TextView total;


        public ViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.cArt_foto);
            titulo = (TextView) v.findViewById(R.id.cArt_titulo);
            precio = (TextView) v.findViewById(R.id.cArt_precio);
            cantidad = (TextView) v.findViewById(R.id.cArt_cantidad);
            total = (TextView) v.findViewById(R.id.cArt_total);

        }


    }
}
