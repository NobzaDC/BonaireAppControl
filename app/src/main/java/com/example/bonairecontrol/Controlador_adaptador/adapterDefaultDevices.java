package com.example.bonairecontrol.Controlador_adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.Modelos.ModelDefaultDevices;
import com.example.bonairecontrol.R;

import java.util.List;

public class adapterDefaultDevices extends RecyclerView.Adapter<adapterDefaultDevices.ViewHolderDefaultDevices> implements View.OnClickListener {
    List<ModelDefaultDevices> lstDevices;
    Context context;
    View.OnClickListener listener;

    public adapterDefaultDevices(List<ModelDefaultDevices> lstDevices, Context context){
        this.lstDevices = lstDevices;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterDefaultDevices.ViewHolderDefaultDevices onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_default_devices, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDefaultDevices(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterDefaultDevices.ViewHolderDefaultDevices holder, int position) {
        int vImage = R.drawable.logo_bonaire_app_sin_rs;
        switch (lstDevices.get(position).getTipo()){
            case "Bonaire-BQH":
                vImage = R.drawable.eliminar_humo_ico;
                break;
            case "Bonaire-BQD":
                vImage = R.drawable.bqd;
                break;
            case "Bonaire-BQV":
                vImage = R.drawable.bqv;
                break;
            case "Bonaire-TECH":
                vImage = R.drawable.b_tech;
                break;
            case "COCINAS INTELIGENTES":
                vImage = R.drawable.b_tech;
                break;
        }

        holder.image.setImageResource(vImage);
        holder.etTitle.setText(lstDevices.get(position).getNombre());
        holder.etMac.setText(lstDevices.get(position).getMac());
    }

    @Override
    public int getItemCount() {
        return lstDevices.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!= null)
            listener.onClick(v);
    }

    public class ViewHolderDefaultDevices extends RecyclerView.ViewHolder {

        ImageView image;
        TextView etTitle, etMac;

        public ViewHolderDefaultDevices(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.img_list_default_device);
            etTitle = itemView.findViewById(R.id.tv_nombre_device);
            etMac = itemView.findViewById(R.id.tv_mac_device);
        }
    }
}
