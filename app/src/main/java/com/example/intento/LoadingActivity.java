package com.example.intento;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {
    private static final int LOADING_DISPLAY_LENGTH = 2000; // Duración del splash de carga (2 segundos)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Aplica la animación de rotación en 3D a la imagen de la flor
        ImageView flowerImage = findViewById(R.id.flowerImage);
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(flowerImage, "rotationY", 0f, 360f);
        rotateAnimation.setDuration(3000); // Duración de la animación
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(ObjectAnimator.INFINITE); // Repetir la animación infinitamente
        rotateAnimation.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Inicia la siguiente actividad después de la carga
                Intent nextActivityIntent = new Intent(LoadingActivity.this, HomeActivity.class);
                LoadingActivity.this.startActivity(nextActivityIntent);
                LoadingActivity.this.finish();
            }
        }, LOADING_DISPLAY_LENGTH);
    }
}
