package com.example.bonairecontrol.configs_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bonairecontrol.R;

public class BqhConfigActivity extends AppCompatActivity {
    private String address = null;
    private Context context = this;
    SharedPreferences sharPref = null;
    SharedPreferences.Editor editor = null;

    private Button btnRele1, btnRele2, btnRele3, btnRele4, btnRele5, btnRele6, btnSave;
    private EditText eBase, eOpt1, eOpt2, ehBase, ehOpt1, ehOpt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bqh_config);


        sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        editor = sharPref.edit();
        address = sharPref.getString("BtMacString", "");

        btnRele1 = findViewById(R.id.btn_rele1);
        btnRele2 = findViewById(R.id.btn_rele2);
        btnRele3 = findViewById(R.id.btn_rele3);
        btnRele4 = findViewById(R.id.btn_rele4);
        btnRele5 = findViewById(R.id.btn_rele5);
        btnRele6 = findViewById(R.id.btn_rele6);

        eBase = findViewById(R.id.et_e_base);
        eOpt1 = findViewById(R.id.et_e_opt_1);
        eOpt2 = findViewById(R.id.et_e_opt_2);
        ehBase = findViewById(R.id.et_eh_base);
        ehOpt1 = findViewById(R.id.et_eh_opt_1);
        ehOpt2 = findViewById(R.id.et_eh_opt_2);

        btnSave = findViewById(R.id.btn_save);

        eBase.setText(sharPref.getString(address + "e_base", "R_1"));
        ehBase.setText(sharPref.getString(address + "eh_base", "R_2"));
        eOpt1.setText(sharPref.getString(address + "e_opt1", ""));
        ehOpt1.setText(sharPref.getString(address + "eh_opt1", ""));
        eOpt2.setText(sharPref.getString(address + "e_opt2", ""));
        ehOpt2.setText(sharPref.getString(address + "eh_opt2", ""));


        btnRele1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue("R_1");
            }
        });
        btnRele2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setValue("R_2");
            }
        });
        btnRele3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue("R_3");
            }
        });
        btnRele4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue("R_4");
            }
        });
        btnRele5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue("R_5");
            }
        });
        btnRele6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue("R_6");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(eBase.getText().toString().equals("") || ehBase.getText().toString().equals(""))
                    Toast.makeText(context, "Debe seleccionar un mensaje base", Toast.LENGTH_SHORT).show();

                editor.putString((address + "e_base"), !eBase.getText().toString().equals("") ? eBase.getText().toString() : "R_1");
                editor.putString((address + "eh_base"), !ehBase.getText().toString().equals("") ? ehBase.getText().toString() : "R_2");

                if(!eOpt1.getText().toString().equals("")){
                    editor.putString((address + "e_opt1"), eOpt1.getText().toString());
                    if(!eOpt2.getText().toString().equals("")) editor.putString((address + "e_opt2"), eOpt2.getText().toString());
                }

                if(!ehOpt1.getText().toString().equals("")){
                    editor.putString((address + "eh_opt1"), ehOpt1.getText().toString());
                    if(!ehOpt2.getText().toString().equals("")) editor.putString((address + "eh_opt2"), ehOpt2.getText().toString());
                }

                editor.apply();
                Toast.makeText(context, "Las configuraciones se guardaron correctamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setValue(String value){
        if (eBase.isFocused()) eBase.setText(value);
        else if(eOpt1.isFocused()) eOpt1.setText(value);
        else if(eOpt2.isFocused()) eOpt2.setText(value);
        else if(ehBase.isFocused()) ehBase.setText(value);
        else if(ehOpt1.isFocused()) ehOpt1.setText(value);
        else if(ehOpt2.isFocused()) ehOpt2.setText(value);
        else Toast.makeText(context, "Seleccione un espacio para escribir el comando.", Toast.LENGTH_LONG).show();
    }
}