package com.example.bonairecontrol.ui.calendar;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bonairecontrol.CalendarActivity;
import com.example.bonairecontrol.Controlador_adaptador.DevicesAdapter;
import com.example.bonairecontrol.InterfazApi.VWEquiposPorUsuarioCaller;
import com.example.bonairecontrol.Modelos.ModelDevices;
import com.example.bonairecontrol.ModelosApi.VWEquiposPorUsuarioModel;
import com.example.bonairecontrol.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarFragment extends Fragment implements AdapterView.OnItemClickListener {

    Context context = getContext();
    private ImageView image;
    private ListView lvCalendar;
    private ProgressBar progressBar;
    final private List<ModelDevices> lst = new ArrayList<>();
    List<VWEquiposPorUsuarioModel> lstEquipos = null;

    String UrlBase = "";
    private DevicesAdapter adapter;
    Retrofit retrofit = null;
    View root =  null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UrlBase = getString(R.string.url_base_api);

        retrofit = new Retrofit.Builder().baseUrl(UrlBase)
                .addConverterFactory(GsonConverterFactory.create()).build();

        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        lvCalendar = (ListView) root.findViewById(R.id.list_view_devices_calendar);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBarCalendar);
        image = (ImageView) root.findViewById(R.id.image_backgroud_devicesCalendar);

        try {
            adapter = new DevicesAdapter(getContext(), R.layout.list_devices, lst);
        }catch (Exception ignored){}


        progressBar.setVisibility(View.VISIBLE);

        fncContruirLista(lst);
        fncRevisarLista(lst);

        lvCalendar.setOnItemClickListener(this);

        SharedPreferences sharPref = getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        if (sharPref.getBoolean("onlyBluetooth", false)){
            Toast.makeText(getContext(), "No es posible abrir esta pantalla ya que se encuentra conectado via solo bluetooth", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {super.onViewCreated(view, savedInstanceState);}
    private void fncContruirLista(List<ModelDevices> lst) {

        SharedPreferences sharPref = this.getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        String email = sharPref.getString("userEmail", "");

        lst.clear();
        lvCalendar.setAdapter(null);
        progressBar.setVisibility(View.VISIBLE);

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
                            String date = i.getFechaHoraUltimoReporte();
                            if (date!=null){
                                date = date.replace("T", " ");
                            }else {
                                date = "No se encontro un ultimo reporte";
                            }

                            String id = "" + i.getMac().charAt(i.getMac().length()-5) + i.getMac().charAt(i.getMac().length()-4) + i.getMac().charAt(i.getMac().length()-3) + i.getMac().charAt(i.getMac().length()-2) + i.getMac().charAt(i.getMac().length()-1);
                            if (i.getNombreEquipo().equals("B-TECH-CE")){
                                //lst.add(new ModelDevices(id, "activo", date, "internet", i.getNombreLugarInstalacion(), i.getNombreEquipo(), R.drawable.ico_tech_ce, i.getMac()));
                            }
                            if (i.getNombreEquipo().equals("B-Q600")){
                                lst.add(new ModelDevices(id, "activo", date, "internet", i.getNombreLugarInstalacion(), i.getNombreEquipo(), R.drawable.bqd, i.getMac()));
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

            lvCalendar.setVisibility(View.VISIBLE);
            lvCalendar.setAdapter(adapter);
            image.setVisibility(View.INVISIBLE);
        } else {

            lvCalendar.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SharedPreferences sharPref = this.getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        String name = adapter.getItem(position).getMac();

            editor.putString("MacString", name);
            editor.apply();
            Intent intent = new Intent(getContext(), CalendarActivity.class);
            startActivity(intent);

    }
}