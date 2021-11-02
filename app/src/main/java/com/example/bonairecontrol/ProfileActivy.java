package com.example.bonairecontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivy extends AppCompatActivity {

    Context context = this;
    TextView txt_data_nombre_usuario, txt_data_nombre, txt_version;
    String version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txt_data_nombre_usuario = (TextView)findViewById(R.id.txt_data_nombre_usuario);
        txt_data_nombre = (TextView)findViewById(R.id.txt_data_nombre);
        txt_version = (TextView)findViewById(R.id.txt_version);

        version = getString(R.string.version);

        txt_version.setText("V " + version);

        //Navigation Bar

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        return true;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext()
                                ,AddActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        if (sharPref.getBoolean("onlyBluetooth", false)){
            Toast.makeText(ProfileActivy.this, "No es posible abrir esta pantalla ya que se encuentra conectado via solo bluetooth", Toast.LENGTH_LONG).show();
            bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(0).getItemId());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent HomePage = new Intent(this, HomeActivity.class);
        startActivity(HomePage);
    }

    private void intentConsole() {
        /*Intent intent = new Intent(this, manual_activity.class);
        startActivity(intent);*/

        Toast.makeText(context, "In construction...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        String Nombre = sharPref.getString("userNombres", "-");
        String Apellidos = sharPref.getString("userApellidos", "-");
        String Correo = sharPref.getString("userEmail", "-");

        txt_data_nombre_usuario.setText(Nombre + " " + Apellidos);
        txt_data_nombre.setText(Correo);

    }

    public void fncBtnConfig(View view){
        Intent intent = new Intent(this, config_devices_activity.class);
        startActivity(intent);
    }

    public void fncBtnCerrarSesion(View view){

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        editor.putBoolean("remember", false);
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}