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
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class manual_activity extends AppCompatActivity {

    Context context = this;

    private SwitchMaterial swRele2, swRele3, swRele4, swEnablePwm, swEnableServo;
    private SeekBar sbPwm, sbServo;
    private TextView tvPwm, tvServo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_activity);
        init();


    }

    private void init() {
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;

                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter

        swRele2=findViewById(R.id.switch_rele_2_manual);
        swRele3=findViewById(R.id.switch_rele_3_manual);
        swRele4=findViewById(R.id.switch_rele_4_manual);
        swEnablePwm=findViewById(R.id.sw_enable_pwm_manual);
        swEnableServo=findViewById(R.id.sw_enable_servo_manual);

        sbPwm=findViewById(R.id.sb_pwm_manual);
        sbServo=findViewById(R.id.sb_servo_manual);

        sbPwm.setEnabled(false);
        sbServo.setEnabled(false);

        tvPwm=findViewById(R.id.tv_pwm_manual);
        tvServo=findViewById(R.id.tv_servo_manual);


        swRele2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swRele2.isChecked()){
                    fncEnviarDatos("R_2:ON");
                }else {
                    fncEnviarDatos("R_2:OFF");
                }
            }
        });

        swRele3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swRele3.isChecked()){
                    fncEnviarDatos("R_3:ON");
                }else {
                    fncEnviarDatos("R_3:OFF");
                }
            }
        });

        swRele4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swRele4.isChecked()){
                    fncEnviarDatos("R_4:ON");
                }else {
                    fncEnviarDatos("R_4:OFF");
                }
            }
        });

        swEnablePwm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbPwm.setEnabled(swEnablePwm.isChecked());
                sbPwm.setProgress(0);
                tvPwm.setText("PWM: 00%");
                if (!swEnablePwm.isChecked()){
                    fncEnviarDatos("PWM:OFF");
                }
            }
        });

        swEnableServo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swEnableServo.isChecked()){
                    fncEnviarDatos("R_1:ON");
                }else {
                    fncEnviarDatos("R_1:OFF");
                }

                sbServo.setEnabled(swEnableServo.isChecked());
                sbServo.setProgress(0);
                tvServo.setText("SERVO: 00%");
                if (!swEnableServo.isChecked()){
                    fncEnviarDatos("SERVO:0");
                }
            }
        });

        sbPwm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPwm.setText("PWM: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fncEnviarDatos("PWM:"+(sbPwm.getProgress()*220)/100);
                Toast.makeText(context, "PWM"+ (sbPwm.getProgress()*220)/100, Toast.LENGTH_SHORT).show();
            }
        });

        sbServo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvServo.setText("SERVO: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fncEnviarDatos("SERVO:"+(sbServo.getProgress()*180)/100);
            }
        });
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
                Log.e("mLog", e.getMessage());
                //finish();
            }
        }
    }

    private void fncEnviarDatos(String strMensaje) {
        try {
            MyConexionBT.write(strMensaje + "/");
            Log.e("mLog", strMensaje);

        } catch (Exception e) {
            Toast.makeText(this, "Falla al enviar los datos, error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("mLog", e.getMessage());
        }
    }
}