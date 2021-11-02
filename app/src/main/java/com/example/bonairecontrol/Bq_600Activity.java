package com.example.bonairecontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bonairecontrol.InterfazApi.MensajesMqttApiCaller;
import com.example.bonairecontrol.InterfazApi.VWSensorReporteApiCaller;
import com.example.bonairecontrol.Modelos.ModelListDevices;
import com.example.bonairecontrol.ModelosApi.MensajesMqttModel;
import com.example.bonairecontrol.ModelosApi.VWSensorReporteModel;
import com.example.bonairecontrol.graphics.graphicsActivity;
import com.example.bonairecontrol.new_adapters.sensoresAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Bq_600Activity extends AppCompatActivity {

    private TextView txt_data_ultimo_reporte, txt_nombre_equipo_bq_600, tvLocation, tvReload;
    private Switch purificacion, desinfeccion, apagado;
    private Button bntExec;

    int timer = 0;
    String UrlBase = "", macName, Topico, macAddress, time, name, location;
    Context context = this;
    Retrofit retrofit = null;
    List<VWSensorReporteModel> lstDatos = null;
    List<ModelListDevices> listDevices = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sensoresAdapter adapter;
    Date lastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bq_600);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        macName = sharPref.getString("MacString", "");
        location = sharPref.getString("LocationDevice", "");

        Topico = "BQ_600/" + macName;

        macAddress = getPublicIPAddress(Bq_600Activity.this);


        UrlBase = getString(R.string.url_base_api);

        retrofit = new Retrofit.Builder().baseUrl(UrlBase)
                .addConverterFactory(GsonConverterFactory.create()).build();

        txt_data_ultimo_reporte = findViewById(R.id.txt_data_ultimo_reporte);
        txt_nombre_equipo_bq_600 = findViewById(R.id.txt_nombre_equipo_bq_600);
        tvReload = findViewById(R.id.tv_next_reload_bq_600);
        bntExec = findViewById(R.id.btn_exec_action);
        purificacion = findViewById(R.id.switch_purificacion_bq_600);
        apagado = findViewById(R.id.switch_apagado_bq_600);
        tvLocation = findViewById(R.id.txt_location_bq_600);
        desinfeccion = findViewById(R.id.switch_desinfeccion_bq_600);
        recyclerView = findViewById(R.id.rv_data_bq600);
        swipeRefreshLayout = findViewById(R.id.sr_bq_600);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        name = "" + macName.charAt(macName.length()-5) + macName.charAt(macName.length()-4) + macName.charAt(macName.length()-3) + macName.charAt(macName.length()-2) + macName.charAt(macName.length()-1);
        tvLocation.setText(location);
        txt_nombre_equipo_bq_600.setText("B-Q600");

        fncRecargarDatos();
        purificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!purificacion.isChecked()){
                    if (desinfeccion.isChecked()){
                        desinfeccion.setChecked(false);
                    }
                }
            }
        });

        desinfeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desinfeccion.isChecked()){
                    if (!purificacion.isChecked()){
                        purificacion.setChecked(true);
                    }
                }
            }
        });

        apagado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apagado.isChecked()){
                    purificacion.setChecked(false);
                    desinfeccion.setChecked(false);
                }
            }
        });

        bntExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if (purificacion.isChecked()){
                    message = "PURIFICACION_BEGIN";
                }
                if (desinfeccion.isChecked()){
                    message = "DESINFECCION_BEGIN";
                }
                if (apagado.isChecked()){
                    fncCrearMensaje("PURIFICACION_STOP");
                    fncCrearMensaje("DESINFECCION_STOP");
                }
                if (message.isEmpty()){
                    Toast.makeText(getBaseContext(), "Debe seleccionar alguna de las opciones", Toast.LENGTH_SHORT).show();
                }else {
                    fncCrearMensaje(message);
                    message = "";
                }
                purificacion.setChecked(false);
                desinfeccion.setChecked(false);
                apagado.setChecked(false);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                timer = 1;
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public static String getPublicIPAddress(Context context) {
        //final NetworkInfo info = NetworkUtils.getNetworkInfo(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();

        RunnableFuture<String> futureRun = new FutureTask<>(() -> {
            if ((info != null && info.isAvailable()) && (info.isConnected())) {
                StringBuilder response = new StringBuilder();

                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) (
                            new URL("http://checkip.amazonaws.com/").openConnection());
                    urlConnection.setRequestProperty("User-Agent", "Android-device");
                    //urlConnection.setRequestProperty("Connection", "close");
                    urlConnection.setReadTimeout(15000);
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    urlConnection.connect();

                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                    }
                    urlConnection.disconnect();
                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //Log.w(TAG, "No network available INTERNET OFF!");
                return null;
            }
            return null;
        });

        new Thread(futureRun).start();

        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fncCrearMensaje(String mensaje) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String DateTime = df.format(c.getTime());

        MensajesMqttModel post = new MensajesMqttModel();

        post.setIdRegistro(1);
        post.setMensaje(mensaje);
        post.setEstado(false);
        post.setFechaRecibido(DateTime);
        post.setOrigen("E");
        post.setTopico(Topico);
        post.setIpComando(macAddress);
        post.setDispositivo("APP");

        fncEnviarPost(post);

    }

    private void fncEnviarPost(MensajesMqttModel post) {

        MensajesMqttApiCaller caller = retrofit.create(MensajesMqttApiCaller.class);
        Call<MensajesMqttModel> mensajescall = caller.post_send_mensaje(post);
        mensajescall.enqueue(new Callback<MensajesMqttModel>() {
            @Override
            public void onResponse(Call<MensajesMqttModel> call, Response<MensajesMqttModel> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Comando enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Se produjo un error al enviar el comando", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MensajesMqttModel> call, Throwable t) {
                Toast.makeText(context, "Error de comunicación con el servidor", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fncRecargarDatos(){
        Timer t = new Timer();
        final TimerTask verificacionDatos = new TimerTask() {
            @Override
            public void run() {
                verificarDatos();
            }
        };
        t.schedule(verificacionDatos, 0, 1000);
    }

    private void verificarDatos(){
        if (timer == 0){
            timer = 60;
            listDevices.clear();
            VWSensorReporteApiCaller caller = retrofit.create(VWSensorReporteApiCaller.class);
            Call<List<VWSensorReporteModel>> sensorcall = caller.GetListaResportes(macName);
            sensorcall.enqueue(new Callback<List<VWSensorReporteModel>>() {
                @Override
                public void onResponse(Call<List<VWSensorReporteModel>> call, Response<List<VWSensorReporteModel>> response) {
                    if (response.isSuccessful()){
                        lstDatos = response.body();
                        for (VWSensorReporteModel i: lstDatos){
                            String value = i.getValorReportado() + " " + i.getUndMedicion();
                            String date = i.getFechaReporte();
                            if (date!=null){
                                date = date.replace("T", " ");
                            }else {
                                date = "No se encontro un ultimo reporte";
                            }
                            switch(i.getIdParametroUi()){
                                case "T":
                                    listDevices.add(new ModelListDevices(R.drawable.temperatura, value,
                                            "Nombre: ", i.getNombre(),"Último reporte: ",
                                            date, "Id: ", name,  "Conexión actual bluetooth, esperando datos…",
                                            "Conectado", i.getIdParametroUi()));
                                    break;
                                case "H":
                                    listDevices.add(new ModelListDevices(R.drawable.humedad, value,
                                            "Nombre: ", i.getNombre(),"Último reporte: ",
                                            date, "Id: ", name,  "Conexión actual bluetooth, esperando datos…",
                                            "Conectado", i.getIdParametroUi()));
                                    break;
                                case "O3":
                                    listDevices.add(new ModelListDevices(R.drawable.o3, value,
                                            "Nombre: ", i.getNombre(),"Último reporte: ",
                                            date, "Id: ", name,  "Conexión actual bluetooth, esperando datos…",
                                            "Conectado", i.getIdParametroUi()));
                                    break;
                            }
                        }
                        adapter = new sensoresAdapter(listDevices);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharPref.edit();
                                Intent graphic = new Intent(context, graphicsActivity.class);
                                editor.putString("tipoSensor", listDevices.get(recyclerView.getChildAdapterPosition(v)).getIdParametroUi());
                                editor.putString("MacString", macName);
                                editor.apply();
                                startActivity(graphic);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<VWSensorReporteModel>> call, Throwable t) {
                }
            });
        }
        tvReload.setText(timer + " seg.");
        timer = timer - 1;
    }
}