package com.example.bonairecontrol;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bonairecontrol.ui.configurations.ConfigurationsFragment;

public class config_devices_activity extends AppCompatActivity {

    ConfigurationsFragment configurationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_devices_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        configurationsFragment = new ConfigurationsFragment();

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_config_m_d, configurationsFragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ProfileActivy.class);
        startActivity(intent);
    }
}