package com.example.bonairecontrol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bonairecontrol.Interfaz.navigationHome;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class HomeActivity extends AppCompatActivity implements navigationHome, View.OnClickListener {

    NavigationView navigationView;
    NavController navController;

    private Dialog dialog;
    private AppBarConfiguration mAppBarConfiguration;
    private SwitchMaterial switchEliminadorHumo, switchBq600, switchTechCe, switchBqv, switchCampanas;
    private Button btnContinuar;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharPref.edit();

        editor.putString("BtMacString", null);
        editor.putString("BtNombreEquipo", null);
        editor.apply();

        //menu desplegable
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_config, R.id.graficsFragment, R.id.calendarFragment,
                R.id.smokeListFragment)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.select_equip_dialog);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.background_select_equip_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.select_equip_dialog_animation;
        dialog.setCancelable(false);

        switchEliminadorHumo = dialog.findViewById(R.id.sw_eliminador_humo);
        switchBq600 = dialog.findViewById(R.id.sw_b_q600);
        switchTechCe = dialog.findViewById(R.id.sw_tech_ce);
        switchBqv = dialog.findViewById(R.id.sw_bonaire_bqv);
        switchCampanas = dialog.findViewById(R.id.sw_campanas);

        btnContinuar = dialog.findViewById(R.id.btn_continuar);

        navigationView.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean eliminadorHumo = false, bq600 = false, techCe = false, bonaire_bqv = false, campanas = false;
                eliminadorHumo = sharPref.getBoolean("initElimHumo", false);
                bq600 = sharPref.getBoolean("initBq600", false);
                techCe = sharPref.getBoolean("initTechCe", false);
                bonaire_bqv = sharPref.getBoolean("initBqv", false);
                campanas = sharPref.getBoolean("initCampanas", false);
                dialog.show();
                switchEliminadorHumo.setChecked(eliminadorHumo);
                switchBq600.setChecked(bq600);
                switchTechCe.setChecked(techCe);
                switchBqv.setChecked(bonaire_bqv);
                switchCampanas.setChecked(campanas);
                if (eliminadorHumo || bq600 || techCe || bonaire_bqv || campanas) {
                    btnContinuar.setEnabled(true);
                }
                return false;
            }
        });
        //Navigation Bar

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext()
                                , AddActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext()
                                , ProfileActivy.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchEliminadorHumo.isChecked() || switchBq600.isChecked() || switchTechCe.isChecked() || switchBqv.isChecked() || switchCampanas.isChecked()) {
                    SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharPref.edit();
                    if (switchEliminadorHumo.isChecked()) {
                        editor.putBoolean("initElimHumo", true);
                    } else {
                        editor.putBoolean("initElimHumo", false);
                    }
                    if (switchBq600.isChecked()) {
                        editor.putBoolean("initBq600", true);
                    } else {
                        editor.putBoolean("initBq600", false);
                    }
                    if (switchTechCe.isChecked()) {
                        editor.putBoolean("initTechCe", true);
                    } else {
                        editor.putBoolean("initTechCe", false);
                    }
                    if (switchBqv.isChecked()) {
                        editor.putBoolean("initBqv", true);
                    } else {
                        editor.putBoolean("initBqv", false);
                    }
                    if (switchCampanas.isChecked()){
                        editor.putBoolean("initCampanas", true);
                    }else {
                        editor.putBoolean("initCampanas", false);
                    }
                    editor.apply();
                    dialog.dismiss();
                    navController.navigate(R.id.nav_home);
                }
            }
        });

        switchEliminadorHumo.setOnClickListener(this);
        switchBq600.setOnClickListener(this);
        switchTechCe.setOnClickListener(this);
        switchBqv.setOnClickListener(this);
        switchCampanas.setOnClickListener(this);
        validarListaRecicler();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext()
                , HomeActivity.class));
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void toDevices(Activity activity) {
        navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
        navController.navigate(R.id.nav_gallery);
    }

    @Override
    public void toEliminadorHumo(Activity activity) {
        navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
        navController.navigate(R.id.smokeListFragment);
    }

    @Override
    public void toCampanas(Activity activity) {
        navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
        navController.navigate(R.id.campanasFragment);
    }

    private void validarListaRecicler() {
        SharedPreferences sharPref = getSharedPreferences("BonairePref", context.MODE_PRIVATE);
        boolean eliminadorHumo, bq600, techCe, bonaireBqv, campanas;
        eliminadorHumo = sharPref.getBoolean("initElimHumo", false);
        bq600 = sharPref.getBoolean("initBq600", false);
        techCe = sharPref.getBoolean("initTechCe", false);
        bonaireBqv = sharPref.getBoolean("initBqv", false);
        campanas = sharPref.getBoolean("initCampanas", false);
        /*SharedPreferences.Editor editor = sharPref.edit();
        editor.putBoolean("initElimHumo", false);
        editor.putBoolean("initBq600", false);
        editor.putBoolean("initTechCe", false);
        editor.putBoolean("initBqv", false);
        editor.apply();*/

        if (!eliminadorHumo && !bq600 && !techCe && !bonaireBqv && !campanas) {
            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        btnContinuar.setEnabled(switchEliminadorHumo.isChecked() || switchBq600.isChecked() || switchTechCe.isChecked() || switchBqv.isChecked() || switchCampanas.isChecked());
    }
}