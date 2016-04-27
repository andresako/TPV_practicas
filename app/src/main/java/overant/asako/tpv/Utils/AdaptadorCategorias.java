package overant.asako.tpv.Utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import overant.asako.tpv.Clases.Articulo;
import overant.asako.tpv.R;
import overant.asako.tpv.TPV.ActividadPrincipal;

public class AdaptadorCategorias extends RecyclerView.Adapter<AdaptadorCategorias.ViewHolder> {


    private final List<Articulo> items;
    private ActividadPrincipal ap;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView precio;
        public ImageView imagen;
        public ImageButton boton;
        public int artId;

        public ViewHolder(View v) {
            super(v);

            nombre = (TextView) v.findViewById(R.id.nombre_comida);
            precio = (TextView) v.findViewById(R.id.precio_comida);
            imagen = (ImageView) v.findViewById(R.id.miniatura_comida);
            boton = (ImageButton) v.findViewById(R.id.addCarro);
        }
    }


    public AdaptadorCategorias(List<Articulo> items, ActividadPrincipal context) {
        this.items = items;
        this.ap = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lista_categorias, viewGroup, false);
        final ViewHolder vh = new ViewHolder(v);
        vh.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("boton", "Articulo " + items.get(vh.getAdapterPosition()).getNombre());
                ap.carrito.addItem(1, items.get(vh.getAdapterPosition()));
                ap.refreshCarro();
                Snackbar.make(v, items.get(vh.getAdapterPosition()).getNombre() + " añadido al carro", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Articulo item = items.get(i);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getDrawable())
                .centerCrop()
                .into(viewHolder.imagen);
        viewHolder.nombre.setText(item.getNombre());
        viewHolder.precio.setText(item.getPrecio() + " €");
        viewHolder.artId = item.getID();
    }
}