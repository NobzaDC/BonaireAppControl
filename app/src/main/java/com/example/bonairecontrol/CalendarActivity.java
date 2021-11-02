package com.example.bonairecontrol;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.InterfazApi.MacCalendarioApiCaller;
import com.example.bonairecontrol.ModelosApi.MacCalendarioModel;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton rbPurificacion, rbDesinfeccion, rbOneTime, rbMondayToFriday, rbAllDays,
            lunes, martes, miercoles, jueves, viernes, sabado, domingo;
    private EditText edBeginTime, edDurationHour;
    private SwitchMaterial swCustom;
    private Button btnSaveCalendar;
    Context context = this;
    String mac, UrlBase, fechaInicio;
    int horaInicio, minutoInicio, horasDuracion, minutosDuracion;
    Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        init();

    }

    private void init() {
        //Inicializacion de strings
        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        mac = sharPref.getString("MacString", "");
        UrlBase = getString(R.string.url_base_api);

        //Iniciamos retrofit
        retrofit = new Retrofit.Builder().baseUrl(UrlBase)
                .addConverterFactory(GsonConverterFactory.create()).build();

        //Radio Button tipo funcion a programar
        rbPurificacion = (RadioButton) findViewById(R.id.rb_purificacion_calendar);
        rbDesinfeccion = (RadioButton) findViewById(R.id.rb_desinfeccion_calendar);

        //Radio button lapsos de repeticion
        rbOneTime = (RadioButton) findViewById(R.id.rb_one_time_calendar);
        rbMondayToFriday = (RadioButton) findViewById(R.id.rb_monday_to_friday_calendar);
        rbAllDays = (RadioButton) findViewById(R.id.rb_all_days_calendar);

        //Radio Button Dias personalizados tiempo repeticion
        lunes = (RadioButton) findViewById(R.id.lunes_calendar);
        martes = (RadioButton) findViewById(R.id.martes_calendar);
        miercoles = (RadioButton) findViewById(R.id.miercoles_calendar);
        jueves = (RadioButton) findViewById(R.id.jueves_calendar);
        viernes = (RadioButton) findViewById(R.id.viernes_calendar);
        sabado = (RadioButton) findViewById(R.id.sabado_calendar);
        domingo = (RadioButton) findViewById(R.id.domingo_calendar);

        //Edit Text tiempos de ejecucion de la funcion programada
        edBeginTime = (EditText) findViewById(R.id.et_date_time_calendar);
        edBeginTime.setInputType(InputType.TYPE_NULL);
        edDurationHour = (EditText) findViewById(R.id.et_duration_hours_calendar);
        edDurationHour.setInputType(InputType.TYPE_NULL);

        //Switch personalizar
        swCustom = (SwitchMaterial) findViewById(R.id.sw_custom_calendar);

        //Boton para guardar el calendario
        btnSaveCalendar = (Button) findViewById(R.id.btn_save_calendar);

        //OnClickListeners
        edBeginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog();
            }
        });
        edDurationHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
        swCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swCustom.isChecked()) {
                    enableCustom(lunes);
                } else {
                    disableCustom(rbOneTime);
                }
            }
        });

        //this.OnClickListenes
        lunes.setOnClickListener(this);
        martes.setOnClickListener(this);
        miercoles.setOnClickListener(this);
        jueves.setOnClickListener(this);
        viernes.setOnClickListener(this);
        sabado.setOnClickListener(this);
        domingo.setOnClickListener(this);

        //No select more than 1 item
        rbOneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbOneTime.isChecked()) {
                    rbAllDays.setChecked(false);
                    rbMondayToFriday.setChecked(false);
                }
            }
        });
        rbMondayToFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbMondayToFriday.isChecked()) {
                    rbAllDays.setChecked(false);
                    rbOneTime.setChecked(false);
                }
            }
        });
        rbAllDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbAllDays.isChecked()) {
                    rbOneTime.setChecked(false);
                    rbMondayToFriday.setChecked(false);
                }
            }
        });
        rbPurificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbPurificacion.isChecked()) {
                    rbDesinfeccion.setChecked(false);
                }
            }
        });
        rbDesinfeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbDesinfeccion.isChecked()) {
                    rbPurificacion.setChecked(false);
                }
            }
        });
        //Button OnClickListener
        btnSaveCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostMacCalendar();
            }
        });
    }

    private void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                horasDuracion = hourOfDay;
                minutosDuracion = minute;
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                edDurationHour.setText(format.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(context, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void showDateTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fechaInicio = year + "-" + (month + 1) + "-" + dayOfMonth;

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        horaInicio = hourOfDay;
                        minutoInicio = minute;

                        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
                        edBeginTime.setText(format.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(context, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        };

        new DatePickerDialog(context, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onClick(View v) {

        if (!lunes.isChecked()) {
            return;
        }
        if (!martes.isChecked()) {
            return;
        }
        if (!miercoles.isChecked()) {
            return;
        }
        if (!jueves.isChecked()) {
            return;
        }
        if (!viernes.isChecked()) {
            return;
        }
        if (!sabado.isChecked()) {
            return;
        }
        if (!domingo.isChecked()) {
            return;
        }
        swCustom.setChecked(false);

        rbOneTime.setChecked(false);
        rbOneTime.setEnabled(true);
        rbMondayToFriday.setChecked(false);
        rbMondayToFriday.setEnabled(true);
        rbAllDays.setChecked(true);
        rbAllDays.setEnabled(true);

        lunes.setEnabled(false);
        lunes.setChecked(false);
        martes.setEnabled(false);
        martes.setChecked(false);
        miercoles.setEnabled(false);
        miercoles.setChecked(false);
        jueves.setEnabled(false);
        jueves.setChecked(false);
        viernes.setEnabled(false);
        viernes.setChecked(false);
        sabado.setEnabled(false);
        sabado.setChecked(false);
        domingo.setEnabled(false);
        domingo.setChecked(false);
    }

    private void disableCustom(RadioButton radioButton) {
        rbOneTime.setChecked(false);
        rbOneTime.setEnabled(true);
        rbMondayToFriday.setChecked(false);
        rbMondayToFriday.setEnabled(true);
        rbAllDays.setChecked(false);
        rbAllDays.setEnabled(true);
        radioButton.setChecked(true);

        lunes.setEnabled(false);
        lunes.setChecked(false);
        martes.setEnabled(false);
        martes.setChecked(false);
        miercoles.setEnabled(false);
        miercoles.setChecked(false);
        jueves.setEnabled(false);
        jueves.setChecked(false);
        viernes.setEnabled(false);
        viernes.setChecked(false);
        sabado.setEnabled(false);
        sabado.setChecked(false);
        domingo.setEnabled(false);
        domingo.setChecked(false);
    }

    private void enableCustom(RadioButton day) {
        rbOneTime.setChecked(false);
        rbOneTime.setEnabled(false);
        rbMondayToFriday.setChecked(false);
        rbMondayToFriday.setEnabled(false);
        rbAllDays.setChecked(false);
        rbAllDays.setEnabled(false);

        lunes.setEnabled(true);
        lunes.setChecked(false);
        martes.setEnabled(true);
        martes.setChecked(false);
        miercoles.setEnabled(true);
        miercoles.setChecked(false);
        jueves.setEnabled(true);
        jueves.setChecked(false);
        viernes.setEnabled(true);
        viernes.setChecked(false);
        sabado.setEnabled(true);
        sabado.setChecked(false);
        domingo.setEnabled(true);
        domingo.setChecked(false);

        day.setChecked(true);
    }

    private void sendPostMacCalendar() {
        btnSaveCalendar.setEnabled(false);
        boolean blFull = checkData();
        if (blFull) {
            String funcion;
            if (rbPurificacion.isChecked()) {
                funcion = "P";
            }else {funcion = "D";}

            MacCalendarioModel model = new MacCalendarioModel();

            model.setMac(mac);
            model.setFuncion(funcion);
            model.setFechaInicio(fechaInicio);
            model.setHoraInicio(horaInicio);
            model.setMinutoInicio(minutoInicio);
            model.setHorasDuracion(horasDuracion);
            model.setMinutosDuracion(minutosDuracion);
            model.setRepetirSU(rbOneTime.isChecked());
            model.setRepetirLV(rbMondayToFriday.isChecked());
            model.setRepetirTD(rbAllDays.isChecked());
            model.setRepetirPD(getStringCustomDates());
            model.setHabilitado(true);
            model.setObservaciones(null);


            MacCalendarioApiCaller apiCaller = retrofit.create(MacCalendarioApiCaller.class);
            Call<MacCalendarioModel> call = apiCaller.postMacCalendario(model);
            call.enqueue(new Callback<MacCalendarioModel>() {
                @Override
                public void onResponse(Call<MacCalendarioModel> call, Response<MacCalendarioModel> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(CalendarActivity.this, "Agenda exitosa", Toast.LENGTH_SHORT).show();
                    }else {
                        Call<MacCalendarioModel> callerCall = apiCaller.putMacCalendario(mac, model);
                        callerCall.enqueue(new Callback<MacCalendarioModel>() {
                            @Override
                            public void onResponse(Call<MacCalendarioModel> call, Response<MacCalendarioModel> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(CalendarActivity.this, "Agenda actualizada con exitosa", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(CalendarActivity.this, "Error de conexion, por favor intente de nuevo mas tarde", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MacCalendarioModel> call, Throwable t) {
                                Toast.makeText(CalendarActivity.this, "Error de conexion, por favor intente de nuevo mas tarde", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    btnSaveCalendar.setEnabled(true);
                }

                @Override
                public void onFailure(Call<MacCalendarioModel> call, Throwable t) {
                    Toast.makeText(CalendarActivity.this, "Error de conexion, por favor intente de nuevo mas tarde", Toast.LENGTH_LONG).show();
                    btnSaveCalendar.setEnabled(true);
                }
            });
        } else {
            btnSaveCalendar.setEnabled(true);
        }
    }

    private String getStringCustomDates() {
        if (lunes.isChecked() || martes.isChecked() || miercoles.isChecked() || jueves.isChecked()
                || viernes.isChecked() || sabado.isChecked() || domingo.isChecked()){
            String dates = "";

            if (domingo.isChecked()){
                dates+= "DO-";
            }
            if (lunes.isChecked()){
                dates+= "LU-";
            }
            if (martes.isChecked()){
                dates+= "MA-";
            }
            if (miercoles.isChecked()){
                dates+= "MI-";
            }
            if (jueves.isChecked()){
                dates+= "JU-";
            }
            if (viernes.isChecked()){
                dates+= "VI-";
            }
            if (sabado.isChecked()){
                dates+= "SA-";
            }

            return dates.substring(0, dates.length() - 1);
        }else {
            return null;
        }
    }

    private boolean checkData() {
        if (!(rbPurificacion.isChecked() || rbDesinfeccion.isChecked())) {
            Toast.makeText(context, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(rbOneTime.isChecked() || rbMondayToFriday.isChecked() || rbAllDays.isChecked() ||
                lunes.isChecked() || martes.isChecked() || miercoles.isChecked() || jueves.isChecked()
                || viernes.isChecked() || sabado.isChecked() || domingo.isChecked())) {
            Toast.makeText(context, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edBeginTime.getText().equals("")) {
            Toast.makeText(context, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edDurationHour.getText().equals("")) {
            Toast.makeText(context, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}