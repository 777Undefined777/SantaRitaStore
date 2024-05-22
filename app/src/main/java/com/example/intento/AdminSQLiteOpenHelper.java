package com.example.intento;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "santa_rita_shop_db";
    // Nueva versión de la base de datos
    private static final int DATABASE_VERSION = 4;

    public AdminSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Método para crear la estructura de la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de usuarios
        String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "phone TEXT,"
                + "password TEXT,"
                + "image TEXT,"
                + "address TEXT,"
                + "gender TEXT)";
        db.execSQL(createUserTableQuery);

        // Crear tabla de pedidos
        String createCommandTableQuery = "CREATE TABLE IF NOT EXISTS commands ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "lastname TEXT,"
                + "phone TEXT,"
                + "address TEXT,"
                + "city TEXT,"
                + "cedula TEXT,"
                + "state TEXT,"
                + "date TEXT,"
                + "totalPrice TEXT,"
                + "command_id INTEGER,"
                + "product_id INTEGER,"
                + "user_id INTEGER,"
                + "FOREIGN KEY (command_id) REFERENCES commands(id),"
                + "FOREIGN KEY (product_id) REFERENCES products(id),"
                + "FOREIGN KEY (user_id) REFERENCES users(id))";
        db.execSQL(createCommandTableQuery);

        // Crear tabla de productos
        String createProductTableQuery = "CREATE TABLE IF NOT EXISTS products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "pname TEXT,"
                + "description TEXT,"
                + "price TEXT,"
                + "image blob,"
                + "category TEXT,"
                + "date TEXT,"
                + "time TEXT)";
        db.execSQL(createProductTableQuery);

        // Crear tabla detalled pedido
        String createCommandDetailTableQuery = "CREATE TABLE IF NOT EXISTS command_detail ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "product_name TEXT,"
                + "price TEXT,"
                + "quantity INTEGER,"
                + "total_price REAL,"
                + "command_id INTEGER," // Agregada la columna command_id
                + "FOREIGN KEY (command_id) REFERENCES commands(id))";
        db.execSQL(createCommandDetailTableQuery);

        // Crear tabla de carrito
        String createCardTableQuery = "CREATE TABLE IF NOT EXISTS cards ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "pid INTEGER,"
                + "pname TEXT,"
                + "price TEXT,"
                + "discount TEXT,"
                + "quantity TEXT,"
                + "command_id INTEGER,"
                + "product_id INTEGER,"
                + "user_id INTEGER,"
                + "FOREIGN KEY (command_id) REFERENCES commands(id),"
                + "FOREIGN KEY (product_id) REFERENCES products(id),"
                + "FOREIGN KEY (user_id) REFERENCES users(id))";
        db.execSQL(createCardTableQuery);
    }

    // Método para actualizar la estructura de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar las tablas si existen
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS commands");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS cards");
        db.execSQL("DROP TABLE IF EXISTS command_detail");
        // Crear la estructura de la base de datos nuevamente
        onCreate(db);
    }

    // Método para agregar un nuevo producto a la base de datos
    public void addProduct(SQLiteDatabase db, String productName, String description, String price, byte [] image, String category, String date, String time) {
        ContentValues values = new ContentValues();
        values.put("pname", productName);
        values.put("description", description);
        values.put("price", price);
        values.put("image", image);
        values.put("category", category);
        values.put("date", date);
        values.put("time", time);

        db.insert("products", null, values);
    }

    // Método para obtener todos los productos de la base de datos
    public Cursor getAllProducts(SQLiteDatabase db) {
        // Define la consulta SQL para obtener todos los productos
        String query = "SELECT * FROM products";

        // Ejecuta la consulta y devuelve el cursor con los resultados
        return db.rawQuery(query, null);
    }

    public Cursor getAllCommands(SQLiteDatabase db) {
        String query = "SELECT id, name, lastname, phone, city, date, address FROM commands";
        return db.rawQuery(query, null);
    }

    public List<CommandDetail> getCommandDetails(SQLiteDatabase db, long commandId) {
        List<CommandDetail> details = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM command_detail WHERE command_id = ?", new String[]{String.valueOf(commandId)});

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                long cmdId = cursor.getLong(cursor.getColumnIndexOrThrow("command_id"));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));

                details.add(new CommandDetail(id, cmdId, productName, price, quantity, totalPrice));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return details;
    }

    // Método para eliminar un producto de la base de datos
    public void deleteProduct(SQLiteDatabase db, int productId) {
        db.delete("products", "id = ?", new String[]{String.valueOf(productId)});
    }
}
