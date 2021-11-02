package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.NivelesAmbientexLugarModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NivelesAmbientexLugarApiCaller {
    @GET("api/NivelesAmbientexLugars")
    public Call<NivelesAmbientexLugarModel> getDatos(@Query("parametroUI")String sensor, @Query("LugarInstalacion")int lugarInstalacion);
}
