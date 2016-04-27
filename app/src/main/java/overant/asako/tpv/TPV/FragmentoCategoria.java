package overant.asako.tpv.TPV;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import overant.asako.tpv.R;
import overant.asako.tpv.Utils.AdaptadorCategorias;
import overant.asako.tpv.Utils.Datos;

public class FragmentoCategoria extends Fragment {

    private static final String INDICE_SECCION = "INDICE_SECCION";

    private RecyclerView reciclador;
    private GridLayoutManager layoutManager;
    private AdaptadorCategorias adaptador;
    private ActividadPrincipal ap;

    public static FragmentoCategoria nuevaInstancia(int indiceSeccion) {
        FragmentoCategoria fragment = new FragmentoCategoria();
        Bundle args = new Bundle();
        args.putInt(INDICE_SECCION, indiceSeccion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_categoria, container, false);
        ap = ((ActividadPrincipal)getActivity());

        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        reciclador.setLayoutManager(layoutManager);

        int indiceSeccion = getArguments().getInt(INDICE_SECCION);

        adaptador = new AdaptadorCategorias(ap.datos.getListaArticulosPorCat(indiceSeccion), ap);
        reciclador.setAdapter(adaptador);

        return view;
    }

}
