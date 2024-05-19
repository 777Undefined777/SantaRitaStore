package com.example.intento;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.intento.model.Card;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class card_activity extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnNext;
    private TextView totalTextView;
    private double totalPrice = 0.0; // Cambia a double para manejar valores decimales
    private ImageView btnRetour;
    private SQLiteDatabase mDatabase;
    private List<Card> cardList;
    private CardAdapter cardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_activity, container, false);

        // Inicializar la base de datos SQLite
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(getActivity());
        mDatabase = dbHelper.getWritableDatabase();

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.card_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Configurar botones y texto total
        btnNext = view.findViewById(R.id.next_card);
        btnRetour = view.findViewById(R.id.retour);
        totalTextView = view.findViewById(R.id.total);

        // Configurar manejadores de clic para botones
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToSharedPreferences();
                Intent intent = new Intent(getActivity(), CommandAdminCheck.class);
                startActivity(intent);
            }
        });

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        // Configurar la lista de tarjetas y el adaptador
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(cardList);
        recyclerView.setAdapter(cardAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadCards();
    }

    private void loadCards() {
        totalPrice = 0.0;
        cardList.clear();

        // Consultar los productos guardados en la base de datos
        HomeActivity homeActivity = (HomeActivity) getActivity();
        int usuarioId = homeActivity.obtenerUsuarioId();

        // Consulta para obtener los productos del carrito del usuario actual
        String query = "SELECT * FROM cards WHERE user_id = ?";
        Cursor cursor = mDatabase.rawQuery(query, new String[]{String.valueOf(usuarioId)});

        if (cursor.moveToFirst()) {
            do {
                // Obtener datos de cada producto
                String pid = cursor.getString(cursor.getColumnIndexOrThrow("pid"));
                String pname = cursor.getString(cursor.getColumnIndexOrThrow("pname"));
                String priceStr = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                String quantityStr = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));

                // Convertir valores de precio y cantidad a números
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);

                // Calcular el total de cada producto
                double totalForOne = price * quantity;
                totalPrice += totalForOne;

                // Crear un objeto Card con los datos
                Card card = new Card(pid, pname, priceStr, "", quantityStr);
                cardList.add(card);

            } while (cursor.moveToNext());
        }

        cursor.close();

        // Mostrar el total en el TextView
        totalTextView.setText(String.format("%.2f", totalPrice));

        // Notificar al adaptador que los datos han cambiado
        cardAdapter.notifyDataSetChanged();
    }

    private void saveDataToSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("CardData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Guardar el total del precio
        editor.putFloat("totalPrice", (float) totalPrice);

        // Guardar los productos en formato JSON
        JSONArray jsonArray = new JSONArray();
        for (Card card : cardList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("product_name", card.getPname());
                jsonObject.put("price", card.getPrice());
                jsonObject.put("quantity", card.getQuantity());
                double totalPriceForOne = Integer.parseInt(card.getQuantity()) * Double.parseDouble(card.getPrice());
                jsonObject.put("total_price", totalPriceForOne);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString("productList", jsonArray.toString());

        editor.apply();
    }
}
