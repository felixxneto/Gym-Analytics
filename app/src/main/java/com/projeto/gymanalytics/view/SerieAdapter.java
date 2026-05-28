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

public class SerieAdapter extends ListAdapter<SerieComExercicio, SerieAdapter.ViewHolder> {

    public SerieAdapter() {
        super(new DiffUtil.ItemCallback<SerieComExercicio>() {
            @Override public boolean areItemsTheSame(@NonNull SerieComExercicio a, @NonNull SerieComExercicio b) {
                return a.serieId == b.serieId;
            }
            @Override public boolean areContentsTheSame(@NonNull SerieComExercicio a, @NonNull SerieComExercicio b) {
                return a.pesoKg == b.pesoKg && a.repeticoes == b.repeticoes;
            }
        });
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_serie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrdem, txtExercicio, txtGrupo, txtCarga;

        ViewHolder(@NonNull View v) {
            super(v);
            txtOrdem     = v.findViewById(R.id.txtOrdemSerie);
            txtExercicio = v.findViewById(R.id.txtNomeExercicio);
            txtGrupo     = v.findViewById(R.id.txtGrupoMuscular);
            txtCarga     = v.findViewById(R.id.txtCargaSerie);
        }

        void bind(SerieComExercicio s) {
            txtOrdem.setText(String.format("Série %d", s.ordemSerie));
            txtExercicio.setText(s.nomeExercicio);
            txtGrupo.setText(s.grupoMuscular);
            txtCarga.setText(String.format(Locale.getDefault(),
                    "%.1f kg × %d reps", s.pesoKg, s.repeticoes));
        }
    }
}