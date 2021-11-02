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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.Modelos.ModelDevices;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class transacBtDeviceListActivity extends AppCompatActivity {

    Context context = this;
    private static final String TAG = "DispositivosVinculados";
    private BluetoothAdapter mBtAdapter;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread MyConexionBT;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final int handlerState = 0;
    Handler bluetoothIn;
    boolean blValidation;
    final private List<ModelDevices> lst = new ArrayList<>();

    //String
    String address, name;
    int to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transac_bt_device_list);


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    //char MyCaracter = (char) msg.obj;

                    //Log.e("DATO: ", "" + MyCaracter);
                    //fncCrearString(MyCaracter);
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter

        SharedPreferences sharPref = context.getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        address = sharPref.getString("BtMacString", "");
        to = sharPref.getInt("transacBtToActivity", 0);
        name = sharPref.getString("BtNombreEquipo", "");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fncTimerBluetooth();


    }

    private void succesTransac() {
        try { // Cuando se sale de la aplicación esta parte permite que no se deje abierto el socket
            btSocket.close();
        } catch (IOException e) {
        }
        Intent intent = new Intent(context, HomeActivity.class);
        switch (to) {
            case 1: //b-q600 bluetooth
                intent = new Intent(context, Bq_600_bt_activity.class);
                break;
            case 2://tech-ce bluetooth
                intent = new Intent(context, Tech_ce_bt_activity.class);
                break;
            case 3:
                intent = new Intent(context, config_master_detail_activity.class);
                break;
            case 4:
                intent = new Intent(context, DeshumidacadorActivity.class);
                break;
            case 5:
                intent = new Intent(context, CampanasActivity.class);
                break;
        }
        //Toast.makeText(context, "Conectando...", Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, "Intent to: "+ to, Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        Intent finalIntent = intent;
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(finalIntent);
            }
        }, 1800);

    }

    private void wrongTransac() {
        try { // Cuando se sale de la aplicación esta parte permite que no se deje abierto el socket
            btSocket.close();
        } catch (IOException e) {
        }
        Toast.makeText(context, "No es posible conectar con este equipo", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Setea la direccion MAC
        try {
            BluetoothDevice device = btAdapter.getRemoteDevice(address);
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
        blValidation = MyConexionBT.write("BT_CONNECTED//");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tryConexionBt();
            }
        }, 1800);

    }

    private void tryConexionBt() {
        try {
            if (blValidation) {
                succesTransac();
            } else {
                wrongTransac();
            }
        } catch (Exception e) {
            wrongTransac();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try { // Cuando se sale de la aplicación esta parte permite que no se deje abierto el socket
            btSocket.close();
        } catch (IOException e2) {
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //crea un conexion de salida segura para el dispositivo usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private void fncTimerBluetooth() {
        Timer t = new Timer();
        final TimerTask verificacionBluetooth = new TimerTask() {
            @Override
            public void run() {

                verificarEstadoBT();
            }
        };
        t.schedule(verificacionBluetooth, 0, 8000);


    }

    private void verificarEstadoBT() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(context, "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                /*Log.d(TAG, "...Bluetooth Activado...");*/
            } else {
                //Solicita al usuario que active Bluetooth
                try {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                } catch (Exception e) {
                    Toast.makeText(context, "Si el Bluetooth no se encuentra activado por favor activelo", Toast.LENGTH_SHORT).show();
                }
            }
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
}