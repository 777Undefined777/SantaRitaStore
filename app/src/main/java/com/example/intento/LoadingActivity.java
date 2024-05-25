package com.example.intento;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {
    private static final int LOADING_DISPLAY_LENGTH = 2000; // Duración del splash de carga (2 segundos)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Aquí puedes iniciar la actividad siguiente después de la carga
                Intent nextActivityIntent = new Intent(LoadingActivity.this, HomeActivity.class);
                LoadingActivity.this.startActivity(nextActivityIntent);
                LoadingActivity.this.finish();
            }
        }, LOADING_DISPLAY_LENGTH);
    }
}
