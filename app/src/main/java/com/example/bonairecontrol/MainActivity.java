package com.example.bonairecontrol;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.InterfazApi.BitacoraWebApiCaller;
import com.example.bonairecontrol.InterfazApi.UsuarioApiCaller;
import com.example.bonairecontrol.ModelosApi.BitacoraWebModel;
import com.example.bonairecontrol.ModelosApi.UsuarioModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.bonairecontrol.Clases.Clases.isConnected;

public class MainActivity extends AppCompatActivity {

    private TextView txt_link_recuperar_password, txt_user_login, txt_password_login;
    private CheckBox cb_recordarme_login, cb_modo_bluetooth;
    private ProgressBar progressBar3;
    private Button btnIngresar;
    boolean remember, blValidationTimer = true;
    Context context = this;
    public static final String TAG = "NOTICIAS";

    //Retrofit
    String UrlBase = "", Ip, token;
    Retrofit retrofit = null;
    boolean validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        editor.putBoolean("onlyBluetooth", !isConnected(this));
        editor.apply();
        if (!isConnected(this)){
            Intent HomePage = new Intent(this, HomeActivity.class);
            startActivity(HomePage);
        }

        UrlBase = getString(R.string.url_base_api);

        fncDeleteData();

        retrofit = new Retrofit.Builder().baseUrl(UrlBase)
                .addConverterFactory(GsonConverterFactory.create()).build();

