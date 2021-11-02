package com.example.bonairecontrol.new_adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.Modelos.ModelListDevices;
import com.example.bonairecontrol.R;

import java.util.List;

public class sensoresAdapter extends RecyclerView.Adapter<sensoresAdapter.ViewHolderBq600> implements View.OnClickListener{
    List<ModelListDevices> list;
    private View.OnClickListener listener;
    public sensoresAdapter(List<ModelListDevices> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public sensoresAdapter.ViewHolderBq600 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_devices, null, false);
        view.setOnClickListener(this);
        return new ViewHolderBq600(view);
    }

    @Override
    public void onBindViewHolder(@NonNull sensoresAdapter.ViewHolderBq600 holder, int position) {

        try {
            holder.image.setImageResource(list.get(position).getImage());
            holder.title.setText(list.get(position).getTitle());
            holder.statusTitle.setText(list.get(position).getStatusTitle());
            holder.statusData.setText(list.get(position).getStatusData());
            holder.lastReportTitle.setText(list.get(position).getLastReportTitle());
            holder.lastReportData.setText(list.get(position).getLastReportData());
            holder.idTitle.setText(list.get(position).getIdTitle());
            holder.idData.setText(list.get(position).getIdData());
            holder.bottomLeft.setText(list.get(position).getBottomLeft());
            holder.bottomRight.setText(list.get(position).getBottomRight());
        }catch (Exception ignore){

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public static class ViewHolderBq600 extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, statusTitle, statusData, lastReportTitle, lastReportData,
        idTitle, idData, bottomLeft, bottomRight;
        public ViewHolderBq600(@NonNull View itemView) {
            super(itemView);
            image  = (ImageView)itemView.findViewById(R.id.img_card_view_list_devices);
            title = (TextView) itemView.findViewById(R.id.txt_location_data_list_device);
            statusTitle = (TextView) itemView.findViewById(R.id.txt_status_device_list_devices);
            statusData = (TextView) itemView.findViewById(R.id.txt_status_data_device_list_devices);
            lastReportTitle = (TextView) itemView.findViewById(R.id.txt_last_report_list_device);
            lastReportData = (TextView) itemView.findViewById(R.id.txt_last_report_data_list_device);
            idTitle  = (TextView) itemView.findViewById(R.id.txt_location_list_device);
            idData = (TextView) itemView.findViewById(R.id.txt_title_device_list_devices);
            bottomLeft = (TextView) itemView.findViewById(R.id.txt_type_device_list_device);
            bottomRight = (TextView) itemView.findViewById(R.id.txt_conection_type_list_device);
        }
    }
}
