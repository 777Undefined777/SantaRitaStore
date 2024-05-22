package com.example.intento;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetail extends AppCompatActivity {

    private EditText quantityEditText;
    private TextView productPriceTextView;
    private TextView productNameTextView;

    private AdminSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        // Inicializar la base de datos
        dbHelper = new AdminSQLiteOpenHelper(this);

        // Obtener los datos del producto seleccionado
        String productName = getIntent().getStringExtra("productName");
        String productDescription = getIntent().getStringExtra("productDescription");
        String productPrice = getIntent().getStringExtra("productPrice");

        // Obtener referencias a los elementos de la interfaz de usuario
        productNameTextView = findViewById(R.id.product_name_details);
        TextView productDescriptionTextView = findViewById(R.id.product_description_details);
        productPriceTextView = findViewById(R.id.product_price_details);
        quantityEditText = findViewById(R.id.quantity_edit_text);

        // Establecer los datos del producto en los elementos de la interfaz de usuario
        productNameTextView.setText(productName);
        productDescriptionTextView.setText(productDescription);
        productPriceTextView.setText("COP $" + productPrice);


        // Configurar el OnClickListener para el botón "Añadir al carrito"
        findViewById(R.id.product_add_to_card_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    // Lógica para manejar el evento de clic en el botón "Agregar al carrito"
    private void addToCart() {
        // Obtener la cantidad ingresada por el usuario
        String quantityString = quantityEditText.getText().toString();
        int quantity = Integer.parseInt(quantityString);

        // Obtener el precio del producto
        String priceString = productPriceTextView.getText().toString();
        double price = Double.parseDouble(priceString.replaceAll("[^\\d.]", ""));

        // Obtener user_id del usuario que inició sesión
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("usuario_id", -1); // Devuelve -1 si no hay user_id

        // Verifica si se obtuvo un user_id válido
        if (userId == -1) {
            Toast.makeText(this, "Error al obtener el user_id", Toast.LENGTH_SHORT).show();
            return;
        }

        // Abrir la base de datos para escritura
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crear un objeto ContentValues para guardar los datos en la base de datos
        ContentValues values = new ContentValues();
        values.put("pid", 0); // Ajusta según tu lógica de producto
        values.put("pname", productNameTextView.getText().toString());
        values.put("price", String.valueOf(price));
        values.put("quantity", String.valueOf(quantity));
        values.put("user_id", userId); // Incluye el user_id del usuario que inició sesión

        // Insertar el nuevo registro en la tabla de tarjetas
        long newRowId = db.insert("cards", null, values);

        // Cerrar la base de datos
        db.close();

        // Mostrar mensaje con Toast dependiendo del resultado
        if (newRowId != -1) {
            Toast.makeText(this, "Producto agregado al carrito exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al ingresar producto al carrito", Toast.LENGTH_SHORT).show();
        }
    }
}
