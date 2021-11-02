package com.example.bonairecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
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
import com.example.bonairecontrol.new_adapters.sensoresAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Bq_600_bt_activity extends AppCompatActivity {

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
    sensoresAdapter adapter;
    Context context = this;

    List<ModelListDevices> listDevices = new ArrayList<>();

    SharedPreferences sharPref;
    SharedPreferences.Editor editor;

    String strMensaje, strMessageReceived = "", nombreEquipo;

    String dataR1, dataR2, dataR3, dataR4, dataPwm, dataServo,
            dataTemperatura, dataHumedad, dataOzono, dataMac,
            dataIp, second, minute, hour, time;

    CardView cardView;
    RecyclerView recycler, recyclerView;
    Switch purificacion, desinfeccion, apagado;
    Button btnExec;
    TextView txt_nombre_equipo_bq_600_bt, txt_data_ultimo_reporte, txt_data_estado_equipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bq_600_bt_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;

                    Log.e("DATO: ", ""+MyCaracter);
                    fncCrearString(MyCaracter);
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter

        txt_nombre_equipo_bq_600_bt = (TextView) findViewById(R.id.txt_nombre_equipo_bq_600_bt);
        txt_data_ultimo_reporte = (TextView) findViewById(R.id.txt_data_ultimo_reporte);
        txt_data_estado_equipo = (TextView) findViewById(R.id.tv_estado_equipo);


        purificacion = (Switch) findViewById(R.id.switch_purificacion_bq_600_bt);
        desinfeccion = (Switch) findViewById(R.id.switch_desinfeccion_bq_600_bt);
        apagado = (Switch) findViewById(R.id.switch_apagado_bq_600_bt);
        recycler = (RecyclerView) findViewById(R.id.rv_bq_600_bt);
        recyclerView = (RecyclerView) findViewById(R.id.rv_default_devices_bq_600);
        cardView = findViewById(R.id.cv_to_devices_bq_600);
        btnExec = (Button)findViewById(R.id.btn_exec_action_bt);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        editor = sharPref.edit();
        nombreEquipo = sharPref.getString("BtNombreEquipo", "");
        txt_nombre_equipo_bq_600_bt.setText(nombreEquipo);

        purificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!purificacion.isChecked()){
                    if (desinfeccion.isChecked()){
                        desinfeccion.setChecked(false);
                    }
                }
            }
        });
        desinfeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desinfeccion.isChecked()){
                    if (!purificacion.isChecked()){
                        purificacion.setChecked(true);
                    }
                }
            }
        });

        apagado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apagado.isChecked()){
                    purificacion.setChecked(false);
                    desinfeccion.setChecked(false);
                }
            }
        });

        btnExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if (purificacion.isChecked()){
                    message = "P";
                }
                if (desinfeccion.isChecked()){
                    message = "D";
                }
                if (apagado.isChecked()){
                    message = "A";
                }

                if (message.equals("")){
                    Toast.makeText(context, "debe seleccionar una de las opciones", Toast.LENGTH_SHORT).show();
                }else {
                    switch (message) {
                        case "P":
                            fncEnviarDatos("PURIFICACION_BEGIN");
                            fncEnviarDatos("R_1:ON");
                            Toast.makeText(context, "comando enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                            break;
                        case "D":
                            fncEnviarDatos("DESINFECCION_BEGIN");
                            fncEnviarDatos("R_1:ON");
                            Toast.makeText(context, "comando enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                            break;
                        case "A":
                            fncEnviarDatos("PURIFICACION_STOP");
                            fncEnviarDatos("DESINFECCION_STOP");
                            fncEnviarDatos("R_1:OFF");
                            Toast.makeText(context, "comando enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    message = "";
                }

                if (purificacion.isChecked()){
                    message = "P";
                }
                if (desinfeccion.isChecked()){
                    message = "D";
                }
                if (apagado.isChecked()){
                    message = "A";
                }
                purificacion.setChecked(false);
                desinfeccion.setChecked(false);
                apagado.setChecked(false);

            }
        });
        FncCargarListaEquipos();
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
            cardView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try { // Cuando se sale de la aplicación esta parte permite que no se deje abierto el socket
            btSocket.close();
        } catch (IOException ignored) {}
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

   /* public void fncSwitchPurificacion(View view){

        try {
            if (switch_purificacion_bq_600_bt.isChecked()) {

                fncEnviarDatos("PURIFICACION_BEGIN");

            } else {

                fncEnviarDatos("PURIFICACION_STOP");
                if (switch_desinfeccion_bq_600_bt.isChecked()) {
                    switch_desinfeccion_bq_600_bt.setChecked(false);
                    fncEnviarDatos("DESINFECCION_STOP");
                }
            }
            Toast.makeText(Bq_600_bt_activity.this, "Datos enviados con exito", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    public void fncSwitchDesinfeccion(View view){

        try {

            if (switch_desinfeccion_bq_600_bt.isChecked()){

                fncEnviarDatos("DESINFECCION_BEGIN");

                if (!switch_purificacion_bq_600_bt.isChecked()){
                    switch_purificacion_bq_600_bt.setChecked(true);
                    fncEnviarDatos("PURIFICACION_BEGIN");
                }

            }else {

                fncEnviarDatos("DESINFECCION_STOP");

            }

        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }

    }*/

    private void fncCrearString(char myCaracter) {
        if (myCaracter != '/'){
            strMessageReceived += myCaracter;
        }
        if (myCaracter == '/'){
            fncActualizarDatos(strMessageReceived);
        }
    }

    private void fncActualizarDatos(String json) {
        json = json.replace("BT:", "").replace("\"\"", "\"0\"");

        Log.e("DATO: ", json);
        //Toast.makeText(context, json, Toast.LENGTH_LONG).show();
        try {

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

            hour = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
            minute = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
            second = new SimpleDateFormat("ss").format(Calendar.getInstance().getTime());
            time = new SimpleDateFormat("yy-MM-dd HH-mm").format(Calendar.getInstance().getTime());

            //txt_data_ultimo_reporte.setText(hour + " : " + minute + " : " + second);

            if (Integer.parseInt(dataServo)>=170){
                txt_data_estado_equipo.setText("Estado actual: desinfección");
            }
            if (Integer.parseInt(dataServo)<170){
                txt_data_estado_equipo.setText("Estado actual: purificación");
            }
            if(dataServo.equals("0")){
                txt_data_estado_equipo.setText("Estado actual: apagado");
            }

            listDevices.clear();
            //temperatura
            listDevices.add(new ModelListDevices(R.drawable.temperatura, dataTemperatura + " °C",
                    "Nombre: ", "TEMPERATURA","Último reporte: ",
                    time, "Id: ", nombreEquipo,  "Conexión actual wifi, esperando datos…",
                    "Conectado", "T"));

            //humedad
            listDevices.add(new ModelListDevices(R.drawable.humedad, dataHumedad + " %",
                    "Nombre: ", "HUMEDAD","Último reporte: ",
                    time, "Id: ", nombreEquipo,  "Conexión actual wifi, esperando datos…",
                    "Conectado", "H"));

            //ozono
            listDevices.add(new ModelListDevices(R.drawable.o3, dataOzono + " PPB",
                    "Nombre: ", "OZONO","Último reporte: ",
                    time, "Id: ", nombreEquipo,  "Conexión actual wifi, esperando datos…",
                    "Conectado", "o3"));

            adapter = new sensoresAdapter(listDevices);
            recycler.setAdapter(adapter);
            strMessageReceived="";
            json="";

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            strMessageReceived="";
            json="";
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //crea un conexion de salida segura para el dispositivo usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        address = sharPref.getString("BtMacString", "");

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
