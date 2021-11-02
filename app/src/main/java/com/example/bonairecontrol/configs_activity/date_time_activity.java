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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class date_time_activity extends AppCompatActivity {

    TextView txt_nombre_disposistivo_date_time, txt_data_año_date_time,
            txt_data_mes_date_time, txt_data_dia_date_time, txt_data_hora_date_time_2, txt_data_minuto_date_time,
            txt_data_segundo_date_time;

    Switch switch_txt_info_1_date_time;

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

    String strMensaje, strMessageReceived = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_activity);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;

                    fncCrearString(MyCaracter);
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        String nombreEquipo = sharPref.getString("BtNombreEquipo", "");

        txt_nombre_disposistivo_date_time = findViewById(R.id.txt_nombre_disposistivo_date_time);
        txt_data_año_date_time = findViewById(R.id.txt_data_año_date_time);
        txt_data_mes_date_time = findViewById(R.id.txt_data_mes_date_time);
        txt_data_dia_date_time = findViewById(R.id.txt_data_dia_date_time);
        txt_data_hora_date_time_2 = findViewById(R.id.txt_data_hora_date_time_2);
        txt_data_minuto_date_time = findViewById(R.id.txt_data_minuto_date_time);
        txt_data_segundo_date_time = findViewById(R.id.txt_data_segundo_date_time);

        switch_txt_info_1_date_time = findViewById(R.id.switch_txt_info_1_date_time);

        txt_nombre_disposistivo_date_time.setText(nombreEquipo);

        switch_txt_info_1_date_time.setChecked(true);

    }

    private void fncCargarEditText() {
        String year = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        String month = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
        String day = new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());
        String hour = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        String minute = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
        String second = new SimpleDateFormat("ss").format(Calendar.getInstance().getTime());

        txt_data_año_date_time.setText(year);
        txt_data_mes_date_time.setText(month);
        txt_data_dia_date_time.setText(day);
        txt_data_hora_date_time_2.setText(hour);
        txt_data_minuto_date_time.setText(minute);
        txt_data_segundo_date_time.setText(second);
    }

    public void fncSwitchListener(View view) {
        if (switch_txt_info_1_date_time.isChecked()) {
            actualTimeEnable();
        } else {
            actualTimeDisable();
        }
    }


    public void fncBtnSave(View view) {

        if (switch_txt_info_1_date_time.isChecked()) {

            String year = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
            String month = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
            String day = new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());
            String hour = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
            String minute = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
            String second = new SimpleDateFormat("ss").format(Calendar.getInstance().getTime());

            fncEnviarDatos("SET_RTC:" + day + ":" + month + ":" + year + ":" + hour + ":" + minute + ":" + second + "");
            Toast.makeText(this, "Datos enviados con exito", Toast.LENGTH_SHORT).show();
            fncEnviarDatos("RESET");

        } else {

            String año = txt_data_año_date_time.getText().toString();
            String mes = txt_data_mes_date_time.getText().toString();
            String dia = txt_data_dia_date_time.getText().toString();
            String hora = txt_data_hora_date_time_2.getText().toString();
            String minuto = txt_data_minuto_date_time.getText().toString();
            String segundo = txt_data_segundo_date_time.getText().toString();

            if (!año.equals("") && !mes.equals("") && !dia.equals("") && !hora.equals("") && !minuto.equals("") && !segundo.equals("")) {
                if (Integer.parseInt(año) <= 9999 && Integer.parseInt(año) > 999) {
                    if (Integer.parseInt(mes) <= 12) {
                        if (Integer.parseInt(dia) <= 31) {
                            if (Integer.parseInt(hora) <= 24) {
                                if (Integer.parseInt(minuto) <= 59) {
                                    if (Integer.parseInt(segundo) <= 59) {

                                        fncEnviarDatos("SET_RTC:" + dia + ":" + mes + ":" + año + ":" + hora + ":" + minuto + ":" + segundo + "");
                                        Toast.makeText(this, "Datos enviados con exito", Toast.LENGTH_SHORT).show();
                                        txt_data_año_date_time.setText("");
                                        txt_data_mes_date_time.setText("");
                                        txt_data_dia_date_time.setText("");
                                        txt_data_hora_date_time_2.setText("");
                                        txt_data_minuto_date_time.setText("");
                                        txt_data_segundo_date_time.setText("");

                                    }else {
                                        txt_data_segundo_date_time.setError("Los segundos no deben ser mayor a 59 ni menor a 0");
                                    }
                                }else {
                                    txt_data_minuto_date_time.setError("Los minutos no debe ser mayor a 59 ni menor a 0");
                                }
                            }else {
                                txt_data_hora_date_time_2.setError("La hora no debe ser mayor a 24 ni menor a 0");
                            }
                        }else {
                            txt_data_dia_date_time.setError("El dia no debe ser mayor a 31 ni menor a 1");
                        }
                    }else {
                        txt_data_mes_date_time.setError("El mes no debe ser mayor a 12 ni menor a 1");
                    }
                }else {
                    txt_data_año_date_time.setError("El año no debe ser mayor a 9999 ni menor a 1000");
                }
            }else {
                Toast.makeText(this, "Debe llenar todos los datos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void actualTimeEnable() {
        txt_data_año_date_time.setEnabled(false);
        txt_data_mes_date_time.setEnabled(false);
        txt_data_dia_date_time.setEnabled(false);
        txt_data_hora_date_time_2.setEnabled(false);
        txt_data_minuto_date_time.setEnabled(false);
        txt_data_segundo_date_time.setEnabled(false);

        txt_data_año_date_time.setText("");
        txt_data_mes_date_time.setText("");
        txt_data_dia_date_time.setText("");
        txt_data_hora_date_time_2.setText("");
        txt_data_minuto_date_time.setText("");
        txt_data_segundo_date_time.setText("");
        fncCargarEditText();
    }

    private void actualTimeDisable() {
        txt_data_año_date_time.setEnabled(true);
        txt_data_mes_date_time.setEnabled(true);
        txt_data_dia_date_time.setEnabled(true);
        txt_data_hora_date_time_2.setEnabled(true);
        txt_data_minuto_date_time.setEnabled(true);
        txt_data_segundo_date_time.setEnabled(true);

        txt_data_año_date_time.setText("");
        txt_data_mes_date_time.setText("");
        txt_data_dia_date_time.setText("");
        txt_data_hora_date_time_2.setText("");
        txt_data_minuto_date_time.setText("");
        txt_data_segundo_date_time.setText("");
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

        fncCargarEditText();

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