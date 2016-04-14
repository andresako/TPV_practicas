package overant.asako.tpv.Admin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import overant.asako.tpv.R;
import overant.asako.tpv.Utils.Herramientas;

public class Administracion extends Activity implements View.OnClickListener {

    private static final String RUTA_GALERIA = "http://overant.es/galeria/";
    private LinearLayout llArtExt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Administracion.this);

        // setear botones
        Button mEmpresa = (Button) findViewById(R.id.btnAdmEmpresa);
        Button mPuntVenta = (Button) findViewById(R.id.btnAdmPuntoVenta);
        Button mArticulo = (Button) findViewById(R.id.btnAdmArticulo);
        Button mCliente = (Button) findViewById(R.id.btnAdmCliente);
        Button mAlmacen = (Button) findViewById(R.id.btnAdmAlmacen);
        Button mProveedor = (Button) findViewById(R.id.btnAdmProveedor);
        Button mArticuloCat = (Button) findViewById(R.id.btnAdmArticuloCat);
        Button mArticuloLista = (Button) findViewById(R.id.btnAdmArticuloLista);

        mEmpresa.setOnClickListener(this);
        mPuntVenta.setOnClickListener(this);
        mArticulo.setOnClickListener(this);
        mCliente.setOnClickListener(this);
        mAlmacen.setOnClickListener(this);
        mProveedor.setOnClickListener(this);
        mArticuloCat.setOnClickListener(this);
        mArticuloLista.setOnClickListener(this);

        TextView titulo = (TextView)findViewById(R.id.tituloEmpresa);
        titulo.setText(sp.getString("empresaNombre", "Empresa"));

        ImageView img = (ImageView)findViewById(R.id.imgTituloEmpresa);
        new Herramientas.ponerImagen(img).execute(RUTA_GALERIA + sp.getString("empresaLogo", ""));

        llArtExt = (LinearLayout) findViewById(R.id.admArticuloExt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdmEmpresa:
                Intent admEmp = new Intent(Administracion.this, AdmEmpresa.class);
                admEmp.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admEmp);
                break;

            case R.id.btnAdmPuntoVenta:
                Intent admPVenta = new Intent(Administracion.this, AdmListaPuntoVenta.class);
                admPVenta.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admPVenta);
                break;

            case R.id.btnAdmArticulo:
                if (llArtExt.isShown()) llArtExt.setVisibility(View.GONE);
                else llArtExt.setVisibility(View.VISIBLE);
                break;

            case R.id.btnAdmCliente:
                Intent admCli = new Intent(Administracion.this, AdmListaClientes.class);
                admCli.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admCli);
                break;

            case R.id.btnAdmAlmacen:
                Intent admAlm = new Intent(Administracion.this, AdmListaAlmacenes.class);
                admAlm.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admAlm);
                break;

            case R.id.btnAdmProveedor:
                Intent admPro = new Intent(Administracion.this, AdmListaProveedores.class);
                admPro.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admPro);
                break;

            case R.id.btnAdmArticuloLista:
                Intent admArtLis = new Intent(Administracion.this, AdmListaArticulos.class);
                admArtLis.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admArtLis);
                break;

            case R.id.btnAdmArticuloCat:
                Intent admArtLisCat = new Intent(Administracion.this, AdmListaCategorias.class);
                admArtLisCat.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admArtLisCat);
                break;

            default:
                break;
        }
    }
}
