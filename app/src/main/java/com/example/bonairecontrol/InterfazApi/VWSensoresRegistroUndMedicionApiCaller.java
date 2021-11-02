package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.VWSensoresRegistroUndMedicionModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VWSensoresRegistroUndMedicionApiCaller {


    @GET("api/VWSensoresRegistroUndMedicions")
    public Call<List<VWSensoresRegistroUndMedicionModel>> getDatos(@Query("mac")String strNumMac, @Query("sensor")String strNombreSensor);
}
