package com.example.intento;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class UserOrderActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter; // Asegúrate de usar OrderAdapter aquí
    private AdminSQLiteOpenHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        mRecyclerView = findViewById(R.id.recycler_view_Command_user);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDbHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Obtener el userId desde SharedPreferences o cualquier otro método
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", -1);

        // Validar que el userId sea válido
        if (userId != -1) {
            Cursor cursor = mDbHelper.getUserCommands(db, userId);

            // Obtener todos los detalles de los comandos
            List<CommandDetail> allDetails = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    long commandId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                    List<CommandDetail> commandDetails = mDbHelper.getCommandDetails(db, commandId);
                    allDetails.addAll(commandDetails);
                } while (cursor.moveToNext());
            }

            mAdapter = new OrderAdapter(cursor, allDetails);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            // Manejar el caso donde no se encontró un userId válido
        }
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
