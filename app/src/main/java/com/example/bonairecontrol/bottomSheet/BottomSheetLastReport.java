package com.example.bonairecontrol.bottomSheet;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bonairecontrol.InterfazApi.VWSensoresRegistroUndMedicionApiCaller;
import com.example.bonairecontrol.ModelosApi.MacModel;
import com.example.bonairecontrol.ModelosApi.NivelesAmbienteModel;
import com.example.bonairecontrol.ModelosApi.NivelesAmbientexLugarModel;
import com.example.bonairecontrol.ModelosApi.VWSensoresRegistroUndMedicionModel;
import com.example.bonairecontrol.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import pl.pawelkleczkowski.customgauge.CustomGauge;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetLastReport extends BottomSheetDialogFragment {

    private CustomGauge customGauge;
    private TextView tvValue;
    private Retrofit retrofit;
    private String Mac, Sensor, UrlBase, unidad;
    private int lugarInstalacion;
    private Float value;

    //Modelos
    MacModel macModel;
    NivelesAmbientexLugarModel nivelesxLugarModel;
    NivelesAmbienteModel nivelesModel;
    List<VWSensoresRegistroUndMedicionModel> lstReports;
    VWSensoresRegistroUndMedicionModel lastReports;

    //colores
    private static final int intColBueno = Color.rgb(8, 255, 0);
    private static final int intColRegular = Color.rgb(255, 255, 0);
    private static final int intColMalo = Color.rgb(255, 139, 0);
    private static final int intColMuyMalo = Color.rgb(255, 0, 0);
    private static final int intColExtreMalo = Color.rgb(74, 35, 90);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_last_graphic, container, false);
        init(v);
        return v;
    }

    private void init(View v) {
        customGauge = (CustomGauge) v.findViewById(R.id.customGauge);
        tvValue = (TextView) v.findViewById(R.id.tv_value_graphic);

        SharedPreferences sharPref = this.getActivity().getSharedPreferences("BonairePref", getContext().MODE_PRIVATE);

        Mac = sharPref.getString("MacString", "");
        Sensor = sharPref.getString("tipoSensor", "");

        UrlBase = getString(R.string.url_base_api);

        retrofit = new Retrofit.Builder().baseUrl(UrlBase)
                .addConverterFactory(GsonConverterFactory.create()).build();

        VWSensoresRegistroUndMedicionApiCaller sensorReporte = retrofit.create(VWSensoresRegistroUndMedicionApiCaller.class);

        Call<List<VWSensoresRegistroUndMedicionModel>> listCall = sensorReporte.getDatos(Mac, Sensor);
        listCall.enqueue(new Callback<List<VWSensoresRegistroUndMedicionModel>>() {
            @Override
            public void onResponse(Call<List<VWSensoresRegistroUndMedicionModel>> call, Response<List<VWSensoresRegistroUndMedicionModel>> response) {
                if (response.isSuccessful()) {
                    lstReports = response.body();
                    int i = 1;
                    lastReports = lstReports.get(0);
                    createReport(Float.valueOf(lastReports.getValorReportado()), lastReports.getUndMedicion());
                    mostrarValor(lastReports);
                } else {
                    value = 0f;
                    unidad = "xx";
                    createReport(value, unidad);
                }
            }

            @Override
            public void onFailure(Call<List<VWSensoresRegistroUndMedicionModel>> call, Throwable t) {
                value = 0f;
                unidad = "xx";
                createReport(value, unidad);
            }
        });

    }

    /*private void callApis() {
        try {
            MacApiCaller macApiCaller = retrofit.create(MacApiCaller.class);
            Call<MacModel> macCall = macApiCaller.get_info_mac(Mac);
            macCall.enqueue(new Callback<MacModel>() {
                @Override
                public void onResponse(Call<MacModel> call, Response<MacModel> response) {
                    if (response.isSuccessful()) {
                        macModel = response.body();
                        lugarInstalacion = macModel.getIdLugarInstalacion();
                    } else {
                        lugarInstalacion = 0;
                    }
                    callReport();
                }

                @Override
                public void onFailure(Call<MacModel> call, Throwable t) {
                    lugarInstalacion = 0;
                    callReport();
                }
            });

        } catch (Exception e) {
            createReport(0f, "xx");
        }
    }*/

    /*private void callReport() {
        if (lugarInstalacion != 0) {
            NivelesAmbientexLugarApiCaller lugarApiCaller = retrofit.create(NivelesAmbientexLugarApiCaller.class);
            Call<NivelesAmbientexLugarModel> lugarModel = lugarApiCaller.getDatos(Sensor, lugarInstalacion);
            lugarModel.enqueue(new Callback<NivelesAmbientexLugarModel>() {
                @Override
                public void onResponse(Call<NivelesAmbientexLugarModel> call, Response<NivelesAmbientexLugarModel> response) {
                    if (response.isSuccessful()) {
                        nivelesxLugarModel = response.body();
                        mostrarValor(value, nivelesxLugarModel.getBuena(), nivelesxLugarModel.getRegular(), nivelesxLugarModel.getMala(), nivelesxLugarModel.getAlta(), nivelesxLugarModel.getCritica());
                    } else {
                        mostrarValor(0f, 1f, 2f, 3f, 4f, 5f);
                    }
                }

                @Override
                public void onFailure(Call<NivelesAmbientexLugarModel> call, Throwable t) {
                    mostrarValor(0f, 1f, 2f, 3f, 4f, 5f);
                }
            });
        } else {
            NivelesAmbienteApiCaller ambienteApiCaller = retrofit.create(NivelesAmbienteApiCaller.class);
            Call<NivelesAmbienteModel> nivelesAmbienteModelCall = ambienteApiCaller.getDatos(Sensor);
            nivelesAmbienteModelCall.enqueue(new Callback<NivelesAmbienteModel>() {
                @Override
                public void onResponse(Call<NivelesAmbienteModel> call, Response<NivelesAmbienteModel> response) {
                    if (response.isSuccessful()) {
                        nivelesModel = response.body();
                        mostrarValor(value, nivelesModel.getBuena(), nivelesModel.getRegular(), nivelesModel.getMala(), nivelesModel.getAlta(), nivelesModel.getCritica());
                    } else {
                        mostrarValor(0f, 1f, 2f, 3f, 4f, 5f);
                    }
                }

                @Override
                public void onFailure(Call<NivelesAmbienteModel> call, Throwable t) {
                    mostrarValor(0f, 1f, 2f, 3f, 4f, 5f);
                }
            });
        }
    }*/

    private void createReport(Float value, String unidad) {
        tvValue.setText(String.format("%s %s", value, unidad));
        /*callApis();*/
    }

    /*private void mostrarValor(float round, float buena, float regular, float mala, float alta, float critica) {
        if (round >= critica) {
            round = critica;
        }
        if (round <= 0) {
            round = 0;
        }
        customGauge.setEndValue(Math.round(critica));
        if (round <= buena) {
            customGauge.setPointStartColor(intColBueno);
            customGauge.setPointEndColor(intColBueno);
        } else if (round <= regular) {
            customGauge.setPointStartColor(intColRegular);
            customGauge.setPointEndColor(intColRegular);
        } else if (round <= mala) {
            customGauge.setPointStartColor(intColMalo);
            customGauge.setPointEndColor(intColMalo);
        } else if (round <= alta) {
            customGauge.setPointStartColor(intColMuyMalo);
            customGauge.setPointEndColor(intColMuyMalo);
        } else {
            customGauge.setPointStartColor(intColExtreMalo);
            customGauge.setPointEndColor(intColExtreMalo);
        }
        customGauge.setValue(Math.round(round));
    }*/

    private void mostrarValor(VWSensoresRegistroUndMedicionModel data) {
        float value = Float.parseFloat(data.getValorReportado());
        if (value <= 0) {
            value = 0.1f;
        }
        if (Float.parseFloat(data.getValorReportado()) >= data.getCritica()) {
            customGauge.setEndValue(Math.round(value * 1.2f));
        } else {
            customGauge.setEndValue(Math.round(data.getCritica()));
        }

        if (value <= data.getBuena()) {
            customGauge.setPointStartColor(intColBueno);
            customGauge.setPointEndColor(intColBueno);
        }
        if (value > data.getBuena() && value <= data.getRegular()) {
            customGauge.setPointStartColor(intColRegular);
            customGauge.setPointEndColor(intColRegular);
        }
        if (value > data.getRegular() && value <= data.getMala()) {
            customGauge.setPointStartColor(intColMalo);
            customGauge.setPointEndColor(intColMalo);
        }
        if (value > data.getMala() && value <= data.getAlta()) {
            customGauge.setPointStartColor(intColMuyMalo);
            customGauge.setPointEndColor(intColMuyMalo);
        }
        if (value > data.getAlta() && value <= data.getCritica()) {
            customGauge.setPointStartColor(intColExtreMalo);
            customGauge.setPointEndColor(intColExtreMalo);
        }
        customGauge.setValue(Math.round(value));
    }
}
