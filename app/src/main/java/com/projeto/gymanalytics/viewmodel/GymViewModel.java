package com.projeto.gymanalytics.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.projeto.gymanalytics.model.Exercicio;
import com.projeto.gymanalytics.model.Serie;
import com.projeto.gymanalytics.model.SerieComExercicio;
import com.projeto.gymanalytics.model.Treino;
import com.projeto.gymanalytics.repository.GymRepository;

import java.util.List;

/**
 * ViewModel — único ponto de contato entre a UI e os dados.
 *
 * Herda de AndroidViewModel para ter acesso ao Application context
 * sem segurar referência a Activity (evita memory leak).
 *
 * A UI observa LiveData: quando os dados mudam no Room,
 * a tela atualiza automaticamente — sem polling, sem callbacks manuais.
 */
public class GymViewModel extends AndroidViewModel {

    private final GymRepository repository;

    // LiveData observados pela UI
    public final LiveData<List<Treino>> todosTreinos;
    public final LiveData<List<Exercicio>> todosExercicios;

    public GymViewModel(@NonNull Application application) {
        super(application);
        repository = new GymRepository(application);
        todosTreinos = repository.buscarTodosTreinos();
        todosExercicios = repository.buscarTodosExercicios();
    }

    // -------------------------------------------------------------------------
    // TREINO
    // -------------------------------------------------------------------------

    public void inserirTreino(Treino treino, GymRepository.OnInsertCallback callback) {
        repository.inserirTreino(treino, callback);
    }

    public void finalizarTreino(Treino treino) {
        treino.dataFim = System.currentTimeMillis();
        repository.finalizarTreino(treino);
    }

    // -------------------------------------------------------------------------
    // SERIE
    // -------------------------------------------------------------------------

    public void inserirSerie(Serie serie) {
        repository.inserirSerie(serie);
    }

    /**
     * Retorna LiveData reativo das séries de um treino específico.
     * Chamado pela Activity de detalhe do treino.
     */
    public LiveData<List<SerieComExercicio>> buscarSeriesDoTreino(int treinoId) {
        return repository.buscarSeriesComExercicio(treinoId);
    }
    public LiveData<Treino> buscarTreinoPorId(int treinoId) {
        return repository.buscarTreinoPorId(treinoId);
    }

    public void sincronizarExercicios() {
        repository.sincronizarExercicios();
    }
}