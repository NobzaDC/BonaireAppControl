package com.example.bonairecontrol.InterfazApi;

import com.example.bonairecontrol.ModelosApi.MensajesMqttModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MensajesMqttApiCaller {

    @POST("api/MensajesMQTTs")
    public Call<MensajesMqttModel> post_send_mensaje(@Body MensajesMqttModel mensajesMqttModel);

}
