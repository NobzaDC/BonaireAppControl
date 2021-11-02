package com.example.bonairecontrol.graphics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bonairecontrol.Controlador_adaptador.adapterGraphicsListData;
import com.example.bonairecontrol.InterfazApi.VWSensorReporteApiCaller;
import com.example.bonairecontrol.ModelosApi.VWSensorReporteModel;
import com.example.bonairecontrol.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class graphicsListDataActivity extends AppCompatActivity {

    String UrlBase, Mac, TAG = "Error Bonaire";
    Retrofit retrofit;
    Context context = this;
    List<VWSensorReporteModel> lst ;
    androidx.recyclerview.widget.RecyclerView recycler;
    adapterGraphicsListData adapter;
    ArrayList<VWSensorReporteModel> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_list_data);

        recycler = findViewById(R.id.recile_graphics_list_data);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        UrlBase = getString(R.string.url_base_api);

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();
        Mac = sharPref.getString("MacString", "");

        retrofit = new Retrofit.Builder().baseUrl(UrlBase)
                .addConverterFactory(GsonConverterFactory.create()).build();

        fncLoadSensoresReporte(Mac);

    }

    public void fncLoadSensoresReporte(String mac){
        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();
        VWSensorReporteApiCaller caller = retrofit.create(VWSensorReporteApiCaller.class);
        Call<List<VWSensorReporteModel>> sensorcall = caller.GetListaResportes(mac);
        sensorcall.enqueue(new Callback<List<VWSensorReporteModel>>() {
            @Override
            public void onResponse(Call<List<VWSensorReporteModel>> call, Response<List<VWSensorReporteModel>> response) {
                if (response.isSuccessful()){
                    lst = response.body();
                    list.addAll(lst);
                    adapter = new adapterGraphicsListData(list);
                    recycler.setAdapter(adapter);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent graphic = new Intent(context, graphicsActivity.class);
                            editor.putString("tipoSensor", list.get(recycler.getChildAdapterPosition(v)).getIdParametroUi());
                            editor.putString("MacString", Mac);
                            editor.apply();
                            startActivity(graphic);
                        }
                    });
                }else {
                    Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error, bad response in the api: " + UrlBase);
                }
            }

            @Override
            public void onFailure(Call<List<VWSensorReporteModel>> call, Throwable t) {
                Toast.makeText(context, "Error de conexion", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error, fail in the api: " + UrlBase);
            }
        });
    }
}