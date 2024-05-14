package com.example.intento;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegistrar extends AppCompatActivity {

    private EditText inputName, inputPhone, inputPassword;
    private ProgressDialog loadingBar;
    private AdminSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        // Inicializar AdminSQLiteOpenHelper
        dbHelper = new AdminSQLiteOpenHelper(this);

        // Asignar vistas de los elementos de la interfaz de usuario
        inputName = findViewById(R.id.register_username_input);
        inputPhone = findViewById(R.id.register_phone_number_input);
        inputPassword = findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        // Configurar el botón de crear cuenta
        Button btnCreateAccount = findViewById(R.id.btn_register);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        // Obtener los valores de los campos de texto
        String name = inputName.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Verificar que todos los campos estén completos
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceder con el registro del usuario
        registerUser(name, phone, password);
    }

    private void registerUser(String name, String phone, String password) {
        // Mostrar un diálogo de progreso durante el registro
        loadingBar.setTitle("Creando su usuario");
        loadingBar.setMessage("Por favor, espere un momento...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // Obtener una instancia de la base de datos
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Obtener el género del usuario desde el RadioGroup
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId != -1) {
            // Se ha seleccionado un RadioButton, obtener el texto del género
            RadioButton radioButton = findViewById(selectedId);
            String gender = radioButton.getText().toString();

            // Crear un objeto ContentValues con los valores a insertar
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("phone", phone);
            values.put("password", password);
            values.put("gender", gender);

            // Insertar los valores en la base de datos
            long newRowId = db.insert("users", null, values);

            // Cerrar el diálogo de progreso
            loadingBar.dismiss();

            // Comprobar si la inserción fue exitosa
            if (newRowId != -1) {
                // Registro exitoso
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                // Guardar el nombre de usuario y user_id en SharedPreferences
                saveUsernameAndUserIdToSharedPreferences(name, (int) newRowId);

                // Redirigir a la actividad de inicio de sesión
                Intent intent = new Intent(ActivityRegistrar.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finalizar esta actividad
            } else {
                // Si la inserción falló
                Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
            }

            // Limpiar los campos de entrada después del registro
            inputName.setText("");
            inputPhone.setText("");
            inputPassword.setText("");
        } else {
            // Si no se seleccionó ningún género
            loadingBar.dismiss();
            Toast.makeText(this, "Por favor, seleccione su género", Toast.LENGTH_SHORT).show();
        }

        // Cerrar la conexión de la base de datos
        db.close();
    }

    // Método para guardar el nombre de usuario y user_id en SharedPreferences
    private void saveUsernameAndUserIdToSharedPreferences(String username, int userId) {
        // Obtener una instancia de SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Guardar el nombre de usuario y user_id
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombre_usuario", username);
        editor.putInt("usuario_id", userId);
        editor.apply();
    }
}
