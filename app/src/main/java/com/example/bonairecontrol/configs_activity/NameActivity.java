package com.example.bonairecontrol.configs_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.R;

public class NameActivity extends AppCompatActivity {
    private String address = null;
    private Context context = this;
    SharedPreferences sharPref = null;
    SharedPreferences.Editor editor = null;

    private Button btnSave;
    private EditText etNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        editor = sharPref.edit();
        address = sharPref.getString("BtMacString", "");

        etNombre = findViewById(R.id.et_nombre);
        btnSave = findViewById(R.id.btn_save);

        etNombre.setText(sharPref.getString(address + "_name", ""));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (etNombre.getText().toString().equals("")){
                        Toast.makeText(context, "Se requiere de un nombre", Toast.LENGTH_SHORT).show();
                    }else {
                        editor.putString(address + "_name", etNombre.getText().toString());
                        editor.apply();
                        Toast.makeText(context, "Cambios guardados con exito", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
}