package com.example.bonairecontrol.bottomSheet;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bonairecontrol.Clases.WifiReceiver;
import com.example.bonairecontrol.Controlador_adaptador.ListWifiAdapter;
import com.example.bonairecontrol.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class bottomSheetNetworksWifi extends BottomSheetDialogFragment {

    private WifiManager wifiManager;
    WifiReceiver wifiReceiver;
    ListWifiAdapter adapter;
    private ListView wifiList;
    List list;

    private BottomSheetListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_wifi_networks, container, false);

        wifiList = view.findViewById(R.id.lv_networks);
        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        //wifiReceiver = new WifiReceiver(wifiManager, wifiList);
        wifiReceiver = new WifiReceiver();

        getActivity().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
/*
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getContext(), "Encendiendo el WiFi...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }*/

        wifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter.getItem(position).toString();
                listener.onFieldClicked(name);
                dismiss();
            }
        });

        return view;
    }

    public interface BottomSheetListener{
        void onFieldClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BottomSheetListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "Debe implementar listener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }else{
            getWifi();
        }
        /*if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            wifiManager.startScan();
        }


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        requireActivity().registerReceiver(wifiReceiver, intentFilter);
        getWifi();*/
    }


    private void getWifi() {
        wifiManager.startScan();
        list = wifiManager.getScanResults();
        adapter = new ListWifiAdapter(getContext(), list);
        wifiList.setAdapter(adapter);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(getContext(), "Su version de android no es compatible con esta función", Toast.LENGTH_SHORT).show();
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(getContext(), "Desactivando ubicación", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            } else {
                //Toast.makeText(getContext(), "Encendiendo ubicación", Toast.LENGTH_SHORT).show();
                wifiManager.startScan();
            }
        } else {
            Toast.makeText(getContext(), "Escaneando", Toast.LENGTH_SHORT).show();
            wifiManager.startScan();
        }*/
    }
    @Override
    public void onPause() {
        super.onPause();
        /*requireActivity().unregisterReceiver(wifiReceiver);*/
    }
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "permission granted", Toast.LENGTH_SHORT).show();
                    wifiManager.startScan();
                } else {
                    Toast.makeText(getContext(), "permission not granted", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }*/
}
