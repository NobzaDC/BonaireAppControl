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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.R;
import com.example.bonairecontrol.bottomSheet.bottomSheetNetworksWifi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class internet_activity extends AppCompatActivity implements bottomSheetNetworksWifi.BottomSheetListener {

    TextView txt_nombre_disposistivo_internet, txt_ssid_data_internet, txt_ssid_password_data_internet;
    ImageButton btn_bottom_sheet;

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

    String strMensaje="", strMessageReceived = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_activity);

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

        txt_nombre_disposistivo_internet = findViewById(R.id.txt_nombre_disposistivo_internet);
        txt_ssid_data_internet = findViewById(R.id.txt_ssid_data_internet);
        txt_ssid_password_data_internet = findViewById(R.id.txt_ssid_password_data_internet);
        btn_bottom_sheet = findViewById(R.id.btn_bottom_sheet);

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        String nombreEquipo = sharPref.getString("BtNombreEquipo", "");

        txt_nombre_disposistivo_internet.setText(nombreEquipo);

        btn_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetNetworksWifi bottomSheet = new bottomSheetNetworksWifi();
                bottomSheet.show(getSupportFragmentManager(), "bottomSheet");
            }
        });

    }

    public void fncBtnSave(View view) {

        String ssid, ssidPassword;

        ssid = txt_ssid_data_internet.getText().toString();
        ssidPassword = txt_ssid_password_data_internet.getText().toString();

        if(!ssid.equals("") && !ssidPassword.equals("")){

            fncEnviarDatos("SET_SSID:" + ssid );
            fncEnviarDatos("SET_PASSWORD:" + ssidPassword );
            fncEnviarDatos("RESET");
            Toast.makeText(this, "Datos enviados con exito", Toast.LENGTH_SHORT).show();
            txt_ssid_data_internet.setText("");
            txt_ssid_password_data_internet.setText("");

        }else {
            if (ssid.equals("")){
                txt_ssid_data_internet.setError("Debe ingresar el ssid del internet");
            }
            if (ssidPassword.equals("")){
                txt_ssid_password_data_internet.setError("Debe ingresar la contraseña del internet");
            }
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

        fncEnviarDatos("GET_SSID");
        fncEnviarDatos("GET_PASSWORD");

        handler.postDelayed(new Runnable() {
            public void run() {
                String[] data = strMensaje.split("\n");

                /*try {
                    if (data.length == 2) {
                        txt_ssid_data_internet.setText(data[0]);
                        txt_ssid_password_data_internet.setText(data[1]);
                    } else {
                        txt_ssid_data_internet.setText(data[1]);
                        txt_ssid_password_data_internet.setText(data[2]);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No fue posible cargar los valores de inicio.", Toast.LENGTH_SHORT).show();
                }*/
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

    @Override
    public void onFieldClicked(String text) {
        txt_ssid_data_internet.setText(text);
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