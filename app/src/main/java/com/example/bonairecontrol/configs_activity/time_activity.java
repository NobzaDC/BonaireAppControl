package com.example.bonairecontrol.configs_activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class time_activity extends AppCompatActivity {

    Context context = this;

    private EditText edInterval, edFrequency;
    private Button btnInterval, btnFrequency;

    //bluetooth
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
    String strMensaje = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_activity);
        init();
    }

    private void init() {
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;
                    strMensaje += MyCaracter;
                    if (strMensaje.contains("BT:")) {
                        strMensaje = "";
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter

        edInterval = (EditText) findViewById(R.id.txt_interval_data_time);
        edFrequency = (EditText) findViewById(R.id.txt_frequency_data_time);
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //crea un conexion de salida segura para el dispositivo usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    public void fncBtnSave(View view) {
        if (edInterval.getText().toString().isEmpty() && edFrequency.getText().toString().isEmpty()) {
            Toast.makeText(context, "Debe llenar almenos un campo para enviar los datos", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (!edInterval.getText().toString().isEmpty()) {
                int value = Integer.parseInt(edInterval.getText().toString());
                value = value * 1000;
                fncEnviarDatos("SET_FAN_INTERVAL:" + value, true);
                fncEnviarDatos("RESET", false);
                edInterval.setText("");
            }
            if (!edFrequency.getText().toString().isEmpty()) {
                int value = Integer.parseInt(edFrequency.getText().toString());
                value = value * 1000;
                fncEnviarDatos("SET_FAN_FREQUENCY:" + value, true);
                fncEnviarDatos("RESET", false);
                edFrequency.setText("");
            }
        }
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

        Handler handler = new Handler();

        fncEnviarDatos("GET_FAN_INTERVAL", false);
        fncEnviarDatos("GET_FAN_FREQUENCY", false);

        handler.postDelayed(new Runnable() {
            public void run() {
                String[] data = strMensaje.split("\n");

                try {
                    if (data.length == 2) {
                        edInterval.setText(data[0].substring(0, data[0].length()-4));
                        edFrequency.setText(data[1].substring(0, data[1].length()-4));
                    } else {
                        edInterval.setText(data[1].substring(0, data[1].length()-4));
                        edFrequency.setText(data[2].substring(0, data[2].length()-4));
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No fue posible cargar los valores de inicio.", Toast.LENGTH_LONG).show();
                }
            }
        }, 500);

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

    private void fncEnviarDatos(String strMensaje, boolean command) {
        try {
            MyConexionBT.write(strMensaje + "/");
            if (command) {
                Toast.makeText(context, "datos enviados satisfactoriamente", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Falla al enviar los datos, error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}