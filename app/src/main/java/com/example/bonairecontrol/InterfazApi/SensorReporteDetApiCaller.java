package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.SensorReporteDetModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SensorReporteDetApiCaller {

    @GET("api/SensorReporteDet")
    public Call<List<SensorReporteDetModel>> getDatos(@Query("mac")String strNumMac, @Query("sensor")String strNombreSensor);
}
