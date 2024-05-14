package com.example.intento;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class AddNewProductActivity extends AppCompatActivity {
    private String categoryName, description, price, pName, saveCurrentDate, saveCurrentTime;
    private Button addNewProductButton;
    private ImageView inputProductImage, ret;
    private EditText inputProductName, inputProductDescription, inputProductPrice;
    private static final int GALLERY_PICK = 1;
    private Uri imageUri;
    private String productRandomKey;
    private ProgressDialog loadingBar;

    private AdminSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        dbHelper = new AdminSQLiteOpenHelper(this);

        categoryName = getIntent().getStringExtra("category");
        addNewProductButton = findViewById(R.id.add_new_product);
        inputProductImage = findViewById(R.id.select_product_image);
        inputProductName = findViewById(R.id.product_name);
        inputProductDescription = findViewById(R.id.product_description);
        inputProductPrice = findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);
        ret = findViewById(R.id.ret);

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateProductData();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            inputProductImage.setImageURI(imageUri);
        }
    }

    private void validateProductData() {
        description = inputProductDescription.getText().toString();
        price = inputProductPrice.getText().toString();
        pName = inputProductName.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Seleccione una imagen para el producto", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            inputProductDescription.setError("Ingrese la descripción del producto");
            inputProductDescription.requestFocus();
        } else if (TextUtils.isEmpty(price)) {
            inputProductPrice.setError("Ingrese el precio del producto");
            inputProductPrice.requestFocus();
        } else if (TextUtils.isEmpty(pName)) {
            inputProductName.setError("Ingrese el nombre del producto");
            inputProductName.requestFocus();
        } else {
            storeProductInformation();
        }
    }

    private void storeProductInformation() {
        loadingBar.setTitle("Añadiendo producto");
        loadingBar.setMessage("Espere mientras se agrega el producto...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        Random random = new Random();
        productRandomKey = String.valueOf(random.nextInt(100000));

        // Obtener la base de datos writable
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Agregar el producto a la base de datos
        dbHelper.addProduct(db, pName, description, price, imageUri.toString(), categoryName, saveCurrentDate, saveCurrentTime);

        loadingBar.dismiss();

        Toast.makeText(this, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show();
        finish(); // Regresar a la actividad anterior después de agregar el producto
    }
}
