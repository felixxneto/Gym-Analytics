package com.projeto.gymanalytics.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.projeto.gymanalytics.R;

/**
 * Dialog simples para nomear um novo treino antes de criá-lo.
 */
public class NovoTreinoDialog extends DialogFragment {

    public interface OnTreinoCriadoListener {
        void onTreinoCriado(String nome);
    }

    private final OnTreinoCriadoListener listener;

    public NovoTreinoDialog(OnTreinoCriadoListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_novo_treino, null);

        EditText editNome = view.findViewById(R.id.editNomeTreino);

        return new AlertDialog.Builder(requireContext())
                .setTitle("Novo Treino")
                .setView(view)
                .setPositiveButton("Criar", (dialog, which) -> {
                    String nome = editNome.getText().toString().trim();
                    if (!TextUtils.isEmpty(nome)) {
                        listener.onTreinoCriado(nome);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();
    }
}