package com.example.bonairecontrol.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bonairecontrol.Controlador_adaptador.ConfigsAdapter;
import com.example.bonairecontrol.Modelos.ModelConfigs;
import com.example.bonairecontrol.R;
import com.example.bonairecontrol.configs_activity.AutomaticActivity;
import com.example.bonairecontrol.configs_activity.BqhConfigActivity;
import com.example.bonairecontrol.configs_activity.DefaultDevicesActivity;
import com.example.bonairecontrol.configs_activity.DimerActivity;
import com.example.bonairecontrol.configs_activity.NameActivity;
import com.example.bonairecontrol.configs_activity.PuntosDeCorteActivity;
import com.example.bonairecontrol.configs_activity.RelesActivity;
import com.example.bonairecontrol.configs_activity.broker_activity;
import com.example.bonairecontrol.configs_activity.date_time_activity;
import com.example.bonairecontrol.configs_activity.desinfeccion_activity;
import com.example.bonairecontrol.configs_activity.dht_activity;
import com.example.bonairecontrol.configs_activity.internet_activity;
import com.example.bonairecontrol.configs_activity.manual_activity;
import com.example.bonairecontrol.configs_activity.ozono_activity;
import com.example.bonairecontrol.configs_activity.purificacion_activity;
import com.example.bonairecontrol.configs_activity.pwm_servo_activity;
import com.example.bonairecontrol.configs_activity.servo_activity;
import com.example.bonairecontrol.configs_activity.settings_activity;
import com.example.bonairecontrol.configs_activity.time_activity;
import com.example.bonairecontrol.consoleActivity;

import java.util.ArrayList;

public class fragment_recicle_configs extends Fragment {


    ArrayList<ModelConfigs> list;
    androidx.recyclerview.widget.RecyclerView recicle_fragment_configs;
    ConfigsAdapter adapter;

    Intent intent = null;

    Activity activity;


