package com.example.intento;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                + "image TEXT,"
                + "category TEXT,"
                + "date TEXT,"
                + "time TEXT)";
        db.execSQL(createProductTableQuery);

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
        // Crear la estructura de la base de datos nuevamente
        onCreate(db);
    }

    // Método para agregar un nuevo producto a la base de datos
    public void addProduct(SQLiteDatabase db, String productName, String description, String price, String imageUrl, String category, String date, String time) {
        ContentValues values = new ContentValues();
        values.put("pname", productName);
        values.put("description", description);
        values.put("price", price);
        values.put("image", imageUrl);
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
}
