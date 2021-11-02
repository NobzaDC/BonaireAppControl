package com.example.bonairecontrol.bottomSheet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.bonairecontrol.Controlador_adaptador.DevicesAdapter;
import com.example.bonairecontrol.Modelos.ModelDevices;
import com.example.bonairecontrol.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BottomSheetListDevices extends BottomSheetDialogFragment implements AdapterView.OnItemClickListener{

    private ListView lvDevices;
    private BluetoothAdapter btAdapter = null;
    private DevicesAdapter adapter;

    private BottomSheetDevicesListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_list_devices, container, false);

        lvDevices = view.findViewById(R.id.lv_devices);

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        List<ModelDevices> lst = new ArrayList<>();

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            //if (device.getName().contains("BONAIRE_TECH")) {
            lst.add(new ModelDevices(device.getName().toString(), "activo", "NA",
                    "bluetooth", "local", device.getAddress(), R.drawable.ico_bluetooth, ""));
            //}
        }

        adapter = new DevicesAdapter(getContext(), R.layout.list_devices, lst);
        lvDevices.setAdapter(adapter);
        lvDevices.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BottomSheetDevicesListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "Debe implementar listener");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String mac = adapter.getItem(position).getTipoEquipo();
        listener.onFieldClicked(mac);
        dismiss();
    }

    public interface BottomSheetDevicesListener{
        void onFieldClicked(String mac_name);
    }
}
