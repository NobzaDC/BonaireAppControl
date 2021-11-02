package com.example.bonairecontrol.Controlador_adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.Clases.EquiposHome;
import com.example.bonairecontrol.R;

import java.util.ArrayList;

public class adaptadorEquiposHome extends RecyclerView.Adapter<adaptadorEquiposHome.ViewHolderEquipos> implements View.OnClickListener{

    ArrayList<EquiposHome> listEquipos;
    private View.OnClickListener listener;

    public adaptadorEquiposHome(ArrayList<EquiposHome> listEquipos) {
        this.listEquipos = listEquipos;
    }

    @NonNull
    @Override
    public adaptadorEquiposHome.ViewHolderEquipos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_equipos, null, false);
        view.setOnClickListener(this);
        return new ViewHolderEquipos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptadorEquiposHome.ViewHolderEquipos holder, int position) {
        holder.tvTitle.setText(listEquipos.get(position).getTitle());
        holder.tvDescription.setText(listEquipos.get(position).getDescription());
        holder.ivEquipo.setImageResource(listEquipos.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return listEquipos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!= null){
            listener.onClick(v);
        }
    }

    public class ViewHolderEquipos extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription;
        ImageView ivEquipo;

        public ViewHolderEquipos(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.txt_title_list_equipos);
            tvDescription = itemView.findViewById(R.id.txt_description_list_equipos);
            ivEquipo = itemView.findViewById(R.id.img_list_equipos);
        }
    }
}
