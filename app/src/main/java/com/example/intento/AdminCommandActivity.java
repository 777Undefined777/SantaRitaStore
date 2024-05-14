package com.example.intento;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intento.AdminDisplayActivity;
import com.example.intento.AdminHomeActivity;
import com.example.intento.AdminSQLiteOpenHelper;
import com.example.intento.R;

public class AdminCommandActivity extends AppCompatActivity {

    private RecyclerView commandList;
    private SQLiteDatabase db;
    private ImageView fer_com;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_command);

        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this);
        db = dbHelper.getWritableDatabase();

        commandList = findViewById(R.id.command_list);
        commandList.setLayoutManager(new LinearLayoutManager(this));
        fer_com = findViewById(R.id.fer_com);
        fer_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCommandActivity.this, AdminHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Cursor cursor = db.rawQuery("SELECT * FROM commands", null);

        CommandAdapter adapter = new CommandAdapter(cursor);
        commandList.setAdapter(adapter);
    }

    private class CommandAdapter extends RecyclerView.Adapter<CommandAdapter.CommandViewHolder> {

        private Cursor cursor;

        CommandAdapter(Cursor cursor) {
            this.cursor = cursor;
        }

        @NonNull
        @Override
        public CommandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.command_layout, parent, false);
            return new CommandViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommandAdapter.CommandViewHolder holder, int position) {
            if (!cursor.moveToPosition(position)) {
                return;
            }

            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
            @SuppressLint("Range") String city = cursor.getString(cursor.getColumnIndex("city"));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range") String totalPrice = cursor.getString(cursor.getColumnIndex("totalPrice"));

            holder.userName.setText(name);
            holder.userPhone.setText(phone);
            holder.userCity.setText(address + " " + city);
            holder.userDateTime.setText(date + " " + time);
            holder.userPrice.setText(totalPrice + " £");

            @SuppressLint("Range") final int pid = cursor.getInt(cursor.getColumnIndex("id"));

            holder.btn_all_products.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminCommandActivity.this, AdminDisplayActivity.class);
                    intent.putExtra("pid", pid);
                    startActivity(intent);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence sequence[] = new CharSequence[]{
                            "Oui", "Non"
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminCommandActivity.this);
                    builder.setTitle("Commande livré ou non:");
                    builder.setItems(sequence, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                removeCommand(pid);
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        private void removeCommand(int pid) {
            db.delete("commands", "id = ?", new String[]{String.valueOf(pid)});
            notifyDataSetChanged();
        }

        class CommandViewHolder extends RecyclerView.ViewHolder {

            TextView userName, userPhone, userCity, userDateTime, userPrice;
            Button btn_all_products;

            CommandViewHolder(View itemView) {
                super(itemView);

                userName = itemView.findViewById(R.id.command_user_name);
                userPhone = itemView.findViewById(R.id.command_phone_number);
                userCity = itemView.findViewById(R.id.command_address_city);
                userDateTime = itemView.findViewById(R.id.command_date_time);
                userPrice = itemView.findViewById(R.id.command_total_price);
                btn_all_products = itemView.findViewById(R.id.btn_all_product);
            }
        }
    }
}
