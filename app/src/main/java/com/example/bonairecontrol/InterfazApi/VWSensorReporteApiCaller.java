package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.VWSensorReporteModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VWSensorReporteApiCaller {

    @GET("api/VWSensorReportes")
    public Call<List<VWSensorReporteModel>> GetListaResportes(@Query("mac")String strNumMac);
}
