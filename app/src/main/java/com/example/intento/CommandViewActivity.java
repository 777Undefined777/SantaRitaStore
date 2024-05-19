package com.example.intento;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CommandViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CommandAdapter mAdapter;
    private AdminSQLiteOpenHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_view);

        mRecyclerView = findViewById(R.id.recyclerViewCommands);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDbHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = mDbHelper.getAllCommands(db);

        // Obtener todos los detalles de los comandos
        List<CommandDetail> allDetails = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                long commandId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                List<CommandDetail> commandDetails = mDbHelper.getCommandDetails(db, commandId);
                allDetails.addAll(commandDetails);
            } while (cursor.moveToNext());
        }

        mAdapter = new CommandAdapter(cursor, allDetails);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
