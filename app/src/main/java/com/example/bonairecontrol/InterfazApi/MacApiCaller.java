package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.MacModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MacApiCaller {

    @GET("api/Macs")
    public Call<MacModel> get_info_mac(@Query("mac")String mac);
}
