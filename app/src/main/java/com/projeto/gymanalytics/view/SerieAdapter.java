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
import com.projeto.gymanalytics.model.SerieComExercicio;

/**
 * Adapter para a lista de séries na TreinoDetalheActivity.
 * Exibe os dados do JOIN entre Serie e Exercicio.
 */
public class SerieAdapter extends ListAdapter<SerieComExercicio, SerieAdapter.SerieViewHolder> {

    public SerieAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<SerieComExercicio> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<SerieComExercicio>() {
                @Override
                public boolean areItemsTheSame(@NonNull SerieComExercicio a, @NonNull SerieComExercicio b) {
                    return a.serieId == b.serieId;
                }

                @Override
                public boolean areContentsTheSame(@NonNull SerieComExercicio a, @NonNull SerieComExercicio b) {
                    return a.pesoKg == b.pesoKg
                            && a.repeticoes == b.repeticoes
                            && a.nomeExercicio.equals(b.nomeExercicio);
                }
            };

    @NonNull
    @Override
    public SerieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_serie, parent, false);
        return new SerieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SerieViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class SerieViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtOrdem;
        private final TextView txtExercicio;
        private final TextView txtGrupo;
        private final TextView txtCarga;

        public SerieViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrdem     = itemView.findViewById(R.id.txtOrdemSerie);
            txtExercicio = itemView.findViewById(R.id.txtNomeExercicio);
            txtGrupo     = itemView.findViewById(R.id.txtGrupoMuscular);
            txtCarga     = itemView.findViewById(R.id.txtCargaSerie);
        }

        public void bind(SerieComExercicio serie) {
            txtOrdem.setText(String.format("Série %d", serie.ordemSerie));
            txtExercicio.setText(serie.nomeExercicio);
            txtGrupo.setText(serie.grupoMuscular);
            txtCarga.setText(String.format("%.1f kg × %d reps", serie.pesoKg, serie.repeticoes));
        }
    }
}