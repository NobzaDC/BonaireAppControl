package com.example.bonairecontrol.Clases;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WifiReceiver extends BroadcastReceiver {

    /*WifiManager wifiManager;
    ListView wifiDeviceList;
    List<String> deviceList;*/

    /*public WifiReceiver(WifiManager wifiManager, ListView wifiDeviceList) {
        this.wifiManager = wifiManager;
        this.wifiDeviceList = wifiDeviceList;
    }*/


    public void onReceive(Context context, Intent intent) {
        /*String action = intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
            List<ScanResult> wifiList = wifiManager.getScanResults();
            deviceList = new ArrayList<>();
            for (ScanResult scanResult : wifiList) {
                if (scanResult.SSID!= null && scanResult.SSID!="")

                deviceList.add(scanResult.SSID);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, deviceList.toArray());
            wifiDeviceList.setAdapter(arrayAdapter);
        }*/
    }

    /*public String getNetworksList(int position){
        return deviceList.get(position);
    }*/

}
