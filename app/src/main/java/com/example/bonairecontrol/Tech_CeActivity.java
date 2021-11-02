package com.example.bonairecontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bonairecontrol.InterfazApi.VWSensorReporteApiCaller;
import com.example.bonairecontrol.Modelos.ModelListDevices;
import com.example.bonairecontrol.ModelosApi.VWSensorReporteModel;
import com.example.bonairecontrol.graphics.graphicsActivity;
import com.example.bonairecontrol.new_adapters.sensoresAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Tech_CeActivity extends AppCompatActivity {
    private TextView txt_nombre_equipo_tech_ce, tvLocation, tvReload;
    private RecyclerView reciclerListSensor;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final static String TAG = "Bonaire";

    int timer = 0;
    String UrlBase = "", macName, name, location;
    Context context = this;
    Retrofit retrofit = null;
    List<VWSensorReporteModel> sensorReporteList;
    List<ModelListDevices> listDevices = new ArrayList<>();

    sensoresAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech__ce);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        macName = sharPref.getString("MacString", "");
        location = sharPref.getString("LocationDevice", "");

        UrlBase = getString(R.string.url_base_api);

        retrofit = new Retrofit.Builder().baseUrl(UrlBase)
                .addConverterFactory(GsonConverterFactory.create()).build();

        tvLocation = findViewById(R.id.txt_location_tech_ce);
        txt_nombre_equipo_tech_ce = findViewById(R.id.txt_nombre_equipo_tech_ce);
        tvReload = findViewById(R.id.tv_next_reload_tech_ce);
        reciclerListSensor = findViewById(R.id.rl_list_datos);
        swipeRefreshLayout = findViewById(R.id.sr_tech_ce);

        reciclerListSensor.setLayoutManager(new LinearLayoutManager(this));

        name = "" + macName.charAt(macName.length()-5) + macName.charAt(macName.length()-4) + macName.charAt(macName.length()-3) + macName.charAt(macName.length()-2) + macName.charAt(macName.length()-1);
        tvLocation.setText(location);
        txt_nombre_equipo_tech_ce.setText("TECH-CE");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                timer = 1;
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
                reciclerListSensor.setEnabled(false);
            }
        });

    }

    private void fncRecargarDatos(){
        Timer t = new Timer();
        final TimerTask loadListSensores = new TimerTask() {
            @Override
            public void run() {
                loadListSensores();
            }
        };
        t.schedule(loadListSensores, 0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fncRecargarDatos();
    }

    private void loadListSensores(){
        try {
            if (timer <= 0){
                timer = 60;
                VWSensorReporteApiCaller caller = retrofit.create(VWSensorReporteApiCaller.class);
                Call<List<VWSensorReporteModel>> sensorcall = caller.GetListaResportes(macName);
                sensorcall.enqueue(new Callback<List<VWSensorReporteModel>>() {
                    @Override
                    public void onResponse(Call<List<VWSensorReporteModel>> call, Response<List<VWSensorReporteModel>> response) {
                        List<ModelListDevices> list = new ArrayList<>();
                        if (response.isSuccessful()){
                            sensorReporteList = response.body();
                            String date = "";
                            Collections.sort(sensorReporteList, new Comparator<VWSensorReporteModel>() {
                                @Override
                                public int compare(VWSensorReporteModel o1, VWSensorReporteModel o2) {
                                    return o1.getOrdenar() - o2.getOrdenar();
                                }
                            });
                            for (VWSensorReporteModel i: sensorReporteList){
                                String value = i.getValorReportado() + " " + i.getUndMedicion();
                                String dateTime = i.getFechaReporte();
                                if (dateTime!=null){
                                    dateTime = dateTime.replace("T", " ");
                                }else {
                                    dateTime = "No se encontro un ultimo reporte";
                                }
                                switch(i.getIdParametroUi()) {
                                    case "O3":
                                        list.add(new ModelListDevices(R.drawable.o3, value,
                                                "Nombre: ", i.getNombre(), "Último reporte: ",
                                                dateTime, "Id: ", name, "Conexión actual wifi, esperando datos…",
                                                "Conectado", i.getIdParametroUi()));
                                        break;
                                    case "CO2":
                                        list.add(new ModelListDevices(R.drawable.co2, value,
                                                "Nombre: ", i.getNombre(), "Último reporte: ",
                                                dateTime, "Id: ", name, "Conexión actual wifi, esperando datos…",
                                                "Conectado", i.getIdParametroUi()));
                                        break;
                                    case "CO":
                                        list.add(new ModelListDevices(R.drawable.co, value,
                                                "Nombre: ", i.getNombre(), "Último reporte: ",
                                                dateTime, "Id: ", name, "Conexión actual wifi, esperando datos…",
                                                "Conectado", i.getIdParametroUi()));
                                        break;
                                    case "VOC":
                                        list.add(new ModelListDevices(R.drawable.voc, value,
                                                "Nombre: ", i.getNombre(), "Último reporte: ",
                                                dateTime, "Id: ", name, "Conexión actual wifi, esperando datos…",
                                                "Conectado", i.getIdParametroUi()));
                                        break;
                                    case "T":
                                        list.add(new ModelListDevices(R.drawable.temperatura, value,
                                                "Nombre: ", i.getNombre(), "Último reporte: ",
                                                dateTime, "Id: ", name, "Conexión actual wifi, esperando datos…",
                                                "Conectado", i.getIdParametroUi()));
                                        break;
                                    case "H":
                                        list.add(new ModelListDevices(R.drawable.humedad, value,
                                                "Nombre: ", i.getNombre(), "Último reporte: ",
                                                dateTime, "Id: ", name, "Conexión actual wifi, esperando datos…",
                                                "Conectado", i.getIdParametroUi()));
                                        break;
                                    case "PM1":
                                        list.add(new ModelListDevices(R.drawable.pm1, value,
                                                "Nombre: ", i.getNombre(), "Último reporte: ",
                                                dateTime, "Id: ", name, "Conexión actual wifi, esperando datos…",
                                                "Conectado", i.getIdParametroUi()));
                                        break;
                                    case "PM25":
                                        list.add(new ModelListDevices(R.drawable.pm25, value,
                                                "Nombre: ", i.getNombre(),"Último reporte: ",
                                                dateTime, "Id: ", name,  "Conexión actual wifi, esperando datos…",
                                                "Conectado", i.getIdParametroUi()));
                                        break;
                                    case "PM10":
                                        list.add(new ModelListDevices(R.drawable.pm10, value,
                                                "Nombre: ", i.getNombre(), "Último reporte: ",
                                                dateTime, "Id: ", name, "Conexión actual wifi, esperando datos…",
                                                "Conectado", i.getIdParametroUi()));
                                        break;
                                }
                            }
                            showSensores(list, date);
                        }else {

                            Log.e(TAG, "Error al cargar datos");
                            swipeRefreshLayout.setEnabled(true);
                            reciclerListSensor.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<VWSensorReporteModel>> call, Throwable t) {
                        Log.e(TAG, "Failure in response of api " + t);
                        swipeRefreshLayout.setEnabled(true);
                        reciclerListSensor.setEnabled(true);
                    }
                });
            }
            tvReload.setText(timer + " seg.");
            timer = timer - 1;

        }catch (Exception e){
            timer = 1;
            swipeRefreshLayout.setEnabled(true);
            reciclerListSensor.setEnabled(true);
        }

    }

    private void showSensores(List<ModelListDevices> sensorReporteList, String date) {
        try {
            adapter = new sensoresAdapter(sensorReporteList);
            reciclerListSensor.setAdapter(adapter);
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharPref.edit();
                    Intent graphic = new Intent(context, graphicsActivity.class);
                    editor.putString("tipoSensor", listDevices.get(reciclerListSensor.getChildAdapterPosition(v)).getIdParametroUi());
                    editor.putString("MacString", macName);
                    editor.apply();
                    startActivity(graphic);
                }
            });
            swipeRefreshLayout.setEnabled(true);
            reciclerListSensor.setEnabled(true);
        }catch (Exception e){
            swipeRefreshLayout.setEnabled(true);
            reciclerListSensor.setEnabled(true);
        }
        listDevices.clear();
        listDevices = sensorReporteList;

    }
}