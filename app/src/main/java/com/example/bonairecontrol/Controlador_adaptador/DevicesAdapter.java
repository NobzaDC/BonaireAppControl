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

import com.example.bonairecontrol.Modelos.ModelDevices;
import com.example.bonairecontrol.R;

import java.util.List;

public class DevicesAdapter  extends ArrayAdapter<ModelDevices> {
    private List<ModelDevices> devicesList;
    private Context context;
    private int resourceLayout;

    public DevicesAdapter(@NonNull Context context, int resource, @NonNull List<ModelDevices> objects) {
        super(context, resource, objects);

        this.devicesList = objects;
        this.context = context;
        this.resourceLayout = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view ==null)
            view = LayoutInflater.from(context).inflate(R.layout.list_devices, null);

        ModelDevices modelDevices = devicesList.get(position);

        ImageView imagen = view.findViewById(R.id.img_card_view_list_devices);
        imagen.setImageResource(modelDevices.getImagen());

        TextView NombreEquipo = view.findViewById(R.id.txt_title_device_list_devices);
        NombreEquipo.setText(modelDevices.getNombreEquipo());

        TextView Estado = view.findViewById(R.id.txt_status_data_device_list_devices);
        Estado.setText(modelDevices.getEstado());

        TextView UltimoReporte = view.findViewById(R.id.txt_last_report_data_list_device);
        UltimoReporte.setText(modelDevices.getUltimoReporte());

        TextView Ubicacion = view.findViewById(R.id.txt_location_data_list_device);
        Ubicacion.setText(modelDevices.getUbicacion());

        TextView TipoConexion = view.findViewById(R.id.txt_conection_type_list_device);
        TipoConexion.setText(modelDevices.getTipoConexion());

        TextView TipoEquipo = view.findViewById(R.id.txt_type_device_list_device);
        TipoEquipo.setText(modelDevices.getTipoEquipo());

        return view;
    }
}
