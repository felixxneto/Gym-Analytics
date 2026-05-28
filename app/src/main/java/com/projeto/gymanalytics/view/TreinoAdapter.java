package com.projeto.gymanalytics.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.projeto.gymanalytics.R;
import com.projeto.gymanalytics.model.Treino;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TreinoAdapter extends ListAdapter<Treino, TreinoAdapter.ViewHolder> {

    public interface OnClickListener { void onClick(Treino treino); }
    private final OnClickListener listener;

    public TreinoAdapter(OnClickListener listener) {
        super(new DiffUtil.ItemCallback<Treino>() {
            @Override public boolean areItemsTheSame(@NonNull Treino a, @NonNull Treino b) {
                return a.id == b.id;
            }
            @Override public boolean areContentsTheSame(@NonNull Treino a, @NonNull Treino b) {
                return a.nome.equals(b.nome) && a.dataFim == b.dataFim;
            }
        });
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_treino, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtData, txtStatus;

        ViewHolder(@NonNull View v) {
            super(v);
            txtNome   = v.findViewById(R.id.txtNomeTreino);
            txtData   = v.findViewById(R.id.txtDataTreino);
            txtStatus = v.findViewById(R.id.txtStatusTreino);
        }

        void bind(Treino treino, OnClickListener listener) {
            txtNome.setText(treino.nome);
            txtData.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm",
                    Locale.getDefault()).format(new Date(treino.dataInicio)));
            txtStatus.setText(treino.dataFim == null ? "Em andamento" : "Finalizado");
            itemView.setOnClickListener(v -> listener.onClick(treino));
        }
    }
}