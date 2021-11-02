package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.UsuarioModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsuarioApiCaller {

    @GET("api/Usuarios/{id}")
    public Call<UsuarioModel> get_by_id(@Path("id")String id);

    @GET("api/Usuarios")
    public Call<UsuarioModel> get_email_user(@Query("data")String data);

    @PUT("api/Usuarios/{id}")
    public Call<UsuarioModel> put_token(@Path("id") String id, @Body UsuarioModel model);

}