        txt_link_recuperar_password = (TextView) findViewById(R.id.txt_link_recuperar_password);
        txt_user_login = (TextView) findViewById(R.id.txt_user_login);
        txt_password_login = (TextView) findViewById(R.id.txt_password_login);
        cb_recordarme_login = (CheckBox) findViewById(R.id.cb_recordarme_login);
        cb_modo_bluetooth = (CheckBox) findViewById(R.id.cb_modo_bluetooth);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);

        //Realizamos una verificacion de datos inicial

        cb_modo_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_modo_bluetooth.isChecked()){
                    txt_user_login.setText("");
                    txt_password_login.setText("");
                    cb_recordarme_login.setChecked(false);
                }
                txt_user_login.setEnabled(!cb_modo_bluetooth.isChecked());
                txt_password_login.setEnabled(!cb_modo_bluetooth.isChecked());
                cb_recordarme_login.setEnabled(!cb_modo_bluetooth.isChecked());
            }
        });

        txt_link_recuperar_password.setMovementMethod(LinkMovementMethod.getInstance());
        txt_password_login.setText("");
        txt_user_login.setText("");
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                Toast.makeText(MainActivity.this, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Get new FCM registration token
                            token = task.getResult();

                            Log.d(TAG, token);
                            fncVerificarDatos();
                            //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            token = "error token";
        }

    }

    private void fncVerificarDatos() {

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        if (isConnected(this)) {
            String User = sharPref.getString("User", "0");
            String Pass = sharPref.getString("Pass", "0");
            remember = sharPref.getBoolean("remember", false);

            if (remember) {
                progressBar3.setVisibility(View.VISIBLE);
                txt_user_login.setEnabled(false);
                txt_password_login.setEnabled(false);
                btnIngresar.setClickable(false);
                fncValidarIngreso(User, Pass);
            }
        } else {
            Toast.makeText(context, "Conectando via bluetooth", Toast.LENGTH_LONG).show();
            editor.putBoolean("blTypeConnection", false);
            editor.apply();
        }
    }

    public void fncBtnIngresar(View view) {

        if (cb_modo_bluetooth.isChecked()){
            SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharPref.edit();

            editor.putBoolean("onlyBluetooth", true);
            editor.apply();

            Intent HomePage = new Intent(this, HomeActivity.class);
            startActivity(HomePage);
            return;
        }

        if (isConnected(this)){

            Ip = getPublicIPAddress(MainActivity.this);

            progressBar3.setVisibility(View.VISIBLE);
            txt_user_login.setEnabled(false);
            txt_password_login.setEnabled(false);
            btnIngresar.setClickable(false);

            String User = txt_user_login.getText().toString();
            String Pass = txt_password_login.getText().toString();

            if (User.equals("")) {
                txt_user_login.setError("Debe ingresar el nombre");
                if (Pass.equals("")) {
                    txt_password_login.setError("Debe ingresar la contraseña");
                }
            } else {
                if (Pass.equals("")) {
                    txt_password_login.setError("Debe ingresar la contraseña");
                } else {
                    fncValidarIngreso(User, Pass);
                }
            }
        }else {
            Toast.makeText(context, "Error al conectarse a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void fncValidarIngreso(String user, String pass) {

        validation = false;

        if (!remember) {
            if (blValidationTimer) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        //----------------------------
                        progressBar3.setVisibility(View.INVISIBLE);
                        txt_user_login.setEnabled(true);
                        txt_password_login.setEnabled(true);
                        txt_password_login.setText("");
                        txt_password_login.setError("Error al ingresar");
                        btnIngresar.setClickable(true);
                        return;
                        //----------------------------

                    }
                }, 15000);
            }
        }
        String User = user;
        String Pass = pass;
        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        UsuarioApiCaller usuarioCaller = retrofit.create(UsuarioApiCaller.class);

        Call<UsuarioModel> usuarioCall = usuarioCaller.get_email_user(User);

        usuarioCall.enqueue(new Callback<UsuarioModel>() {
            @Override
            public void onResponse(Call<UsuarioModel> call, Response<UsuarioModel> response) {
                UsuarioModel usuarioModel = new UsuarioModel();
                if (response.isSuccessful()) {
                    usuarioModel = response.body();
                    if (user.equals(usuarioModel.getEmail())) {
                        if (pass.equals(usuarioModel.getPassword())) {
                            validation = true;
                        }
                    } else {
                        if (user.equals(usuarioModel.getIdUsuario())) {
                            if (pass.equals(usuarioModel.getPassword())) {
                                validation = true;
                            }
                        }
                    }
                }
                if (validation) {
                    if (cb_recordarme_login.isChecked()) {
                        editor.putString("User", User);
                        editor.putString("Pass", Pass);
                        editor.putBoolean("remember", true);
                        editor.apply();
                    }
                    if (!token.equals(usuarioModel.getToken())) {
                        usuarioModel.setToken(token);
                        UsuarioApiCaller usuarioCaller = retrofit.create(UsuarioApiCaller.class);
                        Call<UsuarioModel> usuarioCall = usuarioCaller.put_token(usuarioModel.getIdUsuario(), usuarioModel);
                        usuarioCall.enqueue(new Callback<UsuarioModel>() {
                            @Override
                            public void onResponse(Call<UsuarioModel> call, Response<UsuarioModel> response) {
                            }

                            @Override
                            public void onFailure(Call<UsuarioModel> call, Throwable t) {
                            }
                        });
                    }
                } else {
                    txt_password_login.setText("");
                    Toast.makeText(context, "Usuario y/o contraseña incorrecta", Toast.LENGTH_LONG).show();
                    progressBar3.setVisibility(View.INVISIBLE);
                    txt_user_login.setEnabled(true);
                    txt_password_login.setEnabled(true);
                    btnIngresar.setClickable(true);
                }
                fncRegistroIngreso(User, Pass);
            }

            @Override
            public void onFailure(Call<UsuarioModel> call, Throwable t) {
                Toast.makeText(context, "Error " + t.getMessage(), Toast.LENGTH_LONG).show();
                fncRegistroIngreso(User, Pass);
            }
        });
    }//end

    private void fncRegistroIngreso(String user, String pass) {

        String acceso;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String DateTime = df.format(c.getTime());

        if (validation) {
            acceso = "Acceso concedido";
        } else {
            acceso = "Acceso denegado";
        }

        BitacoraWebModel data = new BitacoraWebModel();

        data.setId(1);
        data.setFechaHora(DateTime);
        data.setIdUsuario(user);
        data.setPassword(pass);
        data.setIp(Ip);
        data.setEvento(acceso);

        BitacoraWebApiCaller bitacoraCaller = retrofit.create(BitacoraWebApiCaller.class);

        Call<BitacoraWebModel> bitacoraCall = bitacoraCaller.postBitacoraWeb(data);
        bitacoraCall.enqueue(new Callback<BitacoraWebModel>() {
            @Override
            public void onResponse(Call<BitacoraWebModel> call, Response<BitacoraWebModel> response) {
                if (response.isSuccessful()) {

                    if (validation) {
                        fncUpdateToken();
                        fncIntentHome(user, pass);
                    }
                }
            }

            @Override
            public void onFailure(Call<BitacoraWebModel> call, Throwable t) {

                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar3.setVisibility(View.INVISIBLE);
                txt_user_login.setEnabled(true);
                txt_password_login.setEnabled(true);
                btnIngresar.setClickable(true);
            }
        });
    }

    private void fncUpdateToken() {

    }

    public void fncSaveData(String user, String pass) {

        if (!remember) {
            if (blValidationTimer) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        //----------------------------
                        progressBar3.setVisibility(View.INVISIBLE);
                        txt_user_login.setEnabled(true);
                        txt_password_login.setEnabled(true);
                        txt_password_login.setText("");
                        txt_password_login.setError("Error al ingresar");
                        btnIngresar.setClickable(true);
                        return;
                        //----------------------------

                    }
                }, 15000);
            }
        }

        String User = user;
        String Pass = pass;

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        UsuarioApiCaller usuarioCaller = retrofit.create(UsuarioApiCaller.class);

        Call<UsuarioModel> usuarioCall = usuarioCaller.get_email_user(User);

        usuarioCall.enqueue(new Callback<UsuarioModel>() {
            @Override
            public void onResponse(Call<UsuarioModel> call, Response<UsuarioModel> response) {


                if (response.isSuccessful()) {
                    UsuarioModel usuarioModel = response.body();

                    editor.putString("userIdUsuario", usuarioModel.getIdUsuario());
                    editor.putString("userEmail", usuarioModel.getEmail());
                    editor.putString("userTelefono", usuarioModel.getTelefono());
                    editor.putString("userPassword", usuarioModel.getPassword());
                    editor.putString("userNombres", usuarioModel.getNombres());
                    editor.putString("userApellidos", usuarioModel.getApellidos());
                    editor.apply();
                }
                progressBar3.setVisibility(View.INVISIBLE);
                txt_user_login.setEnabled(true);
                txt_password_login.setEnabled(true);
                btnIngresar.setClickable(true);
            }

            @Override
            public void onFailure(Call<UsuarioModel> call, Throwable t) {
                progressBar3.setVisibility(View.INVISIBLE);
                txt_user_login.setEnabled(true);
                txt_password_login.setEnabled(true);
                btnIngresar.setClickable(true);
            }
        });
    }

    public void fncDeleteData() {
        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        editor.putString("userIdUsuario", "-");
        editor.putString("userEmail", "-");
        editor.putString("userTelefono", "-");
        editor.putString("userPassword", "-");
        editor.putString("userNombres", "-");
        editor.putString("userApellidos", "-");
        editor.apply();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void fncIntentHome(String user, String pass) {
        blValidationTimer = false;
        fncSaveData(user, pass);
        Intent HomePage = new Intent(this, HomeActivity.class);
        startActivity(HomePage);
    }

    public static String getPublicIPAddress(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();

        RunnableFuture<String> futureRun = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
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
            }
        });

        new Thread(futureRun).start();

        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}