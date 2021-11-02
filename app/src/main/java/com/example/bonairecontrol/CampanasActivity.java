package com.example.bonairecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bonairecontrol.Modelos.ModelJsonBq600;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class CampanasActivity extends AppCompatActivity {

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address = null;
    Context context = this;

    private boolean flag = false, auto = true, blTmp1, blTmp2, blTmp3, blTmp4, blPm1, blPm2, blPm3, blPm4;
    private String strTmpOn, strTmpOff, strPmOn, strPmOff, nombreEquipo="";
    String strMessageReceived = "";

    private TextView tvTemperatura, tvHumedad, tvOzono, tvPm10, tvPm25, tvPm1, title_1, title_2, title_3, title_4, tvDescription, tvTitle;

    private Button toDeviceRigth, toDeviceLeft;

    private CardView rele_1, rele_2, rele_3, rele_4;
    private CheckBox cbRele1, cbRele2, cbRele3, cbRele4;
    SharedPreferences sharPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campanas);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;

                    //Log.e("mLog", "" + MyCaracter);
                    fncCrearString(MyCaracter);
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        sharPref = context.getSharedPreferences("BonairePref", context.MODE_PRIVATE);

        address = sharPref.getString("BtMacString", "");

        auto = sharPref.getBoolean(address + "_auto", false);

        tvTitle = findViewById(R.id.tv_title_campanas);

        nombreEquipo = sharPref.getString("BtNombreEquipo", "");
        tvTitle.setText(sharPref.getString(address + "_name", nombreEquipo));

        String device1 = sharPref.getString(address + "def_dev_eq1", "");
        String device2 = sharPref.getString(address + "def_dev_eq2", "");
        String device3 = sharPref.getString(address + "def_dev_eq3", "");

        rele_1 = findViewById(R.id.rele_1);
        rele_2 = findViewById(R.id.rele_2);
        rele_3 = findViewById(R.id.rele_3);
        rele_4 = findViewById(R.id.rele_4);

        title_1 = findViewById(R.id.title_card_1);
        title_2 = findViewById(R.id.title_card_2);
        title_3 = findViewById(R.id.title_card_3);
        title_4 = findViewById(R.id.title_card_4);

        cbRele1 = findViewById(R.id.cb_rele_1);
        cbRele2 = findViewById(R.id.cb_rele_2);
        cbRele3 = findViewById(R.id.cb_rele_3);
        cbRele4 = findViewById(R.id.cb_rele_4);

        tvTemperatura = findViewById(R.id.tv_temperatura);
        tvHumedad = findViewById(R.id.tv_humedad);
        tvOzono = findViewById(R.id.tv_ozono);
        tvPm10 = findViewById(R.id.tv_pm10);
        tvPm25 = findViewById(R.id.tv_pm25);
        tvPm1 = findViewById(R.id.tv_pm1);

        tvDescription = findViewById(R.id.txt_description_campamas);

        String rele1 = sharPref.getString(address + "_r1", "R_1");
        String rele2 = sharPref.getString(address + "_r2", "R_2");
        String rele3 = sharPref.getString(address + "_r3", "R_3");
        String rele4 = sharPref.getString(address + "_r4", "R_4");

        String rele1_opt1 = sharPref.getString(address + "_r1_opt1", "");
        String rele2_opt1 = sharPref.getString(address + "_r2_opt1", "");
        String rele3_opt1 = sharPref.getString(address + "_r3_opt1", "");
        String rele4_opt1 = sharPref.getString(address + "_r4_opt1", "");

        String rele1_opt2 = sharPref.getString(address + "_r1_opt2", "");
        String rele2_opt2 = sharPref.getString(address + "_r2_opt2", "");
        String rele3_opt2 = sharPref.getString(address + "_r3_opt2", "");
        String rele4_opt2 = sharPref.getString(address + "_r4_opt2", "");



        title_1.setText(sharPref.getString(address + "name_r1", "Dispositivo 1"));
        title_2.setText(sharPref.getString(address + "name_r2", "Dispositivo 2"));
        title_3.setText(sharPref.getString(address + "name_r3", "Dispositivo 3"));
        title_4.setText(sharPref.getString(address + "name_r4", "Dispositivo 4"));

        toDeviceLeft = findViewById(R.id.to_equipo_left);
        toDeviceRigth = findViewById(R.id.to_equipo_rigth);

        if (device1.equals("") && device2.equals("") && device3.equals("")) {
            toDeviceRigth.setVisibility(View.GONE);
            toDeviceLeft.setVisibility(View.GONE);
            tvDescription.setText("Conectado - Operación: " + (auto ? "AUTOMATICA" : "MANUAL"));
        }else{
            String macDevice1 = device1, nameDevice1 = "Equipo 1", macDevice2 = device2,
                    nameDevice2 = "Equipo 2", macDevice3 = device3, nameDevice3 = "Equipo 3";

            if (macDevice1.equals(address) || macDevice2.equals(address) || macDevice3.equals(address)){
                if (macDevice1.equals(address)){
                    tvDescription.setText("Conectado: " + nameDevice1 + " - Operación: " + (auto ? "AUTOMATICA" : "MANUAL"));
                    SetInfoButtons(macDevice2, nameDevice2, macDevice3, nameDevice3);
                }
                if (macDevice2.equals(address)){
                    tvDescription.setText("Conectado: " + nameDevice2 + " - Operación: " + (auto ? "AUTOMATICA" : "MANUAL"));
                    SetInfoButtons(macDevice1, nameDevice1, macDevice3, nameDevice3);
                }
                if (macDevice3.equals(address)){
                    tvDescription.setText("Conectado: " + nameDevice3 + " - Operación: " + (auto ? "AUTOMATICA" : "MANUAL"));
                    SetInfoButtons(macDevice1, nameDevice1, macDevice2, nameDevice2);
                }
            }else {
                toDeviceRigth.setVisibility(View.GONE);
                toDeviceLeft.setVisibility(View.GONE);
                tvDescription.setText("Conectado - Operación: " + (auto ? "AUTOMATICA" : "MANUAL"));
            }
        }

        rele_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRele1.isChecked()) {
                    fncEnviarDatos(rele1 + ":OFF");
                    if(!rele1_opt1.equals("")){
                        fncEnviarDatos(rele1_opt1 + ":OFF");
                        if(!rele1_opt2.equals("")){
                            fncEnviarDatos(rele1_opt2 + ":OFF");
                        }
                    }
                    cbRele1.setChecked(false);
                } else {
                    fncEnviarDatos(rele1 + ":ON");
                    if(!rele1_opt1.equals("")){
                        fncEnviarDatos(rele1_opt1 + ":ON");
                        if(!rele1_opt2.equals("")){
                            fncEnviarDatos(rele1_opt2 + ":ON");
                        }
                    }
                    cbRele1.setChecked(true);
                }
                Toast.makeText(context, "Comando enviado", Toast.LENGTH_SHORT).show();
            }
        });

        rele_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRele2.isChecked()) {
                    fncEnviarDatos(rele2 + ":OFF");
                    if(!rele2_opt1.equals("")){
                        fncEnviarDatos(rele2_opt1 + ":OFF");
                        if(!rele2_opt2.equals("")){
                            fncEnviarDatos(rele2_opt2 + ":OFF");
                        }
                    }
                    fncEnviarDatos(rele1 + ":OFF");
                    if(!rele1_opt1.equals("")){
                        fncEnviarDatos(rele1_opt1 + ":OFF");
                        if(!rele1_opt2.equals("")){
                            fncEnviarDatos(rele1_opt2 + ":OFF");
                        }
                    }
                    cbRele1.setChecked(false);
                    cbRele2.setChecked(false);
                } else {
                    fncEnviarDatos(rele2 + ":ON");
                    if(!rele2_opt1.equals("")){
                        fncEnviarDatos(rele2_opt1 + ":ON");
                        if(!rele2_opt2.equals("")){
                            fncEnviarDatos(rele2_opt2 + ":ON");
                        }
                    }
                    fncEnviarDatos(rele1 + ":ON");
                    if(!rele1_opt1.equals("")){
                        fncEnviarDatos(rele1_opt1 + ":ON");
                        if(!rele1_opt2.equals("")){
                            fncEnviarDatos(rele1_opt2 + ":ON");
                        }
                    }
                    cbRele1.setChecked(true);
                    cbRele2.setChecked(true);
                }
                Toast.makeText(context, "Comando enviado", Toast.LENGTH_SHORT).show();
            }
        });

        rele_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRele3.isChecked()) {
                    fncEnviarDatos(rele3 + ":OFF");
                    if(!rele3_opt1.equals("")){
                        fncEnviarDatos(rele3_opt1 + ":OFF");
                        if(!rele3_opt2.equals("")){
                            fncEnviarDatos(rele3_opt2 + ":OFF");
                        }
                    }
                    cbRele3.setChecked(false);
                } else {
                    fncEnviarDatos(rele3 + ":ON");
                    if(!rele3_opt1.equals("")){
                        fncEnviarDatos(rele3_opt1 + ":ON");
                        if(!rele3_opt2.equals("")){
                            fncEnviarDatos(rele3_opt2 + ":ON");
                        }
                    }
                    cbRele3.setChecked(true);
                }
                Toast.makeText(context, "Comando enviado", Toast.LENGTH_SHORT).show();
            }
        });

        rele_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRele4.isChecked()) {
                    fncEnviarDatos(rele4 + ":OFF");
                    if(!rele4_opt1.equals("")){
                        fncEnviarDatos(rele4_opt1 + ":OFF");
                        if(!rele4_opt2.equals("")){
                            fncEnviarDatos(rele4_opt2 + ":OFF");
                        }
                    }
                    cbRele4.setChecked(false);
                } else {
                    fncEnviarDatos(rele4 + ":ON");
                    if(!rele4_opt1.equals("")){
                        fncEnviarDatos(rele4_opt1 + ":ON");
                        if(!rele4_opt2.equals("")){
                            fncEnviarDatos(rele4_opt2 + ":ON");
                        }
                    }
                    cbRele4.setChecked(true);
                }
                Toast.makeText(context, "Comando enviado", Toast.LENGTH_SHORT).show();
            }
        });
        ValidarAuto();
    }

    private void ValidarAuto() {

        strTmpOn = sharPref.getString(address + "_pc_tm_on", "vacio");
        strTmpOff = sharPref.getString(address + "_pc_tm_off", "vacio");
        strPmOn = sharPref.getString(address + "_pc_pm_on", "vacio");
        strPmOff = sharPref.getString(address + "_pc_pm_off", "vacio");

        if (strTmpOn.equals("vacio") && strTmpOff.equals("vacio") && strPmOn.equals("vacio") && strPmOff.equals("vacio")){
            Toast.makeText(context, "No se encontraron configuraciones automaticas guardadas", Toast.LENGTH_SHORT).show();
        }else {
            blTmp1 = sharPref.getBoolean(address + "_pc_tm_f1", false);
            blTmp2 = sharPref.getBoolean(address + "_pc_tm_f2", false);
            blTmp3 = sharPref.getBoolean(address + "_pc_tm_f3", false);
            blTmp4 = sharPref.getBoolean(address + "_pc_tm_f4", false);
            blPm1 = sharPref.getBoolean(address + "_pc_pm_f1", false);
            blPm2 = sharPref.getBoolean(address + "_pc_pm_f2", false);
            blPm3 = sharPref.getBoolean(address + "_pc_pm_f3", false);
            blPm4 = sharPref.getBoolean(address + "_pc_pm_f4", false);
        }
    }

    private void SetInfoButtons(String macDeviceLeft, String nameDeviceLeft, String macDeviceRight, String nameDeviceRight) {
        toDeviceLeft.setText(nameDeviceLeft);
        toDeviceRigth.setText(nameDeviceRight);

        SharedPreferences.Editor editor = sharPref.edit();

        toDeviceLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("transacBtToActivity", 4);
                editor.putString("BtMacString", macDeviceLeft);
                editor.putString("BtNombreEquipo", nameDeviceLeft);
                editor.apply();
                Intent intent = new Intent(context, transacBtDeviceListActivity.class);
                Toast.makeText(context, "Probando conexión con el equipo", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        toDeviceRigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("transacBtToActivity", 4);
                editor.putString("BtMacString", macDeviceRight);
                editor.putString("BtNombreEquipo", nameDeviceRight);
                editor.apply();
                Intent intent = new Intent(context, transacBtDeviceListActivity.class);
                Toast.makeText(context, "Probando conexión con el equipo", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
        ValidarAuto();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //crea un conexion de salida segura para el dispositivo usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establece la conexión con el socket Bluetooth.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
                Log.e("mLog", e.toString());
            } catch (IOException e2) {
            }
        }
        Log.e("mLog", btSocket.isConnected() + "");
        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();
        MyConexionBT.write("BT_CONNECTED/");
    }

    @Override
    public void onPause() {
        super.onPause();
        try { // Cuando se sale de la aplicación esta parte permite que no se deje abierto el socket
            btSocket.close();
        } catch (IOException e2) {
            Toast.makeText(context, "No se pudo cerrar la conexion bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    private void fncEnviarDatos(String strMensaje) {
        try {
            MyConexionBT.write(strMensaje + "/");
        } catch (Exception e) {
            Toast.makeText(this, "Falla al enviar los datos, error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] byte_in = new byte[1];
            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    mmInStream.read(byte_in);
                    char ch = (char) byte_in[0];
                    bluetoothIn.obtainMessage(handlerState, ch).sendToTarget();
                } catch (IOException e) {
                    //Toast.makeText(getBaseContext(), "No fue posible recibir la informacion", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }

        //Envio de trama
        public boolean write(String input) {
            try {
                mmOutStream.write(input.getBytes());
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    private void fncCrearString(char myCaracter) {
        if (myCaracter == '{') {
            flag = true;
        }
        if (flag) {
            strMessageReceived += myCaracter;
        }
        if (myCaracter == '}') {
            flag = false;
            fncActualizarDatos(strMessageReceived);
            Log.e("mLog", strMessageReceived);
            strMessageReceived = "";
        }
    }

    private void fncActualizarDatos(String json) {
        try{
            Gson gson = new Gson();
            ModelJsonBq600 data = gson.fromJson(json.trim(), ModelJsonBq600.class);

            tvTemperatura.setText(data.getT());
            tvHumedad.setText(data.getH());
            tvOzono.setText(data.getO3());
            tvPm10.setText(data.getPM10());
            tvPm25.setText(data.getPM25());
            tvPm1.setText(data.getPM1());

            String fnc_1 = sharPref.getString(address + "_r1", "R_1");
            String fnc_2 = sharPref.getString(address + "_r2", "R_2");
            String fnc_3 = sharPref.getString(address + "_r3", "R_3");
            String fnc_4 = sharPref.getString(address + "_r4", "R_4");

            if (auto){
                if (Integer.valueOf(data.getT()) > Integer.valueOf(strTmpOn)){
                    if (!strTmpOn.equals("vacio")){
                        if (blTmp1){
                            fncEnviarDatos(fnc_1 + ":ON");
                            cbRele1.setChecked(true);
                        }
                        if (blTmp2){
                            fncEnviarDatos(fnc_2 + ":ON");
                            cbRele2.setChecked(true);
                        }
                        if (blTmp3){
                            fncEnviarDatos(fnc_3 + ":ON");
                            cbRele3.setChecked(true);
                        }
                        if (blTmp4){
                            fncEnviarDatos(fnc_4 + ":ON");
                            cbRele4.setChecked(true);
                        }
                    }
                }

                if (Integer.valueOf(strTmpOff) > Integer.valueOf(data.getT())){
                    if (!strTmpOff.equals("vacio")){
                        if (blTmp1){
                            fncEnviarDatos(fnc_1 + ":OFF");
                            cbRele1.setChecked(false);
                        }
                        if (blTmp2){
                            fncEnviarDatos(fnc_2 + ":OFF");
                            cbRele2.setChecked(false);
                        }
                        if (blTmp3){
                            fncEnviarDatos(fnc_3 + ":OFF");
                            cbRele3.setChecked(false);
                        }
                        if (blTmp4){
                            fncEnviarDatos(fnc_4 + ":OFF");
                            cbRele4.setChecked(false);
                        }
                    }
                }
                if (Integer.valueOf(data.getPM10()) > Integer.valueOf(strPmOn)){
                    if (!strPmOn.equals("vacio")){
                        if (blPm1){
                            fncEnviarDatos(fnc_1 + ":ON");
                            cbRele1.setChecked(true);
                        }
                        if (blPm2){
                            fncEnviarDatos(fnc_2 + ":ON");
                            cbRele2.setChecked(true);
                        }
                        if (blPm3){
                            fncEnviarDatos(fnc_3 + ":ON");
                            cbRele3.setChecked(true);
                        }
                        if (blPm4){
                            fncEnviarDatos(fnc_4 + ":ON");
                            cbRele4.setChecked(true);
                        }
                    }
                }
                if (Integer.valueOf(strPmOff) > Integer.valueOf(data.getPM10())){
                    if (!strPmOff.equals("vacio")){
                        if (blPm1){
                            fncEnviarDatos(fnc_1 + ":OFF");
                            cbRele1.setChecked(false);
                        }
                        if (blPm2){
                            fncEnviarDatos(fnc_2 + ":OFF");
                            cbRele2.setChecked(false);
                        }
                        if (blPm3){
                            fncEnviarDatos(fnc_3 + ":OFF");
                            cbRele3.setChecked(false);
                        }
                        if (blPm4){
                            fncEnviarDatos(fnc_4 + ":OFF");
                            cbRele4.setChecked(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            strMessageReceived = "";
            json = "";
        }
    }
}