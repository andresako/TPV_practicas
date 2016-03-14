package overant.asako.tpv.Admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import overant.asako.tpv.R;

public class Administracion extends Activity implements View.OnClickListener {

    private Button mEmpresa, mPuntVenta, mArticulo, mCliente, mAlmacen, mProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion);

        // setear botones
        mEmpresa = (Button) findViewById(R.id.btnAdmEmpresa);
        mPuntVenta = (Button) findViewById(R.id.btnAdmPuntoVenta);
        mArticulo = (Button) findViewById(R.id.btnAdmArticulo);
        mCliente = (Button) findViewById(R.id.btnAdmCliente);
        mAlmacen = (Button) findViewById(R.id.btnAdmAlmacen);
        mProveedor = (Button) findViewById(R.id.btnAdmProveedor);

        mEmpresa.setOnClickListener(this);
        mPuntVenta.setOnClickListener(this);
        mArticulo.setOnClickListener(this);
        mCliente.setOnClickListener(this);
        mAlmacen.setOnClickListener(this);
        mProveedor.setOnClickListener(this);
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
                break;
            case R.id.btnAdmArticulo:
                break;
            case R.id.btnAdmCliente:

                Intent admCli = new Intent(Administracion.this, AdmClientes.class);
                admCli.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(admCli);

                break;
            case R.id.btnAdmAlmacen:
                break;
            case R.id.btnAdmProveedor:
                break;

            default:
                break;
        }
    }
}