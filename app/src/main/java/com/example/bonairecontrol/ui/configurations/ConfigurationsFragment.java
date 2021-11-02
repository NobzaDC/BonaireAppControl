package com.example.bonairecontrol.ui.configurations;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bonairecontrol.Controlador_adaptador.DevicesAdapter;
import com.example.bonairecontrol.Modelos.ModelDevices;
import com.example.bonairecontrol.R;
import com.example.bonairecontrol.transacBtDeviceListActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ConfigurationsFragment extends Fragment implements AdapterView.OnItemClickListener {


    public static ConfigurationsFragment newInstance() {
        return new ConfigurationsFragment();
    }

    //conexion con la vista
    View view = null;

    //Datos Bluetooth
    private BluetoothAdapter mBtAdapter;
    private static final String TAG = "DispositivosVinculados";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    Handler bluetoothIn;

    Context context = getContext();

    //cargamos componentes de la vista
    private ImageView img_logo_config_fragment, img_card_view_config_fragment;
    private ListView list_view_bluetooth_devices_config_fragment;
    private DevicesAdapter adapter;
    private ProgressBar progress_bar_config_fragment;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread MyConexionBT;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final int handlerState = 0;
    final private List<ModelDevices> lst = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.configurations_fragment, container, false);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;

                    Log.e("DATO", ""+MyCaracter);
                }
            }
        };
        
        img_logo_config_fragment = (ImageView)view.findViewById(R.id.img_logo_config_fragment);
        img_card_view_config_fragment = (ImageView)view.findViewById(R.id.img_card_view_config_fragment);
        list_view_bluetooth_devices_config_fragment = (ListView)view.findViewById(R.id.list_view_bluetooth_devices_config_fragment);
        progress_bar_config_fragment = (ProgressBar) view.findViewById(R.id.progress_bar_config_fragment);

        list_view_bluetooth_devices_config_fragment.setOnItemClickListener(this);
        try {
            adapter = new DevicesAdapter(getContext(), R.layout.list_devices, lst);
        }catch (Exception ignored){}

        fncTimerBluetooth();
        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fncCrearLista();

        SharedPreferences sharPref = this.getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

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
        // Comprueba que el dispositivo tiene Bluetooth y que está encendido.
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(getContext(), "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth Activado...");
            } else {
                //Solicita al usuario que active Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void fncCrearLista() {
        list_view_bluetooth_devices_config_fragment.setAdapter(null);
        lst.clear();
        progress_bar_config_fragment.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable(){
            public void run(){
                if (mBtAdapter.isEnabled()){
                    Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            //if (device.getName().contains("BONAIRE_TECH")){
                                lst.add(new ModelDevices(device.getName().toString(), "activo", "NA",
                                    "bluetooth", "local", device.getAddress(), R.drawable.ico_bluetooth, ""));
                            //}
                            img_card_view_config_fragment.setVisibility(View.INVISIBLE);
                        }
                    }
                }else {
                    verificarEstadoBT();
                }
                progress_bar_config_fragment.setVisibility(View.INVISIBLE);
                try {
                    adapter = new DevicesAdapter(getContext(), R.layout.list_devices, lst);
                }catch (Exception ignored){}

                list_view_bluetooth_devices_config_fragment.setAdapter(adapter);
            }
        }, 3500);
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //crea un conexion de salida segura para el dispositivo usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String address = adapter.getItem(position).getTipoEquipo();
        Intent intent = new Intent(getContext(), transacBtDeviceListActivity.class);

        Toast.makeText(getContext(), "Probando conexión con el equipo", Toast.LENGTH_LONG).show();

        SharedPreferences sharPref = this.getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        editor.putInt("transacBtToActivity", 3);
        editor.putString("BtMacString", address);
        editor.putString("BtNombreEquipo", adapter.getItem(position).getNombreEquipo());
        editor.commit();

        startActivity(intent);

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