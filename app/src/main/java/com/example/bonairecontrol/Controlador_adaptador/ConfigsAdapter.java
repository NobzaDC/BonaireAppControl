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

public class ConfigsAdapter extends RecyclerView.Adapter<ConfigsAdapter.ViewHolderConfigs>
        implements View.OnClickListener{

    ArrayList<ModelConfigs> lst;
    private View.OnClickListener listener;

    public ConfigsAdapter(ArrayList<ModelConfigs> lst) {
        this.lst = lst;
    }

    @Override
    public ViewHolderConfigs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_configs, null, false);
        view.setOnClickListener(this);
        return new ViewHolderConfigs(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderConfigs holder, int position) {

        holder.img_list_configs.setImageResource(lst.get(position).getImg_type_configs());
        holder.txt_title_list_configs.setText(lst.get(position).getStr_title_configs());
        holder.txt_description_list_configs.setText(lst.get(position).getStr_description_configs());

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

    public class ViewHolderConfigs extends RecyclerView.ViewHolder {

        ImageView img_list_configs;
        TextView txt_title_list_configs, txt_description_list_configs;

        public ViewHolderConfigs(@NonNull View itemView) {
            super(itemView);

            img_list_configs = (ImageView)itemView.findViewById(R.id.img_list_configs);
            txt_title_list_configs = (TextView)itemView.findViewById(R.id.txt_title_list_configs);
            txt_description_list_configs = (TextView)itemView.findViewById(R.id.txt_description_list_configs);

        }
    }
}
