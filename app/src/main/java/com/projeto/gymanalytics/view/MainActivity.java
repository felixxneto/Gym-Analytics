package com.projeto.gymanalytics.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.projeto.gymanalytics.R;
import com.projeto.gymanalytics.model.Treino;
import com.projeto.gymanalytics.viewmodel.GymViewModel;

public class MainActivity extends AppCompatActivity {

    private GymViewModel viewModel;
    private TreinoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar — deve ser configurada antes de qualquer outra coisa
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = new ViewModelProvider(this).get(GymViewModel.class);

        // Sincroniza exercícios da API para o Room na abertura
        viewModel.sincronizarExercicios();

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerTreinos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TreinoAdapter(this::abrirDetalhe);
        recyclerView.setAdapter(adapter);

        // Lista vazia
        TextView txtVazio = findViewById(R.id.txtListaVazia);

        // Observa treinos
        viewModel.todosTreinos.observe(this, treinos -> {
            adapter.submitList(treinos);
            txtVazio.setVisibility(
                    treinos == null || treinos.isEmpty() ? View.VISIBLE : View.GONE
            );
        });

        // FAB
        FloatingActionButton fab = findViewById(R.id.fabNovoTreino);
        fab.setOnClickListener(v -> abrirDialogNovoTreino());
    }

    private void abrirDetalhe(Treino treino) {
        Intent intent = new Intent(this, TreinoDetalheActivity.class);
        intent.putExtra("TREINO_ID", treino.id);
        intent.putExtra("TREINO_NOME", treino.nome);
        startActivity(intent);
    }

    private void abrirDialogNovoTreino() {
        NovoTreinoDialog dialog = new NovoTreinoDialog(nome -> {
            Treino treino = new Treino(nome, System.currentTimeMillis());
            viewModel.inserirTreino(treino, id -> runOnUiThread(() -> {
                Intent intent = new Intent(this, TreinoDetalheActivity.class);
                intent.putExtra("TREINO_ID", id);
                intent.putExtra("TREINO_NOME", nome);
                startActivity(intent);
            }));
        });
        dialog.show(getSupportFragmentManager(), "NovoTreinoDialog");
    }
}