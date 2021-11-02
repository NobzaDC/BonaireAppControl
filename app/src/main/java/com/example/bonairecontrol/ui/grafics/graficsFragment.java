package com.example.bonairecontrol.ui.grafics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.example.bonairecontrol.Controlador_adaptador.DevicesAdapter;
import com.example.bonairecontrol.InterfazApi.VWEquiposPorUsuarioCaller;
import com.example.bonairecontrol.Modelos.ModelDevices;
import com.example.bonairecontrol.ModelosApi.VWEquiposPorUsuarioModel;
import com.example.bonairecontrol.R;
import com.example.bonairecontrol.graphics.graphicsListDataActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class graficsFragment extends Fragment implements AdapterView.OnItemClickListener {

    Context context = getContext();
    private ImageView image_backgroud_devicesGrafics;
    private ListView list_view_devicesGrafics;
    private ProgressBar progressBarGrafics;
    final private List<ModelDevices> lst = new ArrayList<>();
    List<VWEquiposPorUsuarioModel> lstEquipos = null;

    String UrlBase = "";
    private DevicesAdapter adapter;
    View root = null;

    Retrofit retrofit = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UrlBase = getString(R.string.url_base_api);

        retrofit = new Retrofit.Builder().baseUrl(UrlBase)
                .addConverterFactory(GsonConverterFactory.create()).build();

        root = inflater.inflate(R.layout.fragment_grafics, container, false);

        list_view_devicesGrafics = (ListView) root.findViewById(R.id.list_view_devicesGrafics);
        progressBarGrafics = (ProgressBar) root.findViewById(R.id.progressBarGrafics);
        image_backgroud_devicesGrafics = (ImageView) root.findViewById(R.id.image_backgroud_devicesGrafics);

        try {
            adapter = new DevicesAdapter(getContext(), R.layout.list_devices, lst);
        }catch (Exception ignored){}

        progressBarGrafics.setVisibility(View.VISIBLE);

        fncContruirLista(lst);
        fncRevisarLista(lst);

        list_view_devicesGrafics.setOnItemClickListener(this);

        return root;
    }

    private void fncContruirLista(List<ModelDevices> lst) {

        SharedPreferences sharPref = this.getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        String email = sharPref.getString("userEmail", "");

        lst.clear();
        list_view_devicesGrafics.setAdapter(null);
        progressBarGrafics.setVisibility(View.VISIBLE);

            VWEquiposPorUsuarioCaller caller = retrofit.create(VWEquiposPorUsuarioCaller.class);
            Call<List<VWEquiposPorUsuarioModel>> vwequiposcall = caller.get_data_equipos(email);
            vwequiposcall.enqueue(new Callback<List<VWEquiposPorUsuarioModel>>() {
                @Override
                public void onResponse(Call<List<VWEquiposPorUsuarioModel>> call, Response<List<VWEquiposPorUsuarioModel>> response) {

                    if (response.isSuccessful()){

                        lst.clear();

                        lstEquipos = response.body();
                        for (VWEquiposPorUsuarioModel i: lstEquipos){
                            if (i.isEstado()){
                                String id = "" + i.getMac().charAt(i.getMac().length()-5) + i.getMac().charAt(i.getMac().length()-4) + i.getMac().charAt(i.getMac().length()-3) + i.getMac().charAt(i.getMac().length()-2) + i.getMac().charAt(i.getMac().length()-1);
                                if (i.getNombreEquipo().equals("B-TECH-CE")){
                                    lst.add(new ModelDevices(id, "activo", i.getFechaHoraUltimoReporte(), "internet", i.getNombreLugarInstalacion(), i.getNombreEquipo(), R.drawable.b_tech, i.getMac()));
                                }
                                if (i.getNombreEquipo().equals("B-Q600")){
                                    //lst.add(new ModelDevices(id, "activo", i.getFechaHoraUltimoReporte(), "internet", i.getNombreLugarInstalacion(), i.getNombreEquipo(), R.drawable.ico_bq600, i.getMac()));
                                }
                            }
                        }
                        fncRevisarLista(lst);
                    }
                }

                @Override
                public void onFailure(Call<List<VWEquiposPorUsuarioModel>> call, Throwable t) {

                }
            });
        fncRevisarLista(lst);
    }

    private void fncRevisarLista(List<ModelDevices> lst) {
        if (lst.size() != 0) {

            list_view_devicesGrafics.setVisibility(View.VISIBLE);
            list_view_devicesGrafics.setAdapter(adapter);
            image_backgroud_devicesGrafics.setVisibility(View.INVISIBLE);
        } else {

            list_view_devicesGrafics.setVisibility(View.INVISIBLE);
            image_backgroud_devicesGrafics.setVisibility(View.VISIBLE);
        }
        progressBarGrafics.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SharedPreferences sharPref = this.getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        String tipo = adapter.getItem(position).getTipoEquipo();
        String name = adapter.getItem(position).getMac();
        String location = adapter.getItem(position).getUbicacion();


        if (tipo.equals("B-Q600")) {
            //editor.putString("MacString", name);
            //editor.apply();
            //Intent intent_bq_600 = new Intent(getContext(), Bq_600Activity.class);
            //startActivity(intent_bq_600);
        } else  if (tipo.equals("B-TECH-CE")) {
                editor.putString("MacString", name);
                editor.putString("locationDevice", location);
                editor.apply();
                Intent intent_tech_ce = new Intent(getContext(), graphicsListDataActivity.class);
                startActivity(intent_tech_ce);
        }
    }
}