package com.example.bonairecontrol.configs_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.Controlador_adaptador.adapterDefaultDevices;
import com.example.bonairecontrol.Modelos.ModelDefaultDevices;
import com.example.bonairecontrol.R;
import com.example.bonairecontrol.bottomSheet.BottomSheetListDevices;
import com.example.bonairecontrol.bottomSheet.BottomSheetTypeDevice;

import java.util.ArrayList;
import java.util.List;

public class DefaultDevicesActivity extends AppCompatActivity implements BottomSheetListDevices.BottomSheetDevicesListener, BottomSheetTypeDevice.BottomSheetTypeDeviceListener  {

    private EditText et_equipo, et_tipo, et_nombre;
    private Button btn_add, btn_clean;

    private String address = null;
    private Context context = this;
    private SharedPreferences sharPref = null;
    private SharedPreferences.Editor editor = null;
    private List<ModelDefaultDevices> lstDevices;
    
    private adapterDefaultDevices adapter;
    private RecyclerView rv_default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_devices);

        sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        editor = sharPref.edit();
        address = sharPref.getString("BtMacString", "");

        et_equipo = findViewById(R.id.et_equipo_default_devices);
        et_tipo = findViewById(R.id.et_tipo_equipo_default_devices);
        et_nombre = findViewById(R.id.et_nombre_default_device);

        btn_add = findViewById(R.id.btn_add_default_devices);
        btn_clean = findViewById(R.id.btn_clean_default);
        
        rv_default = findViewById(R.id.rv_default_devices);
        rv_default.setLayoutManager(new GridLayoutManager(this, 2));

        et_equipo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    return;

                BottomSheetListDevices bottomSheet = new BottomSheetListDevices();
                bottomSheet.show(getSupportFragmentManager(), "bottomSheetEquipo");
            }
        });

        et_tipo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    return;

                BottomSheetTypeDevice bottomSheet = new BottomSheetTypeDevice();
                bottomSheet.show(getSupportFragmentManager(), "bottomSheetTipoEquipo");
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = et_nombre.getText().toString(), mac = et_equipo.getText().toString(), tipo = et_tipo.getText().toString();
                if (nombre.isEmpty() || mac.isEmpty() || tipo.isEmpty())
                    Toast.makeText(context, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
                else{
                    int num = lstDevices.size() + 1;
                    String infoDevice = mac + "%" + tipo + "%" + nombre;
                    editor.putString("DefaultDevices" + num, infoDevice);
                    editor.apply();
                    FncRevisarLista();
                    et_nombre.setText("");
                    et_equipo.setText("");
                    et_tipo.setText("");
                }

            }
        });

        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                int count = 1;
                while(flag){
                    String infoDevice = sharPref.getString("DefaultDevices" + count, "vacio");
                    Log.e("mLog", infoDevice);
                    if (infoDevice.equals("vacio")){
                        flag = false;
                    }else {
                        editor.putString("DefaultDevices" + count, "vacio");
                    }
                    count++;
                }
                editor.apply();
                lstDevices = new ArrayList<>();
                adapter = new adapterDefaultDevices(lstDevices, getBaseContext());
                rv_default.setAdapter(adapter);
            }
        });

        FncRevisarLista();
    }

    private void FncRevisarLista() {
        boolean flag = true;
        lstDevices = new ArrayList<>();
        int count = 1;
        while(flag){
            String infoDevice = sharPref.getString("DefaultDevices" + count, "vacio");
            if (infoDevice.equals("vacio"))
                flag = false;
            else {
                String[] info = infoDevice.split("%");
                 lstDevices.add(new ModelDefaultDevices(info[0], info[1], info[2]));
            }
            count++;
        }

        if (lstDevices.size() >= 1){
            adapter = new adapterDefaultDevices(lstDevices, this);
            rv_default.setAdapter(adapter);
        }else
            Toast.makeText(context, "No tiene equipos registrados", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFieldClicked(String mac) {
        et_equipo.setText(mac);
    }

    @Override
    public void onFieldTypeClicked(String mac_name) {
        et_tipo.setText(mac_name);
    }
}