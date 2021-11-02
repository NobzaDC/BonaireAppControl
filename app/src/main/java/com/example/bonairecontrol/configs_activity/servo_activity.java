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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class servo_activity extends AppCompatActivity {

    TextView txt_nombre_disposistivo_servo_pwm, txt_valor_seek_bar_max_servo_pwm, txt_valor_seek_bar_min_servo_pwm;
    SeekBar seek_bar_servo_max_servo_pwm, seek_bar_servo_min_servo_pwm;

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

    String strMessageReceived = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servo_activity);
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;

                    fncCrearString(MyCaracter);
                }
            }
        };
        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter

        init();
        seek_bar_servo_max_servo_pwm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                txt_valor_seek_bar_max_servo_pwm.setText("" + progress + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (seekBar.getProgress() < seek_bar_servo_min_servo_pwm.getProgress()) {
                    seek_bar_servo_min_servo_pwm.setProgress(0, true);
                    Toast.makeText(getApplicationContext(), "el valor minimo no debe ser mayor al maximo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seek_bar_servo_min_servo_pwm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_valor_seek_bar_min_servo_pwm.setText("" + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (seekBar.getProgress() > seek_bar_servo_max_servo_pwm.getProgress()) {
                    seek_bar_servo_min_servo_pwm.setProgress(0, true);
                    Toast.makeText(getApplicationContext(), "el valor minimo no debe ser mayor al maximo", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void init() {
        seek_bar_servo_max_servo_pwm = findViewById(R.id.seek_bar_servo_max_servo);
        seek_bar_servo_min_servo_pwm = findViewById(R.id.seek_bar_servo_min_servo);
        txt_valor_seek_bar_max_servo_pwm = findViewById(R.id.txt_valor_seek_bar_max_servo);
        txt_valor_seek_bar_min_servo_pwm = findViewById(R.id.txt_valor_seek_bar_min_servo);
        txt_nombre_disposistivo_servo_pwm = findViewById(R.id.txt_nombre_disposistivo_servo);
        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        String nombreEquipo = sharPref.getString("BtNombreEquipo", "");

        txt_nombre_disposistivo_servo_pwm.setText(nombreEquipo);
    }

    public void fncBtnSave(View view) {

        if (seek_bar_servo_min_servo_pwm.getProgress() != 0 && seek_bar_servo_max_servo_pwm.getProgress() != 0) {
            fncEnviarDatos("SET_SERVO_MIN:" + (seek_bar_servo_min_servo_pwm.getProgress()*180)/100);
            fncEnviarDatos("SET_SERVO_MAX:" + (seek_bar_servo_max_servo_pwm.getProgress()*180)/100);
            fncEnviarDatos("RESET");
            seek_bar_servo_max_servo_pwm.setProgress(1, true);
            seek_bar_servo_min_servo_pwm.setProgress(1, true);
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
}