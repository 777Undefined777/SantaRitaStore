package com.example.intento;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CommandAdapter extends RecyclerView.Adapter<CommandAdapter.CommandViewHolder> {

    private Cursor mCursor;
    private List<CommandDetail> commandDetails;

    public CommandAdapter(Cursor cursor, List<CommandDetail> commandDetails) {
        mCursor = cursor;
        this.commandDetails = commandDetails;
    }

    @NonNull
    @Override
    public CommandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_command_adapter, parent, false);
        return new CommandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommandViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        @SuppressLint("Range") int id = mCursor.getInt(mCursor.getColumnIndex("id"));
        @SuppressLint("Range") String name = mCursor.getString(mCursor.getColumnIndex("name"));
        @SuppressLint("Range") String lastname = mCursor.getString(mCursor.getColumnIndex("lastname"));
        @SuppressLint("Range") String phone = mCursor.getString(mCursor.getColumnIndex("phone"));
        @SuppressLint("Range") String city = mCursor.getString(mCursor.getColumnIndex("city"));
        @SuppressLint("Range") String date = mCursor.getString(mCursor.getColumnIndex("date"));
        @SuppressLint("Range") String address = mCursor.getString(mCursor.getColumnIndex("address"));

        holder.textViewId.setText("Pedido Numero  :"+String.valueOf(id));
        holder.textViewName.setText("Nombre: " + name);
        holder.textViewLastname.setText("Apellido: " + lastname);
        holder.textViewPhone.setText("Teléfono: " + phone);
        holder.textViewCity.setText("Ciudad/Pueblo: " + city);
        holder.textViewDate.setText("Fecha de pedido: " + date);
        holder.textViewAddress.setText("Dirección: " + address);

        // Iniciar animación de rotación
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(holder.flowerImage, "rotationY", 0f, 360f);
        rotateAnimation.setDuration(3000); // Duración de la animación
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(ObjectAnimator.INFINITE); // Repetir la animación infinitamente
        rotateAnimation.start();

        // Filtrar la lista de detalles de productos para obtener solo los detalles del producto del pedido actual
        List<CommandDetail> filteredDetails = new ArrayList<>();
        for (CommandDetail detail : commandDetails) {
            if (detail.getCommandId() == id) {
                filteredDetails.add(detail);
            }
        }

        // Mostrar los detalles del producto
        for (CommandDetail detail : filteredDetails) {
            holder.textViewProductName.append("Nombre producto: " + detail.getProductName() + "\n");
            holder.textViewProductPrice.append("Precio Unitario: " + detail.getPrice() + "\n");
            holder.textViewProductQuantity.append("Cantidad: " + detail.getQuantity() + "\n");
            holder.textViewProductTotalPrice.append("Precio X Cantidad: " + detail.getTotalPrice() + "\n");
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public static class CommandViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewId, textViewName, textViewLastname, textViewCity, textViewDate, textViewAddress, textViewPhone;
        public TextView textViewProductName, textViewProductPrice, textViewProductQuantity, textViewProductTotalPrice;
        public ImageView flowerImage;

        public CommandViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewLastname = itemView.findViewById(R.id.textViewLastname);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewCity = itemView.findViewById(R.id.textViewCity);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewProductQuantity = itemView.findViewById(R.id.textViewProductQuantity);
            textViewProductTotalPrice = itemView.findViewById(R.id.textViewProductTotalPrice);
            flowerImage = itemView.findViewById(R.id.flowerImage); // Asegúrate de que tu ImageView tenga este ID en tu layout
        }
    }
}
