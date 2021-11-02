package com.example.bonairecontrol.configs_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.R;

public class PuntosDeCorteActivity extends AppCompatActivity {

    private EditText etTempOn, etTempOff, etPmOn, etPmOff;
    private RadioButton rbTemp1, rbTemp2, rbTemp3, rbTemp4, rbPm1, rbPm2, rbPm3, rbPm4;
    private Button btnSave;
    private String address = null;
    private Context context = this;
    private SharedPreferences sharPref = null;
    private SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_de_corte);
        init();
    }

    private void init() {

        //EditText
        etTempOn = findViewById(R.id.et_tmp_on);
        etTempOff = findViewById(R.id.et_tmp_off);
        etPmOn = findViewById(R.id.et_pm_on);
        etPmOff = findViewById(R.id.et_pm_off);

        //RadioButton
        rbTemp1 = findViewById(R.id.rb_t_fnc_1);
        rbTemp2 = findViewById(R.id.rb_t_fnc_2);
        rbTemp3 = findViewById(R.id.rb_t_fnc_3);
        rbTemp4 = findViewById(R.id.rb_t_fnc_4);

        rbPm1 = findViewById(R.id.rb_pm_fnc_1);
        rbPm2 = findViewById(R.id.rb_pm_fnc_2);
        rbPm3 = findViewById(R.id.rb_pm_fnc_3);
        rbPm4 = findViewById(R.id.rb_pm_fnc_4);

        //Button
        btnSave = findViewById(R.id.btn_save);

        //SharedPreferences
        sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        editor = sharPref.edit();

        //String
        address = sharPref.getString("BtMacString", "");

        //Integer
        String strTempOn = sharPref.getString(address + "_pc_tm_on", "vacio");
        String strTempOff = sharPref.getString(address + "_pc_tm_off", "vacio");
        String strPmOn = sharPref.getString(address + "_pc_pm_on", "vacio");
        String strPmOff = sharPref.getString(address + "_pc_pm_off", "vacio");

        //Set EditText
        if (!strTempOn.equals("vacio"))
            etTempOn.setText(strTempOn);
        if (!strTempOff.equals("vacio"))
            etTempOff.setText(strTempOff);
        if (!strPmOn.equals("vacio"))
            etPmOn.setText(strPmOn);
        if (!strPmOff.equals("vacio"))
            etPmOff.setText(strPmOff);

        //Boolean
        boolean blTmF1 = sharPref.getBoolean(address + "_pc_tm_f1", false);
        boolean blTmF2 = sharPref.getBoolean(address + "_pc_tm_f2", false);
        boolean blTmF3 = sharPref.getBoolean(address + "_pc_tm_f3", false);
        boolean blTmF4 = sharPref.getBoolean(address + "_pc_tm_f4", false);
        boolean blPmF1 = sharPref.getBoolean(address + "_pc_pm_f1", false);
        boolean blPmF2 = sharPref.getBoolean(address + "_pc_pm_f2", false);
        boolean blPmF3 = sharPref.getBoolean(address + "_pc_pm_f3", false);
        boolean blPmF4 = sharPref.getBoolean(address + "_pc_pm_f4", false);

        //Set RadioButton
        rbTemp1.setChecked(blTmF1);
        rbTemp2.setChecked(blTmF2);
        rbTemp3.setChecked(blTmF3);
        rbTemp4.setChecked(blTmF4);
        rbPm1.setChecked(blPmF1);
        rbPm2.setChecked(blPmF2);
        rbPm3.setChecked(blPmF3);
        rbPm4.setChecked(blPmF4);

        //OnClickListener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEtTempOn = etTempOn.getText().toString();
                String strEtTempOff = etTempOff.getText().toString();
                String strEtPmOn = etPmOn.getText().toString();
                String strEtPmOff = etPmOff.getText().toString();

                if (strEtTempOn.equals("") && strEtTempOff.equals("")
                        && strEtPmOn.equals("") && strEtPmOff.equals("")){ //Si no hay cambios registrados
                    Toast.makeText(context, "Debe llenar por lo menos un campo", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    //Banderas para guardar cambios de radio button
                    boolean flagTemp = false;
                    boolean flagPm = false;
                    if (!strEtTempOn.equals("")){
                        editor.putString(address + "_pc_tm_on", strEtTempOn);
                        flagTemp = true;
                    }
                    if (!strEtTempOff.equals("")){
                        editor.putString(address + "_pc_tm_off", strEtTempOff);
                        flagTemp = true;
                    }
                    if (!strEtPmOn.equals("")){
                        editor.putString(address + "_pc_pm_on", strEtPmOn);
                        flagPm = true;
                    }
                    if (!strEtPmOff.equals("")){
                        editor.putString(address + "_pc_pm_off", strEtPmOff);
                        flagPm = true;
                    }

                    //Cambios de radiobutton
                    if (flagTemp){

                        if (!rbTemp1.isChecked() && !rbTemp2.isChecked() && !rbTemp3.isChecked() && !rbTemp4.isChecked()){
                            Toast.makeText(context, "Seleccione que funcion activara y/o desactivara en temperatura", Toast.LENGTH_LONG).show();
                            return;
                        }

                        editor.putBoolean(address + "_pc_tm_f1", rbTemp1.isChecked());
                        editor.putBoolean(address + "_pc_tm_f2", rbTemp2.isChecked());
                        editor.putBoolean(address + "_pc_tm_f3", rbTemp3.isChecked());
                        editor.putBoolean(address + "_pc_tm_f4", rbTemp4.isChecked());

                    }
                    if (flagPm){

                        if (!rbPm1.isChecked() && !rbPm2.isChecked() && !rbPm3.isChecked() && !rbPm4.isChecked()){
                            Toast.makeText(context, "Seleccione que funcion activara y/o desactivara en material particulado", Toast.LENGTH_LONG).show();
                            return;
                        }

                        editor.putBoolean(address + "_pc_pm_f1", rbPm1.isChecked());
                        editor.putBoolean(address + "_pc_pm_f2", rbPm2.isChecked());
                        editor.putBoolean(address + "_pc_pm_f3", rbPm3.isChecked());
                        editor.putBoolean(address + "_pc_pm_f4", rbPm4.isChecked());
                    }
                    editor.apply();
                    Toast.makeText(context, "Cambios guardados con exito", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}