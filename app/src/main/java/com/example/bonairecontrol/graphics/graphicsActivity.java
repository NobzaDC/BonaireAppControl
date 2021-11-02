package com.example.bonairecontrol.graphics;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.InterfazApi.MacApiCaller;
import com.example.bonairecontrol.InterfazApi.NivelesAmbienteApiCaller;
import com.example.bonairecontrol.InterfazApi.NivelesAmbientexLugarApiCaller;
import com.example.bonairecontrol.InterfazApi.SensorApiCaller;
import com.example.bonairecontrol.InterfazApi.VWSensoresRegistroUndMedicionApiCaller;
import com.example.bonairecontrol.ModelosApi.MacModel;
import com.example.bonairecontrol.ModelosApi.NivelesAmbienteModel;
import com.example.bonairecontrol.ModelosApi.NivelesAmbientexLugarModel;
import com.example.bonairecontrol.ModelosApi.SensorModel;
import com.example.bonairecontrol.ModelosApi.VWSensoresRegistroUndMedicionModel;
import com.example.bonairecontrol.R;
import com.example.bonairecontrol.bottomSheet.BottomSheetLastReport;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class graphicsActivity extends AppCompatActivity {

    private Context context = this;
    private String Mac, Sensor, UrlBase, nameSensor, date, location;
    private LineChart lineChart;
    private Retrofit retrofit;
    private List<VWSensoresRegistroUndMedicionModel> lst;
    private TextView title, ubication, lastReport;
    private ImageButton btnRefresh;
    private Button btnShowDetail;
    private int lugarInstalacion;

    private MacModel macModel;
    private NivelesAmbientexLugarModel nivelesxLugarModel;
    private NivelesAmbienteModel nivelesModel;

    private static final int intColBueno = Color.rgb(8, 255, 0);
    private static final int intColRegular = Color.rgb(255, 255, 0);
    private static final int intColMalo = Color.rgb(255, 139, 0);
    private static final int intColMuyMalo = Color.rgb(255, 0, 0);
    private static final int intColExtreMalo = Color.rgb(74, 35, 90);

    private Float flBueno = 1f;
    private Float flRegular = 2f;
    private Float flMalo = 3f;
    private Float flMuyMalo = 4f;
    private Float flExtreMalo = 5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);

        UrlBase = getString(R.string.url_base_api);

        retrofit = new Retrofit.Builder().baseUrl(UrlBase)
                .addConverterFactory(GsonConverterFactory.create()).build();

        title = (TextView) findViewById(R.id.txt_title_grafico);
        ubication = (TextView) findViewById(R.id.tv_data_ubication_graphics);
        lastReport = (TextView) findViewById(R.id.tv_data_last_report_graphics);
        lineChart = (LineChart) findViewById(R.id.graphic_chart);
        btnRefresh = (ImageButton) findViewById(R.id.btn_refresh_graphic);
        btnShowDetail = (Button) findViewById(R.id.btn_show_detail);

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        Mac = sharPref.getString("MacString", "");
        Sensor = sharPref.getString("tipoSensor", "");
        location = sharPref.getString("locationDevice", "");
        ubication.setText(location);
        fncSetTitulo(Sensor);

        callApis();
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fncCreateGraphic(Mac, Sensor);
            }
        });
        btnShowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDetail();
            }
        });
    }

    private void showBottomSheetDetail() {

        BottomSheetLastReport bottomSheet = new BottomSheetLastReport();
        bottomSheet.show(getSupportFragmentManager(), "lastReportBottomSheet");

    }

    private void callApis() {
        try {
            MacApiCaller macApiCaller = retrofit.create(MacApiCaller.class);
            Call<MacModel> macCall = macApiCaller.get_info_mac(Mac);
            macCall.enqueue(new Callback<MacModel>() {
                @Override
                public void onResponse(Call<MacModel> call, Response<MacModel> response) {
                    if (response.isSuccessful()) {
                        macModel = response.body();
                        lugarInstalacion = macModel.getIdLugarInstalacion();
                    } else {
                        lugarInstalacion = 0;
                    }
                    callReport();
                }

                @Override
                public void onFailure(Call<MacModel> call, Throwable t) {
                    lugarInstalacion = 0;
                    callReport();
                }
            });

        } catch (Exception e) {
            lugarInstalacion = 0;
            callReport();
        }
    }

    private void callReport() {
        if (lugarInstalacion != 0) {
            NivelesAmbientexLugarApiCaller lugarApiCaller = retrofit.create(NivelesAmbientexLugarApiCaller.class);
            Call<NivelesAmbientexLugarModel> lugarModel = lugarApiCaller.getDatos(Sensor, lugarInstalacion);
            lugarModel.enqueue(new Callback<NivelesAmbientexLugarModel>() {
                @Override
                public void onResponse(Call<NivelesAmbientexLugarModel> call, Response<NivelesAmbientexLugarModel> response) {
                    if (response.isSuccessful()) {
                        nivelesxLugarModel = response.body();

                        flBueno = nivelesxLugarModel.getBuena();
                        flRegular = nivelesxLugarModel.getRegular();
                        flMalo = nivelesxLugarModel.getMala();
                        flMuyMalo = nivelesxLugarModel.getAlta();
                        flExtreMalo = nivelesxLugarModel.getCritica();
                        fncCreateGraphic(Mac, Sensor);
                    } else {
                        lugarInstalacion = 0;
                        callReport();
                    }
                }

                @Override
                public void onFailure(Call<NivelesAmbientexLugarModel> call, Throwable t) {
                    lugarInstalacion = 0;
                    callReport();
                }
            });
        } else {
            NivelesAmbienteApiCaller ambienteApiCaller = retrofit.create(NivelesAmbienteApiCaller.class);
            Call<NivelesAmbienteModel> nivelesAmbienteModelCall = ambienteApiCaller.getDatos(Sensor);
            nivelesAmbienteModelCall.enqueue(new Callback<NivelesAmbienteModel>() {
                @Override
                public void onResponse(Call<NivelesAmbienteModel> call, Response<NivelesAmbienteModel> response) {
                    if (response.isSuccessful()) {
                        nivelesModel = response.body();

                        flBueno = nivelesModel.getBuena();
                        flRegular = nivelesModel.getRegular();
                        flMalo = nivelesModel.getMala();
                        flMuyMalo = nivelesModel.getAlta();
                        flExtreMalo = nivelesModel.getCritica();
                    }
                    fncCreateGraphic(Mac, Sensor);
                }

                @Override
                public void onFailure(Call<NivelesAmbienteModel> call, Throwable t) {

                    fncCreateGraphic(Mac, Sensor);
                }
            });
        }
    }

    private void fncCreateGraphic(String mac, String sensor) {
        ArrayList<Entry> DataSet = new ArrayList<Entry>();
        ArrayList<Entry> DSBuena = new ArrayList<Entry>();
        ArrayList<Entry> DSRegular = new ArrayList<Entry>();
        ArrayList<Entry> DSMala = new ArrayList<Entry>();
        ArrayList<Entry> DSMuyMala = new ArrayList<Entry>();
        ArrayList<Entry> DSExtreMala = new ArrayList<Entry>();

        DataSet.clear();

        VWSensoresRegistroUndMedicionApiCaller sensorReporte = retrofit.create(VWSensoresRegistroUndMedicionApiCaller.class);

        Call<List<VWSensoresRegistroUndMedicionModel>> listCall = sensorReporte.getDatos(mac, sensor);
        listCall.enqueue(new Callback<List<VWSensoresRegistroUndMedicionModel>>() {
            @Override
            public void onResponse(Call<List<VWSensoresRegistroUndMedicionModel>> call, Response<List<VWSensoresRegistroUndMedicionModel>> response) {
                String unidad = "xx";
                if (response.isSuccessful()) {
                    lst = response.body();
                    int i = 1, max = 0;
                    for (VWSensoresRegistroUndMedicionModel data : lst) {
                        Float value = Float.parseFloat(data.getValorReportado());
                        DataSet.add(new Entry(i, value));
                        DSBuena.add(new Entry(i, flBueno));
                        DSRegular.add(new Entry(i, flRegular));
                        DSMala.add(new Entry(i, flMalo));
                        DSMuyMala.add(new Entry(i, flMuyMalo));
                        if (value > max) {
                            max = Math.round(value);
                        }
                        i++;
                        unidad = data.getUndMedicion();
                        date = data.getFechaReporte();
                        if (date != null) {
                            date = date.replace("T", " ");
                        } else {
                            date = "No se encontro un ultimo reporte";
                        }
                    }
                    int a = 1;
                    for (VWSensoresRegistroUndMedicionModel data : lst) {
                        //if (max > flExtreMalo) {
                          //  DSExtreMala.add(new Entry(a, max + 100));
                        //} else {
                            DSExtreMala.add(new Entry(a, flExtreMalo));
                        //}
                        a++;
                    }

                    lastReport.setText(date);

                    LineDataSet lineDataSet = new LineDataSet(DataSet, nameSensor);

                    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                    LineDataSet LDSBuena = new LineDataSet(DSBuena, "Buena");
                    LineDataSet LDSRegular = new LineDataSet(DSRegular, "Regular");
                    LineDataSet LDSMala = new LineDataSet(DSMala, "Mala");
                    LineDataSet LDSMuyMala = new LineDataSet(DSMuyMala, "Muy mala");
                    LineDataSet LDSExtreMala = new LineDataSet(DSExtreMala, "Extremadamente mala");

                    LDSBuena.setColors(intColBueno);
                    LDSRegular.setColors(intColRegular);
                    LDSMala.setColors(intColMalo);
                    LDSMuyMala.setColors(intColMuyMalo);
                    LDSExtreMala.setColors(intColExtreMalo);

                    lineDataSet.setDrawFilled(true);

                    iLineDataSets.add(LDSExtreMala);
                    iLineDataSets.add(LDSMuyMala);
                    iLineDataSets.add(LDSMala);
                    iLineDataSets.add(LDSRegular);
                    iLineDataSets.add(LDSBuena);
                    iLineDataSets.add(lineDataSet);

                    LineData lineData = new LineData(iLineDataSets);

                    setChart(lineChart, 1800);

                    lineChart.getDescription().setText("Unidad de medida: " + unidad);
                    lineChart.getDescription().setTextSize(12);
                    lineChart.getDescription().setEnabled(true);
                    lineChart.setVerticalFadingEdgeEnabled(true);
                    lineChart.setDoubleTapToZoomEnabled(true);
                    lineChart.setDrawBorders(true);
                    lineChart.getXAxis().setDrawGridLines(false);
                    lineChart.getAxisLeft().setDrawGridLines(false);
                    legend(lineChart);
                    Xaxis(lineChart.getXAxis());
                    RigthYaxis(lineChart.getAxisRight());
                    lineChart.setData(lineData);
                } else {
                    Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<VWSensoresRegistroUndMedicionModel>> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error Bonaire", t.getMessage());
            }
        });

    }

    private void legend(Chart chart) {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        ArrayList<LegendEntry> entries = new ArrayList<>();
        LegendEntry entry = new LegendEntry();
        entry.label = "Ultimos 10 registros";
        entries.add(entry);
        legend.setCustom(entries);
    }

    private void fncSetTitulo(String sensor) {
        SensorApiCaller sensorApiCaller = retrofit.create(SensorApiCaller.class);
        Call<SensorModel> modelCall = sensorApiCaller.get_sensor_detallado(sensor);
        modelCall.enqueue(new Callback<SensorModel>() {
            @Override
            public void onResponse(Call<SensorModel> call, Response<SensorModel> response) {
                if (response.isSuccessful()) {
                    SensorModel model = response.body();
                    nameSensor = model.getNombre();
                    title.setText(nameSensor);
                    //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                } else {
                    title.setText(sensor);
                    //Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SensorModel> call, Throwable t) {
                title.setText(sensor);
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                //Log.e("INFO", t.getMessage());
            }
        });
    }

    private Chart setChart(Chart chart, int animation) {

        chart.getDescription().setEnabled(false);
        chart.animateX(animation);
        chart.setNoDataText("No fue posible cargar los datos");
        return chart;
    }

    private void Xaxis(XAxis axis) {

        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setGranularity(1);
        axis.setGranularityEnabled(true);
    }

    private void RigthYaxis(YAxis axis) {
        axis.setEnabled(false);
    }
}