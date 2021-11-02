package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.NivelesAmbienteModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NivelesAmbienteApiCaller {

    @GET("api/NivelesAmbientes")
    public Call<NivelesAmbienteModel> getDatos(@Query("parametroUI")String sensor);
}
