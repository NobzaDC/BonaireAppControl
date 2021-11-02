package com.example.bonairecontrol.configs_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.R;

public class DimerActivity extends AppCompatActivity {
    private String address = null;
    private Context context = this;
    SharedPreferences sharPref = null;
    SharedPreferences.Editor editor = null;
    private Switch swDimer;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimer);

        sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        editor = sharPref.edit();
        address = sharPref.getString("BtMacString", "");

        swDimer = findViewById(R.id.sw_dimer);
        btnSave = findViewById(R.id.btn_save);

        swDimer.setChecked(sharPref.getBoolean(address + "_dimer", false));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putBoolean(address + "_dimer", swDimer.isChecked());
                editor.apply();
                Toast.makeText(context, "Se guardaron las configuraciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}