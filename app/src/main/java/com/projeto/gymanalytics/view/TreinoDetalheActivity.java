package com.projeto.gymanalytics.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.projeto.gymanalytics.R;
import com.projeto.gymanalytics.model.Exercicio;
import com.projeto.gymanalytics.model.Serie;
import com.projeto.gymanalytics.model.Treino;
import com.projeto.gymanalytics.viewmodel.GymViewModel;

import java.util.List;

public class TreinoDetalheActivity extends AppCompatActivity {

    private GymViewModel viewModel;
    private SerieAdapter adapter;
    private int treinoId;

    // Lista de exercícios cacheada — carregada uma vez, reutilizada no dialog
    private List<Exercicio> exerciciosCacheados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino_detalhe);

        treinoId = getIntent().getIntExtra("TREINO_ID", -1);
        String treinoNome = getIntent().getStringExtra("TREINO_NOME");

        // Toolbar — sempre primeiro
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(treinoNome);
        }

        viewModel = new ViewModelProvider(this).get(GymViewModel.class);

        // RecyclerView de séries
        RecyclerView recyclerView = findViewById(R.id.recyclerSeries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SerieAdapter();
        recyclerView.setAdapter(adapter);

        // Observa séries do treino
        viewModel.buscarSeriesDoTreino(treinoId).observe(this,
                series -> adapter.submitList(series));

        // Cacheia exercícios — observer registrado uma única vez
        viewModel.todosExercicios.observe(this, exercicios ->
                exerciciosCacheados = exercicios);

        // Botão finalizar — observer registrado uma única vez
        configurarBotaoFinalizar();

        // FAB adicionar série
        FloatingActionButton fab = findViewById(R.id.fabAdicionarSerie);
        fab.setOnClickListener(v -> abrirDialogNovaSerie());
    }

    private void configurarBotaoFinalizar() {
        MaterialButton btnFinalizar = findViewById(R.id.btnFinalizarTreino);

        viewModel.buscarTreinoPorId(treinoId).observe(this, treino -> {
            if (treino == null) return;

            // Atualiza estado visual do botão conforme status do treino
            if (treino.dataFim != null) {
                btnFinalizar.setText("Treino Finalizado");
                btnFinalizar.setEnabled(false);
            } else {
                btnFinalizar.setText("Finalizar Treino");
                btnFinalizar.setEnabled(true);
            }

            btnFinalizar.setOnClickListener(v -> {
                viewModel.finalizarTreino(treino);
                Toast.makeText(this, "Treino finalizado!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void abrirDialogNovaSerie() {
        if (exerciciosCacheados == null || exerciciosCacheados.isEmpty()) {
            Toast.makeText(this, "Aguarde, carregando exercícios...", Toast.LENGTH_SHORT).show();
            return;
        }

        NovaSeriDialog dialog = new NovaSeriDialog(
                exerciciosCacheados,
                (exercicioId, peso, reps, ordem) -> {
                    Serie serie = new Serie(treinoId, exercicioId, peso, reps, ordem);
                    viewModel.inserirSerie(serie);
                }
        );
        dialog.show(getSupportFragmentManager(), "NovaSerieDialog");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}