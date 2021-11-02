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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.Controlador_adaptador.adapterDefaultDevices;
import com.example.bonairecontrol.Modelos.ModelConfigs;
import com.example.bonairecontrol.Modelos.ModelDefaultDevices;
import com.example.bonairecontrol.Modelos.ModelJsonTechCE;
import com.example.bonairecontrol.Modelos.ModelListDevices;
import com.example.bonairecontrol.new_adapters.sensoresAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tech_ce_bt_activity extends AppCompatActivity {

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");// Identificador unico de servicio - SPP UUID
    private static final String TAG = "LOG_BONAIRE";//TAG
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private String address = null;
    final int handlerState = 0;
    Handler bluetoothIn;
    Context context = this;//context
    String strMessageReceived = "", nombreEquipo;
    //Componentes XML
    TextView txt_nombre_equipo_tech_ce_bt, txt_data_ultimo_reporte;
    //bluetooth
    RecyclerView recycler, recyclerView;
    //Componentes XML
    List<ModelListDevices> listDevices = new ArrayList<>();
    sensoresAdapter adapter; //adapter
    ArrayList<ModelConfigs> List = new ArrayList<>();
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;
    boolean flag = false;

    SharedPreferences sharPref;
    SharedPreferences.Editor editor;

    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_ce_bt_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txt_nombre_equipo_tech_ce_bt = (TextView) findViewById(R.id.txt_nombre_equipo_tech_ce_bt);
        txt_data_ultimo_reporte = (TextView) findViewById(R.id.txt_data_ultimo_reporte);
        recycler = (RecyclerView) findViewById(R.id.rv_list_tech_ce_bt);
        recyclerView = (RecyclerView) findViewById(R.id.rv_default_devices_tech_ce);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        cardView = findViewById(R.id.cv_to_devices_tech_ce);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;

                    //Log.e("mLog", "" + MyCaracter);
                    fncCrearString(MyCaracter);
                }
            }
        };

        sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        editor = sharPref.edit();
        nombreEquipo = sharPref.getString("BtNombreEquipo", "");
        address = sharPref.getString("BtMacString", "");

        txt_nombre_equipo_tech_ce_bt.setText(nombreEquipo);

        createListData(true, "Esperando reporte", "Esperando reporte",
                "Esperando reporte", "Esperando reporte", "Esperando reporte",
                "Esperando reporte", "Esperando reporte", "Esperando reporte",
                "Esperando reporte", "");
        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
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

    private void createListData(boolean init, String vlO3, String vlCO2, String vlCO, String vlVOC,
                                String vlT, String vlH, String vlPM1, String vlPM10, String vlPM25, String time) {
        listDevices.clear();
        if (init) {
            //T index:(1)
            listDevices.add(new ModelListDevices(R.drawable.temperatura, vlT,
                    "Nombre: ", "TEMPERATURA", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", ""));
            //H index:(2)
            listDevices.add(new ModelListDevices(R.drawable.humedad, vlH,
                    "Nombre: ", "HUMEDAD", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", ""));
            //CO2 index:(3)
            listDevices.add(new ModelListDevices(R.drawable.co2, vlCO2,
                    "Nombre: ", "DIOXIDO DE CARBONO", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", ""));
            //CO index:(4)
            listDevices.add(new ModelListDevices(R.drawable.co, vlCO,
                    "Nombre: ", "MONOXIDO DE CARBONO", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", ""));
            //VOC index:(6)
            listDevices.add(new ModelListDevices(R.drawable.voc, vlVOC,
                    "Nombre: ", "COMPUESTOS ORGANICOS FORMALDEIDO", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", ""));
            //PM1 index:(7)
            listDevices.add(new ModelListDevices(R.drawable.pm1, vlPM1,
                    "Nombre: ", "PM1", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", ""));
            //PM25 index:(8)
            listDevices.add(new ModelListDevices(R.drawable.pm25, vlPM25,
                    "Nombre: ", "PM25", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", ""));
            //PM10 index:(9)
            listDevices.add(new ModelListDevices(R.drawable.pm10, vlPM10,
                    "Nombre: ", "PM10", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", ""));
            //ozono index:(10)
            listDevices.add(new ModelListDevices(R.drawable.o3, vlO3,
                    "Nombre: ", "OZONO", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", ""));
        } else {
            //T index:(1)
            listDevices.add(new ModelListDevices(R.drawable.temperatura, vlT + " °C",
                    "Nombre: ", "TEMPERATURA", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", "T"));
            //H index(2)
            listDevices.add(new ModelListDevices(R.drawable.humedad, vlH + " %",
                    "Nombre: ", "HUMEDAD", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", "H"));
            //CO2 index(3)
            listDevices.add(new ModelListDevices(R.drawable.co2, vlCO2 + " PPM",
                    "Nombre: ", "DIOXIDO DE CARBONO", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", "CO2"));
            //CO index(4)
            listDevices.add(new ModelListDevices(R.drawable.co, vlCO + " PPM",
                    "Nombre: ", "MONOXIDO DE CARBONO", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", "CO"));
            //VOC index(6)
            listDevices.add(new ModelListDevices(R.drawable.voc, vlVOC + " PPB",
                    "Nombre: ", "COMPUESTOS ORGANICOS FORMALDEIDO", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", "VOC"));
            //PM1 index(7)
            listDevices.add(new ModelListDevices(R.drawable.pm1, vlPM1 + " μg/m³",
                    "Nombre: ", "PM1", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", "PM1"));
            //PM25 index(8)
            listDevices.add(new ModelListDevices(R.drawable.pm25, vlPM25 + " μg/m³",
                    "Nombre: ", "PM25", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", "PM25"));
            //PM10 index(9)
            listDevices.add(new ModelListDevices(R.drawable.pm10, vlPM10 + " μg/m³",
                    "Nombre: ", "PM10", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", "PM10"));

            //O3 index(10)
            listDevices.add(new ModelListDevices(R.drawable.o3, vlO3 + " PPB",
                    "Nombre: ", "OZONO", "Último reporte: ",
                    time, "Id: ", nombreEquipo, "Conexión actual bluetooth, esperando datos…",
                    "Conectado", "O3"));
        }
        adapter = new sensoresAdapter(listDevices);
        recycler.setAdapter(adapter);
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
        try {
            Gson gson = new Gson();
            ModelJsonTechCE data = gson.fromJson(json, ModelJsonTechCE.class);

            Log.e("DATO", json);

            createListData(false, data.getO3(), data.getCO2(), data.getCO(), data.getVOC(), data.getT(), data.getH(), data.getPM1(), data.getPM10(), data.getPM25(), data.getFecha() + " " + data.getHora());

            txt_data_ultimo_reporte.setText(data.getFecha() + " " + data.getHora());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //crea un conexion de salida segura para el dispositivo usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);

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

    private void fncEnviarDatos(String strMensaje) {
        MyConexionBT.write(strMensaje + "/");
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
        public boolean write(String input) {
            try {
                mmOutStream.write(input.getBytes());
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
}