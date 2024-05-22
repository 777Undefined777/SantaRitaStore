package com.example.intento;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  // Habilita JavaScript si es necesario

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com/maps/place/La+Madera,+Oca%C3%B1a,+Norte+de+Santander/@8.2666997,-73.2770098,15z/data=!4m15!1m8!3m7!1s0x8e6770cdf5acb1a5:0xcc3356078bfdee5e!2sLa+Playa,+Norte+de+Santander!3b1!8m2!3d8.2748468!4d-73.2057595!16s%2Fm%2F02qbc48!3m5!1s0x8e67707a9c459f5b:0x6ec32d4a4d5c1d90!8m2!3d8.2667!4d-73.26671!16s%2Fg%2F1tfwr03v?hl=es&entry=ttu");
    }
}
