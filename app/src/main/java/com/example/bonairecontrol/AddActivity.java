package com.example.bonairecontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.InterfazApi.UsuarioMacApiCaller;
import com.example.bonairecontrol.ModelosApi.UsuarioMacModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {

    private EditText txt_data_mac_1, txt_data_mac_2, txt_data_mac_3, txt_data_mac_4, txt_data_mac_5, txt_data_mac_6;
    private ProgressBar progressBar4;

    Retrofit retrofit = null;

    Context context = this;
    
    String UrlBase = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        UrlBase = getString(R.string.url_base_api);

        retrofit = new Retrofit.Builder().baseUrl(UrlBase)
                .addConverterFactory(GsonConverterFactory.create()).build();

        //EditText

        txt_data_mac_1 = findViewById(R.id.txt_data_mac_1);
        txt_data_mac_2 = findViewById(R.id.txt_data_mac_2);
        txt_data_mac_3 = findViewById(R.id.txt_data_mac_3);
        txt_data_mac_4 = findViewById(R.id.txt_data_mac_4);
        txt_data_mac_5 = findViewById(R.id.txt_data_mac_5);
        txt_data_mac_6 = findViewById(R.id.txt_data_mac_6);

        //progress Bar

        progressBar4 = findViewById(R.id.progressBar4);

        //Navigation Bar

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.add);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext()
                                , ProfileActivy.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        if (sharPref.getBoolean("onlyBluetooth", false)){
            Toast.makeText(AddActivity.this, "No es posible abrir esta pantalla ya que se encuentra conectado via solo bluetooth", Toast.LENGTH_LONG).show();
            bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(0).getItemId());
        }
    }

    public void fncBtnSendDataMac(View view) {

        fncStartLoad();

        Boolean validador = fncRevisarData();
        String Mac = fncGetMac();
        
        if (!validador){
            return;
        }


        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        String email = sharPref.getString("userEmail", "");
        
        UsuarioMacModel usuarioMacModel = new UsuarioMacModel(Mac, email);
        
        UsuarioMacApiCaller apiCaller = retrofit.create(UsuarioMacApiCaller.class);
        Call<UsuarioMacModel> call = apiCaller.postUsuarioMac(usuarioMacModel);
        call.enqueue(new Callback<UsuarioMacModel>() {
            @Override
            public void onResponse(Call<UsuarioMacModel> call, Response<UsuarioMacModel> response) {
                if (response.isSuccessful()){
                    Toast.makeText(AddActivity.this, "Mac agregada con exito", Toast.LENGTH_SHORT).show();
                    fncCleanData();
                }else{

                    Toast.makeText(AddActivity.this, "La MAC especificada no es válida en la plataforma o ya esta asignada al usuario actual", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioMacModel> call, Throwable t) {
                Toast.makeText(AddActivity.this, "Error, por favor espere un momento e intente de nuevo", Toast.LENGTH_SHORT).show();
                fncCleanData();
            }
        });
        fncEndLoad();
    }

    private void fncEndLoad() {

        txt_data_mac_1.setEnabled(true);
        txt_data_mac_2.setEnabled(true);
        txt_data_mac_3.setEnabled(true);
        txt_data_mac_4.setEnabled(true);
        txt_data_mac_5.setEnabled(true);
        txt_data_mac_6.setEnabled(true);
        progressBar4.setVisibility(View.INVISIBLE);

    }

    private void fncStartLoad() {

        txt_data_mac_1.setEnabled(false);
        txt_data_mac_2.setEnabled(false);
        txt_data_mac_3.setEnabled(false);
        txt_data_mac_4.setEnabled(false);
        txt_data_mac_5.setEnabled(false);
        txt_data_mac_6.setEnabled(false);
        progressBar4.setVisibility(View.VISIBLE);

    }

    private void fncCleanData() {
        txt_data_mac_1.setText("");
        txt_data_mac_2.setText("");
        txt_data_mac_3.setText("");
        txt_data_mac_4.setText("");
        txt_data_mac_5.setText("");
        txt_data_mac_6.setText("");
        fncEndLoad();
    }

    private Boolean fncRevisarData() {

        boolean checker = true;

        if (txt_data_mac_1.equals("")){
            checker = false;
        }
        if (txt_data_mac_2.equals("")){
            checker = false;
        }
        if (txt_data_mac_3.equals("")){
            checker = false;
        }
        if (txt_data_mac_4.equals("")){
            checker = false;
        }
        if (txt_data_mac_5.equals("")){
            checker = false;
        }
        if (txt_data_mac_6.equals("")){
            checker = false;
        }

        if (!checker){
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String fncGetMac() {

        String Mac = "";

        Mac += txt_data_mac_1.getText().toString() + ":";
        Mac += txt_data_mac_2.getText().toString() + ":";
        Mac += txt_data_mac_3.getText().toString() + ":";
        Mac += txt_data_mac_4.getText().toString() + ":";
        Mac += txt_data_mac_5.getText().toString() + ":";
        Mac += txt_data_mac_6.getText().toString();

        return Mac;
    }

}