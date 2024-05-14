package com.example.intento;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.intento.model.Product;
import java.io.ByteArrayInputStream;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getPname());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText("COP $" + product.getPrice());

        // Cargar la imagen desde la base de datos utilizando AsyncTask
        new ImageLoadTask(product.getImage(), holder.productImage, context).execute();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productPrice;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }

    private static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private String imageUrl;
        private ImageView imageView;
        private Context context;

        public ImageLoadTask(String imageUrl, ImageView imageView, Context context) {
            this.imageUrl = imageUrl;
            this.imageView = imageView;
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                // Abrir la base de datos en modo lectura
                AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(context);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                // Realizar una consulta para obtener la imagen según la URL
                Cursor cursor = db.rawQuery("SELECT image FROM products WHERE image=?", new String[]{imageUrl});

                Bitmap bitmap = null;
                // Si se encuentra la imagen en la base de datos, decodificarla
                if (cursor != null && cursor.moveToFirst()) {
                    byte[] imageBytes = cursor.getBlob(0);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    cursor.close();
                }
                db.close();
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}
