package com.example.intento;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.intento.model.Product;

import java.util.ArrayList;
import java.util.List;

public class DeleteProducts extends AppCompatActivity {

    private AdminSQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;
    private LinearLayout linearLayoutProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_products);

        dbHelper = new AdminSQLiteOpenHelper(this);
        database = dbHelper.getWritableDatabase();

        linearLayoutProducts = findViewById(R.id.linear_layout_products);

        Cursor cursor = dbHelper.getAllProducts(database);
        List<Product> products = getAllProductsFromCursor(cursor);

        for (Product product : products) {
            CardView cardView = createCardViewForProduct(product);
            linearLayoutProducts.addView(cardView);
        }
    }

    private List<Product> getAllProductsFromCursor(Cursor cursor) {
        List<Product> products = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("pname"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex("price"));
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));

                Product product = new Product(id, name, description, price, image, category, date, time);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    private CardView createCardViewForProduct(final Product product) {
        final CardView cardView = new CardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        cardView.setLayoutParams(layoutParams);
        cardView.setPadding(20, 20, 20, 20);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardView.setRadius(15);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView productNameTextView = new TextView(this);
        productNameTextView.setText(product.getPname());
        productNameTextView.setTextSize(20);
        linearLayout.addView(productNameTextView);

        TextView productPriceTextView = new TextView(this);
        productPriceTextView.setText(product.getPrice());
        productPriceTextView.setTextSize(16);
        linearLayout.addView(productPriceTextView);

        ImageView productImageView = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
        productImageView.setImageBitmap(bitmap);
        linearLayout.addView(productImageView);

        Button deleteButton = new Button(this);
        deleteButton.setText("Eliminar");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteProduct(database, product.getId());
                linearLayoutProducts.removeView(cardView);
            }
        });
        linearLayout.addView(deleteButton);

        cardView.addView(linearLayout);

        return cardView;
    }
}
