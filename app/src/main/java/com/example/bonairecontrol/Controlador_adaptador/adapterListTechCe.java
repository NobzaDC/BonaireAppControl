package com.example.bonairecontrol.Controlador_adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.ModelosApi.VWSensorReporteModel;
import com.example.bonairecontrol.R;

import java.util.List;

public class adapterListTechCe extends RecyclerView.Adapter<adapterListTechCe.ViewHolderSensores> {
    List<VWSensorReporteModel> sensorReporteModels;

    public adapterListTechCe(List<VWSensorReporteModel> sensorReporteModels) {
        this.sensorReporteModels = sensorReporteModels;
    }

    @NonNull
    @Override
    public ViewHolderSensores onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tech_ce, null, false);
        return new ViewHolderSensores(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSensores holder, int position) {
        holder.sensorName.setText(sensorReporteModels.get(position).getNombre());
        holder.sensorData.setText(String.format("%s %s", sensorReporteModels.get(position).getValorReportado(), sensorReporteModels.get(position).getUndMedicion()));
    }

    @Override
    public int getItemCount() {
        return sensorReporteModels.size();
    }

    public class ViewHolderSensores extends RecyclerView.ViewHolder {
        TextView sensorName, sensorData;
        public ViewHolderSensores(@NonNull View itemView) {
            super(itemView);

            sensorName = itemView.findViewById(R.id.txt_name_sensor_list_tech_ce);
            sensorData = itemView.findViewById(R.id.txt_data_list_tech_ce);

        }
    }
}
