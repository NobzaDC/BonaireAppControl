package com.example.bonairecontrol.configs_activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.Controlador_adaptador.adapterListSettings;
import com.example.bonairecontrol.Modelos.ModelSettings;
import com.example.bonairecontrol.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class settings_activity extends AppCompatActivity {

    Context context = this;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String address;
    //end bluetooth
    private RecyclerView recycler;
    List<ModelSettings> settingsList;
    adapterListSettings adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;

                    Log.e("DATO", ""+MyCaracter);
                }
            }
        };

        settingsList = createList();

        recycler = (RecyclerView)findViewById(R.id.rv_list_settings);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new adapterListSettings(settingsList, context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
                String message = sharPref.getString("BluetoothMessage", "");
                if (!message.equals("")){
                    fncEnviarDatos(message);
                }
            }
        });
        recycler.setAdapter(adapter);


        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter

    }

    private List<ModelSettings> createList() {
        List<ModelSettings> lst = new ArrayList<>();
        String tech_ce = getString(R.string.tech_ce), b_q600 = getString(R.string.b_q600), description = getString(R.string.description), tech_ce_b_q600 = tech_ce + ", " + b_q600;
        lst.add(new ModelSettings(R.drawable.baseline_blur_circular_24, "CO2", description, tech_ce, "SET_CO2_OFFSET:"));
        lst.add(new ModelSettings(R.drawable.baseline_blur_circular_24, "VOC", description, tech_ce, "SET_TVOC_OFFSET:"));
        lst.add(new ModelSettings(R.drawable.baseline_blur_circular_24, "T", description, tech_ce_b_q600, "SET_TEMP_OFFSET:"));
        lst.add(new ModelSettings(R.drawable.baseline_blur_circular_24, "H", description, tech_ce_b_q600, "SET_HUM_OFFSET:"));
        lst.add(new ModelSettings(R.drawable.baseline_blur_circular_24, "CO", description, tech_ce, "SET_CO_OFFSET:"));
        lst.add(new ModelSettings(R.drawable.baseline_blur_circular_24, "O3", description, tech_ce_b_q600, "SET_O3_OFFSET:"));
        lst.add(new ModelSettings(R.drawable.baseline_blur_circular_24, "PM1", description, tech_ce, "SET_PM1_OFFSET:"));
        lst.add(new ModelSettings(R.drawable.baseline_blur_circular_24, "PM25", description, tech_ce, "SET_PM25_OFFSET:"));
        lst.add(new ModelSettings(R.drawable.baseline_blur_circular_24, "PM10", description, tech_ce, "SET_PM10_OFFSET:"));
        return lst;
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
                Toast.makeText(getBaseContext(), "No fue posible enviar la informacion", Toast.LENGTH_LONG).show();
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

    public static void sendConfiguration(Context context) {
        Activity activity = new Activity();
        SharedPreferences sharPref = activity.getSharedPreferences("BonairePref", 0);
        String dato = sharPref.getString("BluetoothMessage", "");

    }
}