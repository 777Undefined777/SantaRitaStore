package com.example.intento;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;



import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandAdminCheck extends AppCompatActivity {

    EditText editTextNombre, editTextApellido, editTextDireccion, editTextTelefono, editTextCiudad, editTextCedula;
    Button btnHacerPedido;
    AdminSQLiteOpenHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_admin_check);

        dbHelper = new AdminSQLiteOpenHelper(this);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextDireccion = findViewById(R.id.editTextDireccion);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextCiudad = findViewById(R.id.editTextCiudad);
        editTextCedula = findViewById(R.id.editTextCedula);

        btnHacerPedido = findViewById(R.id.btnHacerPedido);
        btnHacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerPedido();
            }
        });
    }

    private void hacerPedido() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Obtener la fecha y hora actual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        ContentValues values = new ContentValues();
        values.put("name", editTextNombre.getText().toString());
        values.put("lastname", editTextApellido.getText().toString());
        values.put("address", editTextDireccion.getText().toString());
        values.put("phone", editTextTelefono.getText().toString());
        values.put("city", editTextCiudad.getText().toString());
        values.put("cedula", editTextCedula.getText().toString());
        values.put("state", "no pagado");
        values.put("date", currentDateandTime);

        long newRowId = db.insert("commands", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Pedido realizado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al realizar el pedido", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
