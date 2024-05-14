package com.example.intento;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsActivity extends Fragment {

    private EditText phoneEditText;
    private EditText fullNameEditText;
    private EditText passwordEditText;
    private ImageView closeSettingsBtn;
    private Button updateButton;

    private AdminSQLiteOpenHelper dbHelper;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar la vista para este fragmento
        View view = inflater.inflate(R.layout.activity_settings, container, false);

        // Inicializar la base de datos
        dbHelper = new AdminSQLiteOpenHelper(getActivity());

        // Obtener referencias a los elementos de la interfaz de usuario
        phoneEditText = view.findViewById(R.id.settings_phone_number);
        fullNameEditText = view.findViewById(R.id.settings_full_name);
        passwordEditText = view.findViewById(R.id.settings_address);
        updateButton = view.findViewById(R.id.update_account_settings_btn);
        closeSettingsBtn = view.findViewById(R.id.close_settings_btn);

        // Obtener el user_id del usuario actual
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userId = preferences.getInt("usuario_id", -1);

        // Si no se puede obtener el user_id, muestra un mensaje de error
        if (userId == -1) {
            Toast.makeText(getActivity(), "Error al obtener el ID de usuario", Toast.LENGTH_SHORT).show();
            // Puedes agregar lógica adicional aquí para manejar este caso
        }

        // Cargar los datos del usuario en los campos de entrada
        loadUserData();

        // Configurar el botón para cerrar la actividad
        closeSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para cerrar el fragmento (o regresar a la actividad anterior)
                getActivity().onBackPressed();
            }
        });

        // Configurar el botón de actualización
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        return view;
    }

    // Carga los datos del usuario desde la base de datos y los muestra en los campos de entrada
    private void loadUserData() {
        // Obtener la base de datos legible
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta para obtener los datos del usuario actual
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query("users", null, selection, selectionArgs, null, null, null);

        // Si se encuentran datos del usuario, cargarlos en los campos de entrada
        if (cursor.moveToFirst()) {
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));

            // Establecer los datos en los campos de entrada
            phoneEditText.setText(phone);
            fullNameEditText.setText(fullName);
            passwordEditText.setText(password);
        } else {
            // Mostrar mensaje de error si no se encuentran datos del usuario
            Toast.makeText(getActivity(), "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show();
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        db.close();
    }

    // Actualiza los datos del usuario en la base de datos
    private void updateUserData() {
        // Obtener los valores de los campos de entrada
        String phone = phoneEditText.getText().toString().trim();
        String fullName = fullNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Verificar que los campos no estén vacíos
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(fullName) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la base de datos escribible
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crear un objeto ContentValues con los valores a actualizar
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("name", fullName);
        values.put("password", password);

        // Actualizar los datos del usuario en la base de datos
        int rowsUpdated = db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});

        // Cerrar la base de datos
        db.close();

        // Mostrar mensaje con Toast dependiendo del resultado
        if (rowsUpdated > 0) {
            Toast.makeText(getActivity(), "Datos del usuario actualizados exitosamente", Toast.LENGTH_SHORT).show();
            // Puedes agregar lógica adicional aquí si es necesario, como volver a la actividad anterior
        } else {
            Toast.makeText(getActivity(), "Error al actualizar los datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
