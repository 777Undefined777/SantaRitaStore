package com.example.intento;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Shipment extends Fragment {

    private TextView commandUserName;
    private TextView btnAllProduct;
    private TextView commandTotalPrice; // Declarar commandTotalPrice

    public Shipment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shipment, container, false);

        commandUserName = rootView.findViewById(R.id.command_user_name);
        btnAllProduct = rootView.findViewById(R.id.btn_all_product);
        commandTotalPrice = rootView.findViewById(R.id.command_total_price); // Inicializar commandTotalPrice

        // Obtener el nombre de usuario desde SharedPreferences y mostrarlo
        String nombreUsuario = obtenerNombreUsuario();
        commandUserName.setText(nombreUsuario);

        // Obtener y mostrar el total del precio desde SharedPreferences
        float totalPrice = obtenerTotalPrice();
        commandTotalPrice.setText(String.format("Precio: %.2f", totalPrice));

        // Configurar el OnClickListener para el botón "VER"
        btnAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón "VER"
                // Por ejemplo, navegar a otra actividad o fragmento
                Intent intent = new Intent(getActivity(), WebViewActivity.class); // Reemplaza AnotherActivity con la actividad que desees
                startActivity(intent);
            }
        });

        return rootView;
    }

    private String obtenerNombreUsuario() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return preferences.getString("nombre_usuario", "Usuario");
    }

    private float obtenerTotalPrice() {
        SharedPreferences preferences = getActivity().getSharedPreferences("CardData", Context.MODE_PRIVATE);
        return preferences.getFloat("totalPrice", 0.0f);
    }
}
