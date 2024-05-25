package com.example.intento;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class CheckSplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 2000; // Duración del splash (2 segundos)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Aquí puedes regresar a la actividad anterior o iniciar una nueva
                CheckSplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
