package com.univalle.javiermurguia.proyectoteleferico.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.univalle.javiermurguia.proyectoteleferico.Models.Marcador;
import com.univalle.javiermurguia.proyectoteleferico.R;

public class InfoFragment extends Fragment {

    private TextView nombre,nombreInfo,descripcion,descripcionInfo;
    private Button cerrar;
    private Marcador marker;

    public InfoFragment() {
        // Required empty public constructor
    }

    private void initialize(View view){
        this.nombre = view.findViewById(R.id.textViewNombre);
        this.nombreInfo = view.findViewById(R.id.textViewNombreInfo);
        this.descripcion = view.findViewById(R.id.textViewDescripcion);
        this.descripcionInfo = view.findViewById(R.id.textViewDescripcionInfo);
        this.cerrar = view.findViewById(R.id.buttonClose);
        this.cerrar.setOnClickListener(viewCerrar -> cerrarFragment());
        this.nombre.setText("Nombre: ");
        this.descripcion.setText("Descripción: ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("infoMarcador", this, (requestKey, result) -> loadInfo(result));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadInfo(Bundle info){
        this.marker = (Marcador) info.getSerializable("marcador");
        this.nombreInfo.setText(this.marker.getNombre());
        this.descripcionInfo.setText(this.marker.getDescripcion());
        this.nombre.setVisibility(View.VISIBLE);
        this.nombreInfo.setVisibility(View.VISIBLE);
        this.descripcion.setVisibility(View.VISIBLE);
        this.descripcionInfo.setVisibility(View.VISIBLE);
        this.cerrar.setVisibility(View.VISIBLE);
    }

    public void cerrarFragment(){
        this.nombre.setVisibility(View.INVISIBLE);
        this.nombreInfo.setVisibility(View.INVISIBLE);
        this.descripcion.setVisibility(View.INVISIBLE);
        this.descripcionInfo.setVisibility(View.INVISIBLE);
        this.cerrar.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        initialize(view);
        return view;
    }
}