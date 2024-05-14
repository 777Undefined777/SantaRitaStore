package com.example.intento;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommandAdapter extends RecyclerView.Adapter<CommandAdapter.CommandViewHolder> {

    private Cursor mCursor;

    public CommandAdapter(Cursor cursor) {
        mCursor = cursor;
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

        holder.textViewId.setText(String.valueOf(id));
        holder.textViewName.setText(name);
        holder.textViewLastname.setText(lastname);
        holder.textViewPhone.setText(phone);
        holder.textViewCity.setText(city);
        holder.textViewDate.setText(date);
        holder.textViewAddress.setText(address);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public static class CommandViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewId, textViewName, textViewLastname, textViewCity, textViewDate, textViewAddress, textViewPhone;

        public CommandViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewLastname = itemView.findViewById(R.id.textViewLastname);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewCity = itemView.findViewById(R.id.textViewCity);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
        }
    }
}
