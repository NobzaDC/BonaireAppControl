package com.example.bonairecontrol.ui.smoke;

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
import com.example.bonairecontrol.ModelosApi.VWEquiposPorUsuarioModel;
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

public class SmokeListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "DispositivosVinculados";
    private BluetoothAdapter mBtAdapter;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    Context context = getContext();
    private ImageView image_backgroud_devices;
    private ListView listView;
    private ProgressBar progressBar;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread MyConexionBT;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final int handlerState = 0;
    Handler bluetoothIn;
    final private List<ModelDevices> lst = new ArrayList<>();
    List<VWEquiposPorUsuarioModel> lstEquipos = null;

    private DevicesAdapter adapter;
    private View root = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_smoke_list, container, false);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    char MyCaracter = (char) msg.obj;

                    Log.e("DATO", "" + MyCaracter);
                }
            }
        };
        listView = (ListView) root.findViewById(R.id.lv_deshumidificador);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_deshumidificador);
        image_backgroud_devices = (ImageView) root.findViewById(R.id.image_backgroud_deshumidificador);
        listView.setOnItemClickListener(this);

        try {
            adapter = new DevicesAdapter(getContext(), R.layout.list_devices, lst);
        } catch (Exception ignored) {
        }
        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter

        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);
        if (verificarEstadoBT()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    fncContruirLista(lst);
                    fncRevisarLista(lst);
                }
            }, 3500);
        }

        fncTimerBluetooth();
        listView.setAdapter(null);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                fncContruirLista(lst);
                fncRevisarLista(lst);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, 5000);
    }

    private void fncContruirLista(List<ModelDevices> lst) {

        try {
            lst.clear();
            listView.setAdapter(null);
            progressBar.setVisibility(View.VISIBLE);

            //agregar validacion internet o bluetooth
            Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    //if (device.getName().contains("BONAIRE_TECH")) {
                        lst.add(new ModelDevices(device.getName().toString(), "activo", "NA",
                                "bluetooth", "local", device.getAddress(), R.drawable.ico_bluetooth, ""));
                    //}
                }
            }
            fncRevisarLista(lst);
            adapter = new DevicesAdapter(getContext(), R.layout.list_devices, lst);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setAdapter(adapter);

            fncRevisarLista(lst);
        } catch (Exception ignored) {
        }
    }

    private void fncRevisarLista(List<ModelDevices> lst) {
        if (lst.size() != 0) {

            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
            image_backgroud_devices.setVisibility(View.INVISIBLE);
        } else {

            listView.setVisibility(View.INVISIBLE);
            image_backgroud_devices.setVisibility(View.VISIBLE);
        }
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);

        progressBar.setVisibility(View.INVISIBLE);
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

    private boolean verificarEstadoBT() {
        // Comprueba que el dispositivo tiene Bluetooth y que está encendido.
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(getContext(), "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (mBtAdapter.isEnabled()) {
                /*Log.d(TAG, "...Bluetooth Activado...");*/
                return true;
            } else {
                //Solicita al usuario que active Bluetooth
                try {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                    return true;
                } catch (Exception e) {
                    //Toast.makeText(getContext(), "Si el Bluetooth no se encuentra activado por favor activelo", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences sharPref = getActivity().getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        String tipo = adapter.getItem(position).getTipoEquipo();
        String tipoConexion = adapter.getItem(position).getTipoConexion();
        String address = adapter.getItem(position).getTipoEquipo();
        String name = adapter.getItem(position).getMac();
        String location = adapter.getItem(position).getUbicacion();

        if (tipoConexion.equals("bluetooth")) {
            String strNombre = adapter.getItem(position).getNombreEquipo();
            editor.putInt("transacBtToActivity", 4);
            editor.putString("BtMacString", address);
            editor.putString("BtNombreEquipo", adapter.getItem(position).getNombreEquipo());
            editor.apply();
            Intent intent = new Intent(getContext(), transacBtDeviceListActivity.class);
            Toast.makeText(getContext(), "Probando conexión con el equipo", Toast.LENGTH_LONG).show();
            startActivity(intent);
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