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

public class RelesActivity extends AppCompatActivity {
    private String address = null;
    private Context context = this;
    SharedPreferences sharPref = null;
    SharedPreferences.Editor editor = null;

    private Button btnRele1, btnRele2, btnRele3, btnRele4, btnSave;
    private EditText rele1, rele1_opt1, rele1_opt2,
            rele2, rele2_opt1, rele2_opt2,
            rele3, rele3_opt1, rele3_opt2,
            rele4, rele4_opt1, rele4_opt2;
    private EditText nombre_rele1, nombre_rele2, nombre_rele3, nombre_rele4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reles);

        sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        editor = sharPref.edit();
        address = sharPref.getString("BtMacString", "");

        btnRele1 = findViewById(R.id.btn_rele1);
        btnRele2 = findViewById(R.id.btn_rele2);
        btnRele3 = findViewById(R.id.btn_rele3);
        btnRele4 = findViewById(R.id.btn_rele4);

        btnSave = findViewById(R.id.btn_save);

        rele1 = findViewById(R.id.et_function1);
        rele1_opt1 = findViewById(R.id.et_function_1_opt_1);
        rele1_opt2 = findViewById(R.id.et_function_1_opt_2);

        rele2 = findViewById(R.id.et_function2);
        rele2_opt1 = findViewById(R.id.et_function_2_opt_1);
        rele2_opt2 = findViewById(R.id.et_function_2_opt_2);

        rele3 = findViewById(R.id.et_function3);
        rele3_opt1 = findViewById(R.id.et_function_3_opt_1);
        rele3_opt2 = findViewById(R.id.et_function_3_opt_2);

        rele4 = findViewById(R.id.et_function4);
        rele4_opt1 = findViewById(R.id.et_function_4_opt_1);
        rele4_opt2 = findViewById(R.id.et_function_4_opt_2);

        nombre_rele1 = findViewById(R.id.et_nombre_function1);
        nombre_rele2 = findViewById(R.id.et_nombre_function2);
        nombre_rele3 = findViewById(R.id.et_nombre_function3);
        nombre_rele4 = findViewById(R.id.et_nombre_function4);

        rele1.setText(sharPref.getString(address + "_r1", "R_1"));
        rele2.setText(sharPref.getString(address + "_r2", "R_2"));
        rele3.setText(sharPref.getString(address + "_r3", "R_3"));
        rele4.setText(sharPref.getString(address + "_r4", "R_4"));

        nombre_rele1.setText(sharPref.getString(address + "name_r1", "R_1"));
        nombre_rele2.setText(sharPref.getString(address + "name_r2", "R_2"));
        nombre_rele3.setText(sharPref.getString(address + "name_r3", "R_3"));
        nombre_rele4.setText(sharPref.getString(address + "name_r4", "R_4"));

        btnRele1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rele1.isFocused()){
                    rele1.setText("R_1");
                }else if (rele2.isFocused()){
                    rele2.setText("R_1");
                }else if (rele3.isFocused()){
                    rele3.setText("R_1");
                }else if (rele4.isFocused()){
                    rele4.setText("R_1");
                }else {
                    Toast.makeText(context, "Seleccione un espacio para escribir el comando.", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnRele2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rele1.isFocused()){
                    rele1.setText("R_2");
                }else if (rele2.isFocused()){
                    rele2.setText("R_2");
                }else if (rele3.isFocused()){
                    rele3.setText("R_2");
                }else if (rele4.isFocused()){
                    rele4.setText("R_2");
                }else {
                    Toast.makeText(context, "Seleccione un espacio para escribir el comando.", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnRele3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rele1.isFocused()){
                    rele1.setText("R_3");
                }else if (rele2.isFocused()){
                    rele2.setText("R_3");
                }else if (rele3.isFocused()){
                    rele3.setText("R_3");
                }else if (rele4.isFocused()){
                    rele4.setText("R_3");
                }else {
                    Toast.makeText(context, "Seleccione un espacio para escribir el comando.", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnRele4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rele1.isFocused()){
                    rele1.setText("R_4");
                }else if (rele2.isFocused()){
                    rele2.setText("R_4");
                }else if (rele3.isFocused()){
                    rele3.setText("R_4");
                }else if (rele4.isFocused()){
                    rele4.setText("R_4");
                }else {
                    Toast.makeText(context, "Seleccione un espacio para escribir el comando.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString((address + "_r1"), !rele1.getText().toString().equals("") ? rele1.getText().toString() : "R_1");
                editor.putString((address + "_r2"), !rele2.getText().toString().equals("") ? rele2.getText().toString() : "R_2");
                editor.putString((address + "_r3"), !rele3.getText().toString().equals("") ? rele3.getText().toString() : "R_3");
                editor.putString((address + "_r4"), !rele4.getText().toString().equals("") ? rele4.getText().toString() : "R_4");
                editor.putString((address + "name_r1"), !nombre_rele1.getText().toString().equals("") ? nombre_rele1.getText().toString() : "Dispositivo 1");
                editor.putString((address + "name_r2"), !nombre_rele2.getText().toString().equals("") ? nombre_rele2.getText().toString() : "Dispositivo 2");
                editor.putString((address + "name_r3"), !nombre_rele3.getText().toString().equals("") ? nombre_rele3.getText().toString() : "Dispositivo 3");
                editor.putString((address + "name_r4"), !nombre_rele4.getText().toString().equals("") ? nombre_rele4.getText().toString() : "Dispositivo 4");

                if(!rele1_opt1.getText().toString().equals("")){
                    editor.putString((address + "_r1_opt1"), rele1_opt1.getText().toString());
                    if(!rele1_opt2.getText().toString().equals("")){
                        editor.putString((address + "_r1_opt2"), rele1_opt2.getText().toString());
                    }
                }

                if(!rele2_opt1.getText().toString().equals("")){
                    editor.putString((address + "_r2_opt1"), rele2_opt1.getText().toString());
                    if(!rele2_opt2.getText().toString().equals("")){
                        editor.putString((address + "_r2_opt2"), rele2_opt2.getText().toString());
                    }
                }

                if(!rele3_opt1.getText().toString().equals("")){
                    editor.putString((address + "_r3_opt1"), rele3_opt1.getText().toString());
                    if(!rele3_opt2.getText().toString().equals("")){
                        editor.putString((address + "_r3_opt2"), rele3_opt2.getText().toString());
                    }
                }

                if(!rele4_opt1.getText().toString().equals("")){
                    editor.putString((address + "_r4_opt1"), rele4_opt1.getText().toString());
                    if(!rele4_opt2.getText().toString().equals("")){
                        editor.putString((address + "_r4_opt2"), rele4_opt2.getText().toString());
                    }
                }

                editor.apply();
                Toast.makeText(context, "Las configuraciones se guardaron correctamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}