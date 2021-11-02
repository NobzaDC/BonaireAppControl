package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.SensorModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SensorApiCaller {
    @GET("api/Sensors")
    public Call<SensorModel> get_sensor_detallado(@Query("parametroUI")String parametroUI);

}
