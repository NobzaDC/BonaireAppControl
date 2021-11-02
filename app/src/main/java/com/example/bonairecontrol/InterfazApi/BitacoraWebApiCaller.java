package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.BitacoraWebModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BitacoraWebApiCaller {

    @POST("api/BitacoraWebs")
    public Call<BitacoraWebModel> postBitacoraWeb(@Body BitacoraWebModel bitacoraModel);
}
