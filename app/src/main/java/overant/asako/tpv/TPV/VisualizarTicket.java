package overant.asako.tpv.TPV;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import overant.asako.tpv.Clases.Carrito;
import overant.asako.tpv.Clases.Linea;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.AdaptadorTicket;
import overant.asako.tpv.Utils.AdaptadorTickets;

public class VisualizarTicket extends AppCompatActivity {

    private Carrito carro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ticket);

        Intent i = getIntent();
        carro = i.getParcelableExtra("carro");
        ArrayList<Linea> listas = (ArrayList<Linea>) i.getSerializableExtra("lista");
        Log.d("Lineas: ", listas.size()+"");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(VisualizarTicket.this);
        ((TextView)findViewById(R.id.cabecera_titulo)).setText(sp.getString("empresaNombre","Nombre"));
        ((TextView)findViewById(R.id.cabecera_fecha)).setText(carro.getFecha());
        ((TextView)findViewById(R.id.cabecera_numero)).setText(carro.getNumero()+"");
        ((TextView)findViewById(R.id.ticketTotal)).setText(carro.getTotal()+ " €");

        RecyclerView reciclador = (RecyclerView) findViewById(R.id.reciclador);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        reciclador.setLayoutManager(linearLayout);
        AdaptadorTicket adaptador = new AdaptadorTicket(listas);
        reciclador.setAdapter(adaptador);
    }
}
