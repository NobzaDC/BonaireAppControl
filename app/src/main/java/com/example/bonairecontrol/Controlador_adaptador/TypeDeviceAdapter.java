package com.example.bonairecontrol.Controlador_adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bonairecontrol.Modelos.ModelTypeDevices;
import com.example.bonairecontrol.R;

import java.util.List;

public class TypeDeviceAdapter extends ArrayAdapter<ModelTypeDevices> {
    private List<ModelTypeDevices> typeDevices;
    private Context context;
    private int resourceLayout;

    public TypeDeviceAdapter(@NonNull Context context, int resource, @NonNull List<ModelTypeDevices> objects) {
        super(context, resource, objects);

        this.typeDevices = objects;
        this.context = context;
        this.resourceLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_type_devices, null);

        ModelTypeDevices modelType = typeDevices.get(position);

        ImageView image = view.findViewById(R.id.img_type_devices);
        image.setImageResource(modelType.getImg());

        TextView textView = view.findViewById(R.id.tv_name_type_device);
        textView.setText(modelType.getNameType());

        return view;
    }
}
