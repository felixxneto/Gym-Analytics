package com.projeto.gymanalytics.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.projeto.gymanalytics.R;
import com.projeto.gymanalytics.model.Exercicio;

import java.util.ArrayList;
import java.util.List;

public class NovaSeriDialog extends DialogFragment {

    public interface OnSerieCriadaListener {
        void onSerieCriada(int exercicioId, double pesoKg, int repeticoes, int ordem);
    }

    private final List<Exercicio> exercicios;
    private final OnSerieCriadaListener listener;

    // Recebe a lista já resolvida — não mais LiveData
    public NovaSeriDialog(List<Exercicio> exercicios, OnSerieCriadaListener listener) {
        this.exercicios = exercicios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_nova_serie, null);

        Spinner spinnerExercicio = view.findViewById(R.id.spinnerExercicio);
        EditText editPeso        = view.findViewById(R.id.editPesoKg);
        EditText editReps        = view.findViewById(R.id.editRepeticoes);
        EditText editOrdem       = view.findViewById(R.id.editOrdemSerie);

        // Popula o Spinner com a lista já disponível
        List<String> nomes = new ArrayList<>();
        for (Exercicio e : exercicios) {
            nomes.add(e.nome + " (" + e.grupoMuscular + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                nomes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercicio.setAdapter(adapter);

        return new AlertDialog.Builder(requireContext())
                .setTitle("Nova Série")
                .setView(view)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    int index = spinnerExercicio.getSelectedItemPosition();
                    if (exercicios.isEmpty() || index < 0) return;

                    int exercicioId = exercicios.get(index).id;

                    double peso = 0;
                    int reps    = 0;
                    int ordem   = 1;

                    try {
                        peso  = Double.parseDouble(editPeso.getText().toString());
                        reps  = Integer.parseInt(editReps.getText().toString());
                        ordem = Integer.parseInt(editOrdem.getText().toString());
                    } catch (NumberFormatException e) {
                        // valor inválido — ignora
                    }

                    listener.onSerieCriada(exercicioId, peso, reps, ordem);
                })
                .setNegativeButton("Cancelar", null)
                .create();
    }
}