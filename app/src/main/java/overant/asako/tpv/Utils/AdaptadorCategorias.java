package overant.asako.tpv.Utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import overant.asako.tpv.Clases.Articulo;
import overant.asako.tpv.R;
import overant.asako.tpv.TPV.ActividadPrincipal;


public class AdaptadorCategorias extends RecyclerView.Adapter<AdaptadorCategorias.ViewHolder> {

    private static final String RUTA_GALERIA = "http://overant.es/galeria/";

    private List<Articulo> items;
    private SharedPreferences sp;
    private ActividadPrincipal ap;

    public AdaptadorCategorias(List<Articulo> items, ActividadPrincipal context) {
        this.items = items;
        this.ap = context;
        sp = PreferenceManager.getDefaultSharedPreferences(ap);
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
                if (!ap.carrito.isCerrado()) {
                    ap.carrito.addItem(1, items.get(vh.getAdapterPosition()));
                    //ap.refreshCarro();
                    Snackbar.make(v, items.get(vh.getAdapterPosition()).getNombre() + " añadido al carro", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .show();
                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(ap);
                    alert.setTitle("ATENCION!\nEl carro está cerrado\nSeleccione uno abierto");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            }
                    );
                    alert.show();
                }
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Articulo item = items.get(i);
        final String urlFoto = RUTA_GALERIA + item.getFoto();

        final Bitmap[] theBM = {null};
        viewHolder.nombre.setText(item.getNombre());
        viewHolder.precio.setText(item.getPrecio() + " €");
        viewHolder.artId = item.getID();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    theBM[0] = Glide.
                            with(ap).
                            load(urlFoto).
                            asBitmap().
                            into(100,100).
                            get();
                } catch (final ExecutionException | InterruptedException e) {
                    Log.e("FOTO!?", e.getMessage()+ " " + urlFoto);
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void dummy) {
                if (null != theBM[0]) {
                    // The full bitmap should be available here
                    viewHolder.imagen.setImageBitmap(theBM[0]);
                    Log.d("FOTO!?", "Image loaded " + urlFoto);
                }else{
                    viewHolder.imagen.setImageResource(R.drawable.no_image);
                }
            }
        }.execute();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView precio;
        public ImageView imagen;
        public ImageButton boton;
        public int artId;

        public ViewHolder(View v) {
            super(v);

            nombre = (TextView) v.findViewById(R.id.nombre_articulo);
            precio = (TextView) v.findViewById(R.id.precio_articulo);
            imagen = (ImageView) v.findViewById(R.id.miniatura_articulo);
            boton = (ImageButton) v.findViewById(R.id.addCarro);
        }
    }

}