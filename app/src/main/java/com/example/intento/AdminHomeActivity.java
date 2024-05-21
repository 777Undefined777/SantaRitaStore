package com.example.intento;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity {

    private Button btn_logout, btn_viewCommand , btn_add_category, btn_del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btn_logout = findViewById(R.id.btn_logout_admin);
        btn_viewCommand = findViewById(R.id.btn_command);
        btn_add_category = findViewById(R.id.btn_add_product);
        btn_del = findViewById(R.id.btn_delete_products_admin);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        btn_viewCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar aquí la lógica para ver los comandos
                Intent intent = new Intent(AdminHomeActivity.this, CommandViewActivity.class);
                startActivity(intent);
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar aquí la lógica para ver los comandos
                Intent intent = new Intent(AdminHomeActivity.this, DeleteProducts.class);
                startActivity(intent);
            }
        });

        btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
            }
        });

    }
}
