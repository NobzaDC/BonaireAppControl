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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.Controlador_adaptador.adapterDefaultDevices;
import com.example.bonairecontrol.Modelos.ModelDefaultDevices;
import com.example.bonairecontrol.Modelos.ModelJsonBq600;
import com.example.bonairecontrol.Modelos.ModelListDevices;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeshumidacadorActivity extends AppCompatActivity {
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private String address = null;
    private CardView cvVentilacion, cvEliminacionHumo, cvApagadoTotal;
    Context context = this;

    private SharedPreferences sharPref;
    private SharedPreferences.Editor editor = null;

    private boolean flag = false, dimer, auto = true, blTmp1, blTmp2, blTmp3, blTmp4, blPm1, blPm2, blPm3, blPm4;
    private String strTmpOn, strTmpOff, strPmOn, strPmOff;

    List<ModelListDevices> listDevices = new ArrayList<>();
    String strMessageReceived = "", nombreEquipo="";
    String dataR1, dataR2, dataR3, dataR4, dataPwm, dataServo,
            dataTemperatura, dataHumedad, dataOzono, dataMac,
            dataIp, second, minute, hour, time;

    private TextView txt_nombre_equipo_deshumidificador, tvTemperatura, tvPm10, tvPm25, tvPm1, tvDescription;
    private RecyclerView recyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deshumidacador);

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
            sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
            editor = sharPref.edit();
            txt_nombre_equipo_deshumidificador = (TextView) findViewById(R.id.txt_nombre_equipo_deshumidificador);
            cvVentilacion = findViewById(R.id.cv_ventilacion);
            cvEliminacionHumo = findViewById(R.id.cv_eliminacion_humo);
            cvApagadoTotal = findViewById(R.id.cv_apagado_total);
            recyclerView = findViewById(R.id.rv_default_devices_deshumidificador);

            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            address = sharPref.getString("BtMacString", "");
            auto = sharPref.getBoolean(address + "_auto", false);

            tvTemperatura = findViewById(R.id.tv_temperatura);
            //tvHumedad = findViewById(R.id.tv_humedad);
            //tvOzono = findViewById(R.id.tv_ozono);
            tvPm10 = findViewById(R.id.tv_pm10_des);
            tvPm25 = findViewById(R.id.tv_pm25_des);
            tvPm1 = findViewById(R.id.tv_pm1_des);

            tvDescription = findViewById(R.id.txt_description_eliminador);

            /*swBegin = (Switch) findViewById(R.id.switch_begin_deshumidificador);
            swStop = (Switch) findViewById(R.id.switch_stop_deshumidificador);
            swBeginVentilacion = (Switch) findViewById(R.id.switch_begin_ventilacion_deshumidificador);
            btnExec = (Button) findViewById(R.id.btn_exec_action_deshumidificador);*/

            nombreEquipo = sharPref.getString("BtNombreEquipo", "");
            txt_nombre_equipo_deshumidificador.setText(sharPref.getString(address + "_name", nombreEquipo));

            dimer = sharPref.getBoolean(address + "_dimer", false);

            String eBase = sharPref.getString(address + "e_base", "R_1");
            String eOpt1 = sharPref.getString(address + "e_opt1", "");
            String eOpt2 = sharPref.getString(address + "e_opt2", "");
            String ehBase = sharPref.getString(address + "eh_base", "R_2");
            String ehOpt1 = sharPref.getString(address + "eh_opt1", "");
            String ehOpt2 = sharPref.getString(address + "eh_opt2", "");

            cvVentilacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fncEnviarDatos(eBase + ":ON");

                    if(!eOpt1.equals("")){
                        fncEnviarDatos(eOpt1 + ":ON");
                        if(!eOpt2.equals("")) fncEnviarDatos(eOpt2 + ":ON");
                    }
                    Toast.makeText(context, "comando enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                }
            });

            cvEliminacionHumo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fncEnviarDatos(ehBase + ":ON");

                    if(!ehOpt1.equals("")){
                        fncEnviarDatos(ehOpt1 + ":ON");
                        if(!ehOpt2.equals("")) fncEnviarDatos(ehOpt2 + ":ON");
                    }

                    Toast.makeText(context, "comando enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                }
            });
        cvApagadoTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fncEnviarDatos( eBase + ":OFF");
                    fncEnviarDatos( ehBase + ":OFF");
                    fncEnviarDatos( eOpt1 + ":OFF");
                    fncEnviarDatos( eOpt2 + ":OFF");
                    fncEnviarDatos( ehOpt1 + ":OFF");
                    fncEnviarDatos( ehOpt2 + ":OFF");

                    Toast.makeText(context, "comando enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                }
            });

            /*swBegin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swBegin.isChecked()) {
                        swStop.setChecked(false);

                    }
                }
            });
            swStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swStop.isChecked()) {
                        swBegin.setChecked(false);
                    }
                }
            });
            swBeginVentilacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swStop.isChecked()) {
                        swBegin.setChecked(false);
                    }
                }
            });

            btnExec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = "";
                    if (swBegin.isChecked()) {
                        message = "B";
                    }
                    if (swStop.isChecked()) {
                        message = "S";
                    }
                    if (swBeginVentilacion.isChecked()) {
                        message = "V";
                    }

                    if (message.equals("")) {
                        Toast.makeText(context, "debe seleccionar una de las opciones", Toast.LENGTH_SHORT).show();
                    } else {
                        switch (message) {
                            case "B":
                                fncEnviarDatos("DESINFECCION_BEGIN");
                                fncEnviarDatos("R_1:ON");
                                Toast.makeText(context, "comando enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                                break;
                            case "V":
                                fncEnviarDatos("PURIFICACION_BEGIN");
                                fncEnviarDatos("R_1:OFF");
                                Toast.makeText(context, "comando enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                                break;
                            case "S":
                                fncEnviarDatos("PURIFICACION_STOP");
                                fncEnviarDatos("DESINFECCION_STOP");
                                fncEnviarDatos("R_1:OFF");
                                Toast.makeText(context, "comando enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        message = "";
                    }

                    if (swBegin.isChecked()) {
                        message = "P";
                    }
                    if (swStop.isChecked()) {
                        message = "D";
                    }
                    swBegin.setChecked(false);
                    swBeginVentilacion.setChecked(false);
                    swStop.setChecked(false);
                }
            });*/
        FncCargarListaEquipos();
        ValidarAuto();

    }

    private void FncCargarListaEquipos() {
        boolean flag = true;

        List<ModelDefaultDevices> lstDevices = new ArrayList<>();
        adapterDefaultDevices adapter;
        int count = 1;
        while(flag){
            String infoDevice = sharPref.getString("DefaultDevices" + count, "vacio");
            if (infoDevice.equals("vacio"))
                flag = false;
            else {
                String[] info = infoDevice.split("%");
                if (!info[0].equals(address))
                    lstDevices.add(new ModelDefaultDevices(info[0], info[1], info[2]));
            }
            count++;
        }

        if (lstDevices.size() >= 1){
            adapter = new adapterDefaultDevices(lstDevices, this);
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mac = lstDevices.get(recyclerView.getChildAdapterPosition(v)).getMac();
                    String tipo = lstDevices.get(recyclerView.getChildAdapterPosition(v)).getTipo();

                    int transac = 0;

                    switch (tipo){
                        case "Bonaire-BQH":
                            transac = 4;
                            break;
                        case "Bonaire-BQD":
                            transac = 1;
                            break;
                        case "Bonaire-BQV":
                            transac = 1;
                            break;
                        case "Bonaire-TECH":
                            transac = 2;
                            break;
                        case "COCINAS INTELIGENTES":
                            transac = 5;
                            break;
                    }

                    editor.putInt("transacBtToActivity", transac);
                    editor.putString("BtMacString", mac);
                    editor.putString("BtNombreEquipo", nombreEquipo);
                    editor.apply();
                    Intent intent = new Intent(context, transacBtDeviceListActivity.class);
                    Toast.makeText(context, "Probando conexión con el equipo", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);

        }else{
            Toast.makeText(context, "No tiene equipos registrados", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try { // Cuando se sale de la aplicación esta parte permite que no se deje abierto el socket
            btSocket.close();
        } catch (IOException ignored) {
        }
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void fncCrearString(char myCaracter) {
        if (myCaracter == '{'){
            flag = true;
        }
        if (flag){
            strMessageReceived += myCaracter;
        }
        if (myCaracter == '}'){
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

            dataR1 = data.getR_1();
            dataR2 = data.getR_2();
            dataR3 = data.getR_3();
            dataR4 = data.getR_4();
            dataPwm = data.getPWM();
            dataServo = data.getServo();
            dataTemperatura = data.getT();
            dataHumedad = data.getH();
            dataOzono = data.getO3();
            dataMac = data.getMAC();
            dataIp = data.getIP();

            tvTemperatura.setText(data.getT());
            //tvHumedad.setText(data.getH());
            //tvOzono.setText(data.getO3());
            tvPm10.setText(data.getPM10());
            tvPm25.setText(data.getPM25());
            tvPm1.setText(data.getPM1());

            if (auto){
                if (Integer.valueOf(data.getT()) > Integer.valueOf(strTmpOn)){
                    if (!strTmpOn.equals("vacio")){
                        if (blTmp1){
                            fncEnviarDatos("R_1:ON");
                        }
                        if (blTmp2){
                            fncEnviarDatos("R_2:ON");
                        }
                        if (blTmp3){
                            fncEnviarDatos("R_3:ON");
                        }
                        if (blTmp4){
                            fncEnviarDatos("R_4:ON");
                        }
                    }
                }

                if (Integer.valueOf(strTmpOff) > Integer.valueOf(data.getT())){
                    if (!strTmpOff.equals("vacio")){
                        if (blTmp1){
                            fncEnviarDatos("R_1:OFF");
                        }
                        if (blTmp2){
                            fncEnviarDatos("R_2:OFF");
                        }
                        if (blTmp3){
                            fncEnviarDatos("R_3:OFF");
                        }
                        if (blTmp4){
                            fncEnviarDatos("R_4:OFF");
                        }
                    }
                }
                if (Integer.valueOf(data.getPM10()) > Integer.valueOf(strPmOn)){
                    if (!strPmOn.equals("vacio")){
                        if (blPm1){
                            fncEnviarDatos("R_1:ON");
                        }
                        if (blPm2){
                            fncEnviarDatos("R_2:ON");
                        }
                        if (blPm3){
                            fncEnviarDatos("R_3:ON");
                        }
                        if (blPm4){
                            fncEnviarDatos("R_4:ON");
                        }
                    }
                }
                if (Integer.valueOf(strPmOff) > Integer.valueOf(data.getPM10())){
                    if (!strPmOff.equals("vacio")){
                        if (blPm1){
                            fncEnviarDatos("R_1:OFF");
                        }
                        if (blPm2){
                            fncEnviarDatos("R_2:OFF");
                        }
                        if (blPm3){
                            fncEnviarDatos("R_3:OFF");
                        }
                        if (blPm4){
                            fncEnviarDatos("R_4:OFF");
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

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //crea un conexion de salida segura para el dispositivo usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume() {
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
            } catch (IOException e2) {
            }
        }
        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();

        fncEnviarDatos("BT_CONNECTED");
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

    //Crea la clase que permite crear el evento de conexion
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
        public void write(String input) {
            try {
                mmOutStream.write(input.getBytes());
            } catch (IOException e) {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "No fue posible enviar los datos", Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    private void fncEnviarDatos(String strMensaje) {
        try {
            MyConexionBT.write(strMensaje + "/");
        } catch (Exception e) {
            Toast.makeText(this, "Falla al enviar los datos, error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}