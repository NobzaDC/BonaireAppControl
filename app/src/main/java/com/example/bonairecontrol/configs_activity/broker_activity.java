package com.example.bonairecontrol.configs_activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class  broker_activity extends AppCompatActivity {

    TextView txt_nombre_disposistivo_broker, txt_data_servidor_broker, txt_data_user_broker,
            txt_data_password_broker, txt_data_port_broker;

    Context context = this;

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

    String strMensaje = "", strMessageReceived = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_activity);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;
                    strMensaje += MyCaracter;
                    if (strMensaje.contains("BT:")){
                        strMensaje="";
                    }

                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter

        txt_nombre_disposistivo_broker = findViewById(R.id.txt_nombre_disposistivo_broker);
        txt_data_servidor_broker = findViewById(R.id.txt_data_servidor_broker);
        txt_data_user_broker = findViewById(R.id.txt_data_user_broker);
        txt_data_password_broker = findViewById(R.id.txt_data_password_broker);
        txt_data_port_broker = findViewById(R.id.txt_data_port_broker);

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        String nombreEquipo = sharPref.getString("BtNombreEquipo", "");
        txt_nombre_disposistivo_broker.setText(nombreEquipo);

    }

    public void fncBtnActualizarDatosBroker(View view) {
        String server, user, password, port;
        boolean a = false;

        server = txt_data_servidor_broker.getText().toString();
        user = txt_data_user_broker.getText().toString();
        password = txt_data_password_broker.getText().toString();
        port = txt_data_port_broker.getText().toString();

        if (!server.equals("")) {
            fncEnviarDatos("SET_MQTT_SERVER:" + server);
            a = true;
        }
        if (!user.equals("")) {
            fncEnviarDatos("SET_MQTT_USER:" + user);
            a = true;
        }
        if (!password.equals("")) {
            fncEnviarDatos("SET_MQTT_PASSWORD:" + password);
            a = true;
        }
        if (!port.equals("")) {
            fncEnviarDatos("SET_MQTT_PORT:" + port);
            a = true;
        }

        txt_data_servidor_broker.setText("");
        txt_data_user_broker.setText("");
        txt_data_password_broker.setText("");
        txt_data_port_broker.setText("");

        if (!a) {
            Toast.makeText(this, "Debe ingresar como minimo un valor", Toast.LENGTH_SHORT).show();
        } else {
            fncEnviarDatos("RESET");
            Toast.makeText(this, "Datos enviados con exito", Toast.LENGTH_SHORT).show();
        }
    }


    private void fncCrearString(char myCaracter) {
        if (myCaracter != '/') {
            strMessageReceived += myCaracter;
        }
        if (myCaracter == '/') {
            fncActualizarDatos(strMessageReceived);
        }
    }

    private void fncActualizarDatos(String json) {
        try {

        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
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

        Handler handler = new Handler();

        fncEnviarDatos("GET_MQTT_SERVER");

        fncEnviarDatos("GET_MQTT_USER");

        fncEnviarDatos("GET_MQTT_PASSWORD");

        fncEnviarDatos("GET_MQTT_PORT");

        handler.postDelayed(new Runnable() {
            public void run() {
                String[] data = strMensaje.split("\n");

                try {
                    if (data.length == 4){
                        txt_data_servidor_broker.setText(data[0]);
                        txt_data_user_broker.setText(data[1]);
                        txt_data_password_broker.setText(data[2]);
                        txt_data_port_broker.setText(data[3]);
                    }else {
                        txt_data_servidor_broker.setText(data[1]);
                        txt_data_user_broker.setText(data[2]);
                        txt_data_password_broker.setText(data[3]);
                        txt_data_port_broker.setText(data[4]);
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "No fue posible cargar los valores de inicio.", Toast.LENGTH_SHORT).show();
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

    private void fncEnviarDatos(String strMensaje) {
        try {
            MyConexionBT.write(strMensaje + "/");
        } catch (Exception e) {
            Toast.makeText(this, "Falla al enviar los datos, error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}