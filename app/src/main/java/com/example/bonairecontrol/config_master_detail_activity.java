package com.example.bonairecontrol;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bonairecontrol.fragments.fragment_recicle_configs;

public class config_master_detail_activity extends AppCompatActivity{

    fragment_recicle_configs recicleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_master_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recicleFragment = new fragment_recicle_configs();

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_config_m_d, recicleFragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ProfileActivy.class);
        startActivity(intent);
    }
}