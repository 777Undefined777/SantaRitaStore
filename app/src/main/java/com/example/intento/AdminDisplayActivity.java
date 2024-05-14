package com.example.intento;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intento.model.Card;

import java.util.ArrayList;
import java.util.List;

public class AdminDisplayActivity extends AppCompatActivity {

    private RecyclerView productsList;
    private RecyclerView.LayoutManager layoutManager;
    private List<Card> cardList = new ArrayList<>();
    private String pid = "";
    private ImageView fer_disp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_display);

        pid = getIntent().getStringExtra("pid");
        productsList = findViewById(R.id.product_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);
        fer_disp = findViewById(R.id.fer_disp);

        fer_disp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDisplayActivity.this, AdminCommandActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        // Aquí debes recuperar los productos de la base de datos local SQLite y agregarlos a cardList
        // Supongamos que tienes un método para obtener los productos de la base de datos local llamado getProductsFromLocalDB()
        cardList = getProductsFromLocalDB();

        // Luego, puedes configurar el adaptador y asignarlo al RecyclerView
        CardAdapter adapter = new CardAdapter(cardList);
        productsList.setAdapter(adapter);
    }

    // Método para obtener los productos de la base de datos local (debes implementarlo según tu lógica)
    private List<Card> getProductsFromLocalDB() {
        // Aquí deberías implementar la lógica para obtener los productos de la base de datos local SQLite
        // y devolver una lista de objetos Card
        // Por ahora, retornamos una lista vacía
        return new ArrayList<>();
    }

    private class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

        private List<Card> cardList;

        CardAdapter(List<Card> cardList) {
            this.cardList = cardList;
        }

        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items_layout, parent, false);
            return new CardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
            Card card = cardList.get(position);
            holder.txtProductQuantity.setText(card.getQuantity());
            holder.txtProductPrice.setText(card.getPrice());
            holder.txtProductName.setText(card.getPname());
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        class CardViewHolder extends RecyclerView.ViewHolder {

            TextView txtProductName, txtProductPrice, txtProductQuantity;

            CardViewHolder(View itemView) {
                super(itemView);
                txtProductName = itemView.findViewById(R.id.product_name);
                txtProductPrice = itemView.findViewById(R.id.product_price);
                txtProductQuantity = itemView.findViewById(R.id.product_quantity);
            }
        }
    }
}
