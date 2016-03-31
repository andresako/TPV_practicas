package overant.asako.tpv.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import overant.asako.tpv.Admin.AdmListaStock;
import overant.asako.tpv.Clases.StockPorArticulo;
import overant.asako.tpv.R;

public class AdapterStockPorArticulo extends BaseAdapter {

    private List<StockPorArticulo> listaStock;
    private static LayoutInflater inflater = null;
    private Context context;

    public AdapterStockPorArticulo(List<StockPorArticulo> listaStock, Activity context) {
        this.listaStock = listaStock;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listaStock.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tvAlmacen;
        TextView tvStock;
        TextView tvStockMinimo;
        Button btnBorrar;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;

        if (convertView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.item_lista_stock, null);
        } else {
            rowView = convertView;
        }
        holder.tvAlmacen = (TextView) rowView.findViewById(R.id.ilsAlmacen);
        holder.tvStock = (TextView) rowView.findViewById(R.id.ilsStock);
        holder.tvStockMinimo = (TextView) rowView.findViewById(R.id.ilsStockMin);
        holder.btnBorrar = (Button) rowView.findViewById(R.id.ilsBorrar);

        holder.tvAlmacen.setText(String.valueOf(listaStock.get(position).getNomAlmacen()));
        holder.tvStock.setText(String.valueOf(listaStock.get(position).getCantidad()));
        holder.tvStockMinimo.setText(String.valueOf(listaStock.get(position).getCantidadMinima()));
        holder.btnBorrar.setTag(position);

        return rowView;
    }

}