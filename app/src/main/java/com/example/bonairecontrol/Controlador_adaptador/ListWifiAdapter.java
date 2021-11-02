package com.example.bonairecontrol.Controlador_adaptador;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bonairecontrol.R;

import java.util.List;

public class ListWifiAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<ScanResult> wifiResult;

    public ListWifiAdapter(Context context, List<ScanResult> wifiResult) {
        this.context = context;
        this.wifiResult = wifiResult;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return wifiResult.size();
    }

    @Override
    public Object getItem(int position) {
        return wifiResult.get(position).SSID;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        View v = convertView;

        if (v==null){
            v = inflater.inflate(R.layout.list_wifi_names, null);
            holder = new Holder();
            holder.tvDetails = (TextView)v.findViewById(R.id.txt_wifi_name);
            v.setTag(holder);
        }else {
            holder = (Holder)v.getTag();
        }

        holder.tvDetails.setText(wifiResult.get(position).SSID);

        return v;
    }

    class Holder{
        TextView tvDetails;
    }
}
