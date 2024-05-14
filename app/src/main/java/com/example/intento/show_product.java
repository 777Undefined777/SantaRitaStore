package com.example.intento;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.intento.model.Product;
import java.util.ArrayList;
import java.util.List;

public class show_product extends Fragment implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private AdminSQLiteOpenHelper dbHelper;

    public show_product() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.category, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        productList = new ArrayList<>();
        dbHelper = new AdminSQLiteOpenHelper(getActivity());

        // Método para cargar los productos desde la base de datos SQLite
        loadProducts();

        return rootView;
    }

    // Método para cargar los productos desde la base de datos SQLite
    private void loadProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Obtener todos los productos de la base de datos
        Cursor cursor = dbHelper.getAllProducts(db);

        // Verificar si el cursor tiene algún resultado
        if (cursor != null && cursor.moveToFirst()) {
            // Iterar a través del cursor y agregar los productos a la lista
            do {
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("pname"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex("image"));

                Product product = new Product(productName, description, price, image);
                productList.add(product);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            // Mostrar un mensaje si no se encontraron productos
            Toast.makeText(getActivity(), "No se encontraron productos", Toast.LENGTH_SHORT).show();
        }

        // Configurar el adaptador del RecyclerView
        productAdapter = new ProductAdapter(getActivity(), productList);
        productAdapter.setOnProductClickListener(this);
        recyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onProductClick(Product product) {
        // Manejar el clic del producto aquí, por ejemplo, abrir una nueva actividad
        // con la información del producto
        // Puedes usar un Intent aquí para iniciar una nueva actividad
        // y pasar la información del producto como extras del Intent
        // Ejemplo:

        Intent intent = new Intent(getActivity(), ProductDetail.class);
        intent.putExtra("productName", product.getPname());
        intent.putExtra("productDescription", product.getDescription());
        intent.putExtra("productPrice", product.getPrice());
        intent.putExtra("productImage", product.getImage());
        startActivity(intent);
    }
}
