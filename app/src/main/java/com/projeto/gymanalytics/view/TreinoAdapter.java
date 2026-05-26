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

/**
 * Adapter para a lista de treinos na MainActivity.
 * Usa ListAdapter + DiffUtil: só re-renderiza itens que mudaram.
 */
public class TreinoAdapter extends ListAdapter<Treino, TreinoAdapter.TreinoViewHolder> {

    public interface OnTreinoClickListener {
        void onClick(Treino treino);
    }

    private final OnTreinoClickListener listener;

    public TreinoAdapter(OnTreinoClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Treino> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Treino>() {
                @Override
                public boolean areItemsTheSame(@NonNull Treino a, @NonNull Treino b) {
                    return a.id == b.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Treino a, @NonNull Treino b) {
                    return a.nome.equals(b.nome) && a.dataFim == b.dataFim;
                }
            };

    @NonNull
    @Override
    public TreinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_treino, parent, false);
        return new TreinoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreinoViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class TreinoViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtNome;
        private final TextView txtData;
        private final TextView txtStatus;

        public TreinoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome   = itemView.findViewById(R.id.txtNomeTreino);
            txtData   = itemView.findViewById(R.id.txtDataTreino);
            txtStatus = itemView.findViewById(R.id.txtStatusTreino);
        }

        public void bind(Treino treino, OnTreinoClickListener listener) {
            txtNome.setText(treino.nome);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            txtData.setText(sdf.format(new Date(treino.dataInicio)));

            if (treino.dataFim == null) {
                txtStatus.setText("Em andamento");
            } else {
                txtStatus.setText("Finalizado");
            }

            itemView.setOnClickListener(v -> listener.onClick(treino));
        }
    }
}