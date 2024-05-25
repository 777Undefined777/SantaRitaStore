package com.example.intento;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity2 extends AppCompatActivity {
    private static final int LOADING_DISPLAY_LENGTH = 2000; // Duración del splash de carga (2 segundos)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Aquí puedes iniciar la actividad siguiente después de la carga
                Intent nextActivityIntent = new Intent(LoadingActivity2.this, AdminHomeActivity.class);
                LoadingActivity2.this.startActivity(nextActivityIntent);
                LoadingActivity2.this.finish();
            }
        }, LOADING_DISPLAY_LENGTH);
    }
}
