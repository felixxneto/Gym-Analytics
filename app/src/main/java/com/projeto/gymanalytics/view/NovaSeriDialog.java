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

    public interface Listener {
        void onCriada(int exercicioId, double pesoKg, int repeticoes, int ordem);
    }

    private final List<Exercicio> exercicios;
    private final Listener listener;

    public NovaSeriDialog(List<Exercicio> exercicios, Listener listener) {
        this.exercicios = exercicios;
        this.listener = listener;
    }

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_nova_serie, null);

        Spinner spinner     = view.findViewById(R.id.spinnerExercicio);
        EditText editPeso   = view.findViewById(R.id.editPesoKg);
        EditText editReps   = view.findViewById(R.id.editRepeticoes);
        EditText editOrdem  = view.findViewById(R.id.editOrdemSerie);

        List<String> nomes = new ArrayList<>();
        for (Exercicio e : exercicios) nomes.add(e.nome + " (" + e.grupoMuscular + ")");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, nomes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return new AlertDialog.Builder(requireContext())
                .setTitle("Nova Série")
                .setView(view)
                .setPositiveButton("Salvar", (d, w) -> {
                    int index = spinner.getSelectedItemPosition();
                    if (index < 0 || exercicios.isEmpty()) return;
                    try {
                        int exercicioId = exercicios.get(index).id;
                        double peso = Double.parseDouble(editPeso.getText().toString());
                        int reps    = Integer.parseInt(editReps.getText().toString());
                        int ordem   = Integer.parseInt(editOrdem.getText().toString());
                        listener.onCriada(exercicioId, peso, reps, ordem);
                    } catch (NumberFormatException e) {
                        // campos inválidos — ignora
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();
    }
}