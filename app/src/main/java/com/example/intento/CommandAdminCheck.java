package com.example.intento;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

        // Cargar datos desde SharedPreferences
        loadDataFromSharedPreferences();
    }

    private void hacerPedido() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Obtener la fecha y hora actual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        // Insertar el pedido en la tabla commands
        ContentValues values = new ContentValues();
        values.put("name", editTextNombre.getText().toString());
        values.put("lastname", editTextApellido.getText().toString());
        values.put("address", editTextDireccion.getText().toString());
        values.put("phone", editTextTelefono.getText().toString());
        values.put("city", editTextCiudad.getText().toString());
        values.put("cedula", editTextCedula.getText().toString());
        values.put("state", "no pagado");
        values.put("date", currentDateandTime);
        values.put("totalPrice", getTotalPriceFromSharedPreferences());

        long newRowId = db.insert("commands", null, values);

        // Si se inserta correctamente, guardar los detalles del pedido en command_detail
        if (newRowId != -1) {
            SharedPreferences sharedPreferences = getSharedPreferences("CardData", MODE_PRIVATE);
            String productListJson = sharedPreferences.getString("productList", "");

            if (!productListJson.isEmpty()) {
                try {
                    JSONArray jsonArray = new JSONArray(productListJson);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String productName = jsonObject.getString("product_name");
                        String price = jsonObject.getString("price");
                        int quantity = jsonObject.getInt("quantity");
                        double totalPrice = jsonObject.getDouble("total_price");

                        ContentValues detailValues = new ContentValues();
                        detailValues.put("product_name", productName);
                        detailValues.put("price", price);
                        detailValues.put("quantity", quantity);
                        detailValues.put("total_price", totalPrice);
                        detailValues.put("command_id", newRowId); // Referencia al pedido

                        db.insert("command_detail", null, detailValues);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(this, "Pedido realizado correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CommandAdminCheck.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error al realizar el pedido", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void loadDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CardData", MODE_PRIVATE);
        float totalPrice = sharedPreferences.getFloat("totalPrice", 0);
        String productListJson = sharedPreferences.getString("productList", "");

        // Aquí puedes usar estos datos como sea necesario en tu actividad
        // Por ejemplo, mostrarlos en un TextView o procesarlos de alguna manera
    }

    private float getTotalPriceFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CardData", MODE_PRIVATE);
        return sharedPreferences.getFloat("totalPrice", 0);
    }
}
