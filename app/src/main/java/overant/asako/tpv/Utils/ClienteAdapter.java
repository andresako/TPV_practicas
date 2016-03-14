package overant.asako.tpv.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import overant.asako.tpv.Admin.AdmClientes;
import overant.asako.tpv.Clases.Cliente;
import overant.asako.tpv.R;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {

    private List<Cliente> items;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int pos;
        public TextView cTitulo, cNombre, cApellido, cDNI, cDirecc, cLocal, cProv, cCpostal, cTelf, cMail;
        public ImageButton cExpand, cContraer, cSave;
        public LinearLayout llBot;

        public ViewHolder(View v) {
            super(v);
            cTitulo = (TextView) v.findViewById(R.id.cardTitulo);
            cNombre = (TextView) v.findViewById(R.id.cardNombre);
            cApellido = (TextView) v.findViewById(R.id.cardApellidos);
            cDNI = (TextView) v.findViewById(R.id.cardDNI);
            cDirecc = (TextView) v.findViewById(R.id.cardDireccion);
            cLocal = (TextView) v.findViewById(R.id.cardLocalidad);
            cProv = (TextView) v.findViewById(R.id.cardProvincia);
            cCpostal = (TextView) v.findViewById(R.id.cardCPostal);
            cTelf = (TextView) v.findViewById(R.id.cardTelefono);
            cMail = (TextView) v.findViewById(R.id.cardEmail);

            llBot = (LinearLayout) v.findViewById(R.id.cardLayoutBot);

            cExpand = (ImageButton) v.findViewById(R.id.cardExpandir);
            cContraer = (ImageButton) v.findViewById(R.id.cardContraer);
            cSave = (ImageButton) v.findViewById(R.id.cardGuardar);

            cExpand.setOnClickListener(this);
            cContraer.setOnClickListener(this);
            cSave.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.cardExpandir:
                    llBot.setVisibility(View.VISIBLE);
                    cExpand.setVisibility(View.GONE);
                    cContraer.setVisibility(View.VISIBLE);
                    cSave.setVisibility(View.VISIBLE);
                    break;
                case R.id.cardContraer:
                    llBot.setVisibility(View.GONE);
                    cExpand.setVisibility(View.VISIBLE);
                    cContraer.setVisibility(View.GONE);
                    cSave.setVisibility(View.GONE);
                    break;
                case R.id.cardGuardar:
                    llBot.setVisibility(View.GONE);
                    cExpand.setVisibility(View.VISIBLE);
                    cContraer.setVisibility(View.GONE);
                    cSave.setVisibility(View.GONE);
                    new AdmClientes().guardarCliente(pos);
                    break;
            }
        }
    }

    public ClienteAdapter(List<Cliente> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup padre, int i) {
        View v = LayoutInflater.from(padre.getContext()).inflate(R.layout.cliente_card, padre, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        holder.pos = posicion;
        holder.cTitulo.setText(items.get(posicion).getNombre() + " " + items.get(posicion).getApellido());
        holder.cNombre.setText(items.get(posicion).getNombre());
        holder.cApellido.setText(items.get(posicion).getApellido());
        holder.cDNI.setText(items.get(posicion).getDni());
        holder.cDirecc.setText(items.get(posicion).getDireccion());
        holder.cLocal.setText(items.get(posicion).getLocalidad());
        holder.cProv.setText(items.get(posicion).getProvincia());
        holder.cCpostal.setText(items.get(posicion).getcPostal());
        holder.cTelf.setText(items.get(posicion).getTelefono());
        holder.cMail.setText(items.get(posicion).getMail());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}