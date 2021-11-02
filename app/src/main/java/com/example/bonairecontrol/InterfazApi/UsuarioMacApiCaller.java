package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.UsuarioMacModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UsuarioMacApiCaller {

    //api/BitacoraWebs

    @GET("api/UsuarioMacs")
    public Call<List<UsuarioMacModel>> get_mac_user_lst(@Query("data")String email);

    @POST("api/UsuarioMacs")
    public Call<UsuarioMacModel> postUsuarioMac(@Body UsuarioMacModel usuarioMacModel);

}
