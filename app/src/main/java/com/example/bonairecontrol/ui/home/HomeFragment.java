package com.example.bonairecontrol.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.Clases.EquiposHome;
import com.example.bonairecontrol.Controlador_adaptador.adaptadorEquiposHome;
import com.example.bonairecontrol.HomeActivity;
import com.example.bonairecontrol.Interfaz.navigationHome;
import com.example.bonairecontrol.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RadioButton rbBluetooth, rbInternet;
    private final Context context = getContext();
    private RecyclerView recyclerView;
    private navigationHome nav;

    ArrayList<EquiposHome> listEquipos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        init(root);
        SharedPreferences sharPref = getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();
        rbInternet.setEnabled(!sharPref.getBoolean("onlyBluetooth", false));

        rbBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbBluetooth.isChecked()) {

                    editor.putBoolean("blTypeConnection", false);
                    editor.apply();
                }
            }
        });

        rbInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("blTypeConnection", true);
                editor.apply();
            }
        });
        return root;
    }

    private void init(View root) {
        rbBluetooth = root.findViewById(R.id.rb_conexion_bluetooth_home);
        rbInternet = root.findViewById(R.id.rb_conexion_internet_home);

        SharedPreferences sharPref = getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        boolean typeConnection = sharPref.getBoolean("blTypeConnection", true);
        if (typeConnection) {
            rbInternet.setChecked(true);
        } else {
            rbBluetooth.setChecked(true);
        }

        nav = new HomeActivity();
        listEquipos = new ArrayList<>();
        recyclerView = root.findViewById(R.id.rv_equipos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        validarListaRecicler();
    }

    private void validarListaRecicler() {
        SharedPreferences sharPref = getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharPref.edit();
        boolean eliminadorHumo, bq600, techCe, bonaireBqv, campanas;
        eliminadorHumo = sharPref.getBoolean("initElimHumo", false);
        bq600 = sharPref.getBoolean("initBq600", false);
        techCe = sharPref.getBoolean("initTechCe", false);
        bonaireBqv = sharPref.getBoolean("initBqv", false);
        campanas = sharPref.getBoolean("initCampanas", false);
        /*editor.putBoolean("initElimHumo", false);
        editor.putBoolean("initBq600", false);
        editor.putBoolean("initTechCe", false);
        editor.apply();*/

        if (eliminadorHumo || bq600 || techCe || bonaireBqv || campanas) {
            cargeRecicler(eliminadorHumo, bq600, techCe, bonaireBqv, campanas);
        }
    }

    private void cargeRecicler(boolean eliminadorHumo, boolean bq600, boolean techCe, boolean bonaireBqv, boolean campanas) {
        if (eliminadorHumo || bq600 || techCe || bonaireBqv || campanas) {
            listEquipos.clear();
            if (eliminadorHumo) {
                listEquipos.add(new EquiposHome("Bonaire-BQH",getContext().getString(R.string.descripcion_bonaire_bqh), R.drawable.eliminar_humo_ico));
            }
            if (bq600) {
                listEquipos.add(new EquiposHome("Bonaire-BQD", getContext().getString(R.string.descripcion_bonaire_bqd), R.drawable.bqd));
            }
            if (bonaireBqv) {
                listEquipos.add(new EquiposHome("Bonaire-BQV", getContext().getString(R.string.descripcion_bonaire_bqv), R.drawable.bqv));
            }
            if (techCe) {
                listEquipos.add(new EquiposHome("Bonaire-TECH", getContext().getString(R.string.descripcion_bonaire_tech), R.drawable.b_tech));
            }
            if (campanas){
                listEquipos.add(new EquiposHome("COCINAS INTELIGENTES", "Sin descripcion", R.drawable.b_tech));
            }
            adaptadorEquiposHome adapter = new adaptadorEquiposHome(listEquipos);
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = listEquipos.get(recyclerView.getChildAdapterPosition(v)).getTitle();
                    switch (title) {
                        case "Bonaire-BQH":
                            nav.toEliminadorHumo(getActivity());
                            break;
                        case "Bonaire-BQD":
                        case "Bonaire-BQV":
                        case "Bonaire-TECH":
                            nav.toDevices(getActivity());
                            break;
                        case "COCINAS INTELIGENTES":
                            nav.toCampanas(getActivity());
                    }
                }
            });
            recyclerView.setAdapter(adapter);

        }
    }
}