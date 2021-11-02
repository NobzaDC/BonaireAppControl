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

public class AutomaticActivity extends AppCompatActivity {

    private Switch swAuto;
    private Button btnSave;

    private String address = null;
    private Context context = this;
    private SharedPreferences sharPref = null;
    private SharedPreferences.Editor editor = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autimatic);

        sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        editor = sharPref.edit();
        address = sharPref.getString("BtMacString", "");
        
        swAuto = findViewById(R.id.sw_automatic);
        btnSave = findViewById(R.id.btn_save);

        swAuto.setChecked(sharPref.getBoolean(address + "_auto", false));
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(address + "_auto", swAuto.isChecked());
                Toast.makeText(context, "Se guardaron las configuraciones correctamente.", Toast.LENGTH_SHORT).show();
                editor.apply();
            }
        });
        
    }
}