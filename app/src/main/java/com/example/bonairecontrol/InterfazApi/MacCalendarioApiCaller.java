package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.MacCalendarioModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MacCalendarioApiCaller {

    @POST("api/MacCalendarios")
    public Call<MacCalendarioModel> postMacCalendario(@Body MacCalendarioModel macCalendarioModel);

    @PUT("api/MacCalendarios/{id}")
    public Call<MacCalendarioModel> putMacCalendario(@Path("id")String id, @Body MacCalendarioModel macCalendarioModel);

}
