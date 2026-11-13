package com.jcaa.appcrudusers.entrypoint.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jcaa.appcrudusers.R;
import com.jcaa.appcrudusers.application.dto.response.VueloResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VueloAdapter extends RecyclerView.Adapter<VueloAdapter.VueloViewHolder> {

    public interface OnVueloClickListener {
        void onVueloClick(VueloResponseDto vuelo);
    }

    public interface OnVueloEditListener {
        void onVueloEdit(VueloResponseDto vuelo);
    }

    public interface OnVueloDeleteListener {
        void onVueloDelete(VueloResponseDto vuelo);
    }

    private final List<VueloResponseDto> vuelos = new ArrayList<>();
    private final OnVueloClickListener clickListener;
    private final OnVueloEditListener editListener;
    private final OnVueloDeleteListener deleteListener;

    public VueloAdapter(
            final OnVueloClickListener clickListener,
            final OnVueloEditListener editListener,
            final OnVueloDeleteListener deleteListener
    ) {
        this.clickListener = clickListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    public void setVuelos(final List<VueloResponseDto> newVuelos) {
        this.vuelos.clear();
        if (newVuelos != null) {
            this.vuelos.addAll(newVuelos);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VueloViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vuelo, parent, false);
        return new VueloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VueloViewHolder holder, final int position) {
        final VueloResponseDto vuelo = vuelos.get(position);
        holder.bind(vuelo, clickListener, editListener, deleteListener);
    }

    @Override
    public int getItemCount() {
        return vuelos.size();
    }

    static class VueloViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvVueloCodigo;
        private final TextView tvVueloEstado;
        private final TextView tvVueloRuta;
        private final TextView tvVueloFechas;
        private final TextView tvVueloCliente;
        private final TextView tvVueloValor;
        private final ImageButton btnEditVuelo;
        private final ImageButton btnDeleteVuelo;

        VueloViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvVueloCodigo = itemView.findViewById(R.id.tvVueloCodigo);
            tvVueloEstado = itemView.findViewById(R.id.tvVueloEstado);
            tvVueloRuta = itemView.findViewById(R.id.tvVueloRuta);
            tvVueloFechas = itemView.findViewById(R.id.tvVueloFechas);
            tvVueloCliente = itemView.findViewById(R.id.tvVueloCliente);
            tvVueloValor = itemView.findViewById(R.id.tvVueloValor);
            btnEditVuelo = itemView.findViewById(R.id.btnEditVuelo);
            btnDeleteVuelo = itemView.findViewById(R.id.btnDeleteVuelo);
        }

        void bind(
                final VueloResponseDto vuelo,
                final OnVueloClickListener clickListener,
                final OnVueloEditListener editListener,
                final OnVueloDeleteListener deleteListener
        ) {
            final String codigo = (vuelo.aerolinea() != null ? vuelo.aerolinea() : "") + " " + (vuelo.numero() != null ? vuelo.numero() : "");
            tvVueloCodigo.setText(codigo.trim());
            tvVueloEstado.setText(vuelo.estado() != null ? vuelo.estado() : "");

            final String salida = vuelo.aereopuertoSalida() != null ? vuelo.aereopuertoSalida() : "";
            final String llegada = vuelo.aereopuertoLlegada() != null ? vuelo.aereopuertoLlegada() : "";
            tvVueloRuta.setText(salida + " ➔ " + llegada);

            final String fechas = "Salida: " + (vuelo.fechaSalida() != null ? vuelo.fechaSalida() : "-")
                    + " | Llegada: " + (vuelo.fechaLlegada() != null ? vuelo.fechaLlegada() : "-");
            tvVueloFechas.setText(fechas);

            final String cliente = "Pasajero: " + (vuelo.cliente() != null ? vuelo.cliente() : "-")
                    + " (Puesto: " + (vuelo.puesto() != null ? vuelo.puesto() : "-") + ")";
            tvVueloCliente.setText(cliente);

            final Double valor = vuelo.valor() != null ? vuelo.valor() : 0.0;
            tvVueloValor.setText(String.format(Locale.getDefault(), "$%,.2f", valor));

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onVueloClick(vuelo);
                }
            });

            btnEditVuelo.setOnClickListener(v -> {
                if (editListener != null) {
                    editListener.onVueloEdit(vuelo);
                }
            });

            btnDeleteVuelo.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onVueloDelete(vuelo);
                }
            });
        }
    }
}
