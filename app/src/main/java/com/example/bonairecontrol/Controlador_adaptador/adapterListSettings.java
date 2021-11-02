package com.example.bonairecontrol.Controlador_adaptador;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonairecontrol.Modelos.ModelSettings;
import com.example.bonairecontrol.R;

import java.util.List;

public class adapterListSettings extends RecyclerView.Adapter<adapterListSettings.ViewHolderSettings>{
List<ModelSettings> listSettings;
Context context;
String message, text;
private View.OnClickListener listener;


    public adapterListSettings(List<ModelSettings> listSettings, Context context, View.OnClickListener listener) {
        this.listSettings = listSettings;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderSettings onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_settings, null, false);
        return new ViewHolderSettings(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSettings holder, int position) {
        holder.ivImage.setImageResource(listSettings.get(position).getImage());
        holder.tvTitle.setText(listSettings.get(position).getTitle());
        holder.tvDescription.setText(listSettings.get(position).getDescription());
        holder.tvType.setText(listSettings.get(position).getType());

        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.etValue.getText().toString().equals("")){
                    holder.etValue.setError("Debe llenar los campos");
                }else {
                    String value =  listSettings.get(position).getMessage() + holder.etValue.getText().toString();
                    SharedPreferences sharPref = context.getSharedPreferences("BonairePref", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharPref.edit();
                    editor.putString("BluetoothMessage", value);
                    editor.apply();
                    holder.etValue.setText("");
                    listener.onClick(v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSettings.size();
    }

    public class ViewHolderSettings extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvDescription, tvType;
        EditText etValue;
        Button btnSend;
        public ViewHolderSettings(@NonNull View itemView) {
            super(itemView);

            ivImage = (ImageView)itemView.findViewById(R.id.img_settings_list);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title_list_settings);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_description_list_settings);
            tvType = (TextView)itemView.findViewById(R.id.tv_type_sensor);
            etValue = (EditText)itemView.findViewById(R.id.et_data_list_settings);
            btnSend = (Button)itemView.findViewById(R.id.btn_send_list_settings);

        }
    }

}
