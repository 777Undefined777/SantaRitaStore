package com.example.intento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText inputName, inputPhone, inputPassword;
    private AdminSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar AdminSQLiteOpenHelper
        dbHelper = new AdminSQLiteOpenHelper(this);

        // Asignar vistas de los elementos de la interfaz de usuario
        inputName = findViewById(R.id.login_name_input);
        inputPhone = findViewById(R.id.login_phone_number_input);
        inputPassword = findViewById(R.id.login_password_input);

        // Configurar el botón de inicio de sesión
        Button btnLogin = findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    // Método para manejar el inicio de sesión
    private void loginUser() {
        // Obtener valores de los campos de texto
        String name = inputName.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Verificar que todos los campos estén completos
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si las credenciales son del administrador
        if (name.equals("andres") && phone.equals("3228847727") && password.equals("jorge")) {
            // Si las credenciales son para el administrador, inicia sesión y redirige a AdminHomeActivity
            Toast.makeText(this, "Inicio de sesión exitoso como administrador", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Verificar credenciales del usuario y obtener el ID
            Integer userId = checkCredentials(name, phone, password);

            if (userId != null) {
                // Si las credenciales son válidas, redirige al usuario a HomeActivity
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                // Guardar el nombre de usuario y user_id en SharedPreferences
                saveUsernameAndUserIdToSharedPreferences(name, userId);

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Si las credenciales son inválidas, muestra un mensaje de error
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para verificar las credenciales del usuario y obtener el ID
    private Integer checkCredentials(String name, String phone, String password) {
        // Obtener una base de datos legible
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Proyección para obtener el ID de usuario
        String[] projection = {"id"};
        String selection = "name = ? AND phone = ? AND password = ?";
        String[] selectionArgs = {name, phone, password};

        // Realizar la consulta a la base de datos
        Cursor cursor = db.query(
                "users",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Integer userId = null;
        if (cursor.moveToFirst()) {
            // Verifica si la columna "id" existe
            int idIndex = cursor.getColumnIndex("id");
            if (idIndex != -1) {
                // Si la columna "id" existe, obtiene su valor
                userId = cursor.getInt(idIndex);
            }
        }

        // Cierra el cursor y la base de datos
        cursor.close();
        db.close();

        return userId;
    }

    // Método para guardar el nombre de usuario y user_id en SharedPreferences
    private void saveUsernameAndUserIdToSharedPreferences(String username, Integer userId) {
        if (userId != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("nombre_usuario", username);
            editor.putInt("usuario_id", userId);
            editor.apply();
        }
    }
}
