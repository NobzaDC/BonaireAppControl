package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.SensorReporteModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SensorReporteApiCaller {

    @GET("api/SensorReportes")
    public Call<List<SensorReporteModel>> get_list_report_sensores_mac(@Query("mac")String mac);
}
