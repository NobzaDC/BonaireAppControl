package com.example.bonairecontrol.Controlador_adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.ModelosApi.VWSensorReporteModel;
import com.example.bonairecontrol.R;

import java.util.ArrayList;

public class adapterGraphicsListData
        extends RecyclerView.Adapter<adapterGraphicsListData.ViewHolderDatos>
        implements View.OnClickListener{

    ArrayList<VWSensorReporteModel> lst;
    private View.OnClickListener listener;

    public adapterGraphicsListData(ArrayList<VWSensorReporteModel> lst) {
        this.lst = lst;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sensores_reporte, null, false);

        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        String date = lst.get(position).getFechaReporte();
        if (date!=null){
            date = date.replace("T", " ");
        }else {
            date = "No se encontro un ultimo reporte";
        }

        holder.title.setText(lst.get(position).getNombre());
        holder.date.setText(date);
        holder.value.setText(String.format("%s %s", lst.get(position).getValorReportado(), lst.get(position).getUndMedicion()));
    }

    @Override
    public int getItemCount() {
        return lst.size();
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

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView title, date, value;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.txt_title_list_sensores);
            date = (TextView)itemView.findViewById(R.id.txt_last_report_data_list_device);
            value = (TextView)itemView.findViewById(R.id.txt_data_value_reported_list_sensores);
        }
    }
}
