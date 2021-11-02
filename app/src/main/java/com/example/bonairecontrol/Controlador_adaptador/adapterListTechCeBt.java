package com.example.bonairecontrol.Controlador_adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.Modelos.ModelConfigs;
import com.example.bonairecontrol.R;

import java.util.ArrayList;

public class adapterListTechCeBt extends RecyclerView.Adapter<adapterListTechCeBt.ViewHolderTechCeBt> {
    ArrayList<ModelConfigs> list;

    public adapterListTechCeBt(ArrayList<ModelConfigs> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public adapterListTechCeBt.ViewHolderTechCeBt onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_configs, null, false);
        return new ViewHolderTechCeBt(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterListTechCeBt.ViewHolderTechCeBt holder, int position) {

        holder.image.setImageResource(list.get(position).getImg_type_configs());
        holder.title.setText(list.get(position).getStr_title_configs());
        holder.title.setTextSize(12);
        holder.description.setText(list.get(position).getStr_description_configs());
        holder.description.setTextSize(28);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderTechCeBt extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, description;

        public ViewHolderTechCeBt(@NonNull View itemView) {
            super(itemView);

            image = (ImageView)itemView.findViewById(R.id.img_list_configs);
            title = (TextView)itemView.findViewById(R.id.txt_title_list_configs);
            description = (TextView)itemView.findViewById(R.id.txt_description_list_configs);

        }
    }
}