    public fragment_recicle_configs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recicle_configs, container, false);
        recicle_fragment_configs = view.findViewById(R.id.recicle_fragment_configs);
        recicle_fragment_configs.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<ModelConfigs>();
        return view;
    }

    private void LlenarListaConfigs() {

        list.add(new ModelConfigs(12, R.drawable.logo_bonaire_app_sin_rs, "OPERACIÓN MANUAL", "Permite controlar los puertos y los integrados de función de manera manual."));
        list.add(new ModelConfigs(1, R.drawable.baseline_calendar_today_24, "FECHA Y HORA", "Permite ajustar la fecha y hora del reloj interno del equipo."));
        list.add(new ModelConfigs(7, R.drawable.ic_baseline_wifi_24, "INTERNET", "Almacenar o actualizar los parámetros de conexión de la red wifi a la que va a estar conectada el equipo."));
        list.add(new ModelConfigs(2, R.drawable.baseline_cloud_circle_24, "BROKER", "Almacenar o actualizar los parámetros de conexión del equipo por MQTT."));
        list.add(new ModelConfigs(11, R.drawable.logo_bonaire_app_sin_rs, "FRECUENCIA Y TIEMPO ", "Almacenar o actualizar los tiempos para la toma de datos y la frecuencia con la que el equipo va a reportarlos a la plataforma por MQTT."));
        list.add(new ModelConfigs(10, R.drawable.baseline_settings_24, "VALORES DE AJUSTE", "Especifique los valores a ajustar de cada una de las variable que reporta el equipo."));
        list.add(new ModelConfigs(3, R.drawable.baseline_blur_circular_24, "DESINFECCIÓN", "Permite especificar los parámetros de inicio de la función desinfección."));
        list.add(new ModelConfigs(4, R.drawable.baseline_blur_linear_24, "PURIFICACIÓN", "Permite especificar los parámetros de inicio de la función purificación."));
        list.add(new ModelConfigs(5, R.drawable.baseline_device_hub_24, "DHT", "Especificar el tipo de sensor instalado en el equipo para temperatura y humedad."));
        list.add(new ModelConfigs(6, R.drawable.baseline_scatter_plot_24, "OZONO", "Cambiar la configuración del tiempo de recolección de la muestra del sensor."));
        list.add(new ModelConfigs(8, R.drawable.baseline_dns_24, "PWM", "Modificar las configuraciones y valores de la regresión polinómica del PWM."));
        list.add(new ModelConfigs(9, R.drawable.ic_baseline_servo, "SERVO", "Establecer la posición 0 de calibración del SERVO en el equipo."));
        list.add(new ModelConfigs(13, R.drawable.logo_bonaire_app_sin_rs, "CONSOLA", "Conexión a la consola bluetooth del equipo."));
        list.add(new ModelConfigs(14, R.drawable.logo_bonaire_app_sin_rs, "RELES", "Configuracion de comandos enviados por los reles."));
        list.add(new ModelConfigs(15, R.drawable.logo_bonaire_app_sin_rs, "VALORES LIMITE", "Configuracion de los puntos de corte del equipo."));
        list.add(new ModelConfigs(16, R.drawable.logo_bonaire_app_sin_rs, "DIMER", "Configuracion del dimer."));
        list.add(new ModelConfigs(17, R.drawable.logo_bonaire_app_sin_rs, "NOMBRE", "Cambiar nombre del equipo."));
        list.add(new ModelConfigs(18, R.drawable.logo_bonaire_app_sin_rs, "EQUIPOS PREDEFINIDOS", "Seleccione los equipos que usara de forma predefenida."));
        list.add(new ModelConfigs(19, R.drawable.logo_bonaire_app_sin_rs, "AUTOMATIZACIÓN DEL EQUIPO", "Activar o descarivar la automatizacion del equipo."));
        list.add(new ModelConfigs(20, R.drawable.logo_bonaire_app_sin_rs, "CONFIGURACIÓN BQH", "Configurar los reles del equipo bqh."));

        adapter = new ConfigsAdapter(list);
        recicle_fragment_configs.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int IdActivity = list.get(recicle_fragment_configs.getChildAdapterPosition(v)).getId();
                switch (IdActivity) {
                    case 1:
                        intent = new Intent(getContext(), date_time_activity.class);
                        break;
                    case 2:
                        intent = new Intent(getContext(), broker_activity.class);
                        break;
                    case 3:
                        intent = new Intent(getContext(), desinfeccion_activity.class);
                        break;
                    case 4:
                        intent = new Intent(getContext(), purificacion_activity.class);
                        break;
                    case 5:
                        intent = new Intent(getContext(), dht_activity.class);
                        break;
                    case 6:
                        intent = new Intent(getContext(), ozono_activity.class);
                        break;
                    case 7:
                        intent = new Intent(getContext(), internet_activity.class);
                        break;
                    case 8:
                        intent = new Intent(getContext(), pwm_servo_activity.class);
                        break;
                    case 9:
                        intent = new Intent(getContext(), servo_activity.class);
                        break;
                    case 10:
                        intent = new Intent(getContext(), settings_activity.class);
                        break;
                    case 11:
                        intent = new Intent(getContext(), time_activity.class);
                        break;
                    case 12:
                        intent = new Intent(getContext(), manual_activity.class);
                        break;
                    case 13:
                        intent = new Intent(getContext(), consoleActivity.class);
                        break;
                    case 14:
                        intent = new Intent(getContext(), RelesActivity.class);
                        break;
                    case 15:
                        intent = new Intent(getContext(), PuntosDeCorteActivity.class);
                        break;
                    case 16:
                        intent = new Intent(getContext(), DimerActivity.class);
                        break;
                    case 17:
                        intent = new Intent(getContext(), NameActivity.class);
                        break;
                    case 18:
                        intent = new Intent(getContext(), DefaultDevicesActivity.class);
                        break;
                    case 19:
                        intent = new Intent(getContext(), AutomaticActivity.class);
                        break;
                    case 20:
                        intent = new Intent(getContext(), BqhConfigActivity.class);
                        break;
                }
                startActivity(intent);

                //Toast.makeText(getContext(), "Valor id: " + IdActivity, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LlenarListaConfigs();
    }
}