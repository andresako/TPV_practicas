package overant.asako.tpv.Utils;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import overant.asako.tpv.Clases.Linea;
import overant.asako.tpv.R;

public class AdaptadorTicket extends RecyclerView.Adapter<AdaptadorTicket.ViewHolder>{

    private final ArrayList<Linea> listaLineas;

    public AdaptadorTicket(ArrayList<Linea> listaLineas){
        this.listaLineas = listaLineas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_detalle, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Linea ln = listaLineas.get(position);

        holder.numero.setText(ln.getCantidad()+"");
        holder.nombre.setText(ln.getArt().getNombre());
        holder.precio.setText(ln.getArt().getPrecio() + " €..");
        holder.total.setText(ln.getPrecioTotal()+" €");
    }

    @Override
    public int getItemCount() {
        return listaLineas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView numero;
        public TextView nombre;
        public TextView precio;
        public TextView total;

        public ViewHolder(View v) {
            super(v);
            numero = (TextView) v.findViewById(R.id.item_detalle_numero);
            nombre = (TextView) v.findViewById(R.id.item_detalle_nombre);
            precio = (TextView) v.findViewById(R.id.item_detalle_precio);
            total = (TextView) v.findViewById(R.id.item_detalle_total);
        }
    }
}
