package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.VWEquiposPorUsuarioModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VWEquiposPorUsuarioCaller {

    @GET("api/VWEquiposPorUsuarios")
    public Call<List<VWEquiposPorUsuarioModel>> get_data_equipos(@Query("email")String email);
}
