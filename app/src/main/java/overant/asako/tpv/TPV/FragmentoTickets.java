package overant.asako.tpv.TPV;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import overant.asako.tpv.Clases.Carrito;
import overant.asako.tpv.R;
import overant.asako.tpv.Utils.AdaptadorTickets;
import overant.asako.tpv.Utils.JSONParser;

public class FragmentoTickets extends Fragment {

    private ActividadPrincipal pa;
    private RecyclerView reciclador;

    public FragmentoTickets() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_tickets, container, false);

        pa = (ActividadPrincipal) getActivity();
        setHasOptionsMenu(true);

        reciclador = (RecyclerView) v.findViewById(R.id.reciclador);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(linearLayout);

        return v;
    }

    @Override
    public void onResume() {
        new CargarLista().execute();
        super.onResume();
    }

    private class CargarLista extends AsyncTask<Void, Void, Boolean> {
        JSONParser jsonParser = new JSONParser();
        ArrayList<Carrito> lista = new ArrayList<>();
        private String URL = "http://overant.es/TPV_java.php";
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                pDialog = new ProgressDialog(pa);
                pDialog.setMessage("Actualizando datos...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("accion", "1"));
            params.add(new BasicNameValuePair("empresaId", pa.empresaId + ""));
            JSONObject json = jsonParser.peticionHttp(URL, "POST", params);

            int resp = 0;

            try {
                resp = json.getInt("Res");

                if (resp == 1) {

                    JSONArray jsonArray = json.getJSONArray("ticket");
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject c = jsonArray.getJSONObject(x);

                        Carrito cr = new Carrito(
                                c.getInt("id"),
                                pa.empresaId,
                                c.getInt("user"),
                                c.getInt("numero"),
                                c.getString("fecha"),
                                c.getDouble("total"));
                        if(c.getString("cerrado").equals("S")) cr.setCerrado(true);

                        lista.add(cr);
                    }

                }else{
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            if (msg) {
                AdaptadorTickets adaptador = new AdaptadorTickets(pa, lista);
                reciclador.setAdapter(adaptador);
            }
            pDialog.dismiss();
            super.onPostExecute(msg);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
}
