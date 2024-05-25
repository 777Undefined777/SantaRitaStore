package com.example.intento;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ProductDetail extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productDescriptionTextView;
    private TextView productPriceTextView;
    private EditText quantityEditText;
    private Button addToCartButton;
    private AdminSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        dbHelper = new AdminSQLiteOpenHelper(this);

        // Obtener referencias a los elementos de la interfaz de usuario
        productImageView = findViewById(R.id.product_image_details);
        productNameTextView = findViewById(R.id.product_name_details);
        productDescriptionTextView = findViewById(R.id.product_description_details);
        productPriceTextView = findViewById(R.id.product_price_details);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        addToCartButton = findViewById(R.id.product_add_to_card_btn);

        // Obtener los datos del producto seleccionado
        String productName = Objects.requireNonNull(getIntent().getStringExtra("productName"));
        String productDescription = Objects.requireNonNull(getIntent().getStringExtra("productDescription"));
        String productPrice = Objects.requireNonNull(getIntent().getStringExtra("productPrice"));

        // Establecer los datos del producto en los elementos de la interfaz de usuario
        productNameTextView.setText(productName);
        productDescriptionTextView.setText(productDescription);
        productPriceTextView.setText("COP $" + productPrice);

        // Obtener la imagen como cadena Base64 de los extras del intent
        String imageBase64 = getIntent().getStringExtra("productImage");

        // Convertir la cadena Base64 de la imagen a un Bitmap y establecerla en el ImageView
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            byte[] imageByteArray = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            productImageView.setImageBitmap(bitmap);
        } else {
            // Si no se recibe la imagen correctamente, mostrar una imagen de error o un mensaje
        }

        // Agregar OnClickListener al botón "Añadir al carrito"
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

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
        values.put("pid", 0); // Ajustar según tu lógica de producto
        values.put("pname", productNameTextView.getText().toString());
        values.put("price", String.valueOf(price));
        values.put("quantity", String.valueOf(quantity));
        values.put("user_id", userId); // Incluir el user_id del usuario que inició sesión

        // Insertar el nuevo registro en la tabla de tarjetas
        long newRowId = db.insert("cards", null, values);

        // Cerrar la base de datos
        db.close();

        // Mostrar mensaje con Toast dependiendo del resultado
        if (newRowId != -1) {
            Intent intent = new Intent(ProductDetail.this, CheckSplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al ingresar producto al carrito", Toast.LENGTH_SHORT).show();
        }
    }
}
