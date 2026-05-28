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

public class GymViewModel extends AndroidViewModel {

    private final GymRepository repository;
    public final LiveData<List<Treino>> todosTreinos;
    public final LiveData<List<Exercicio>> todosExercicios;

    public GymViewModel(@NonNull Application application) {
        super(application);
        repository = new GymRepository(application);
        todosTreinos = repository.buscarTodosTreinos();
        todosExercicios = repository.buscarTodosExercicios();
    }

    public void sincronizarExercicios() {
        repository.sincronizarExercicios();
    }

    public void inserirTreino(Treino treino, GymRepository.OnInsertCallback callback) {
        repository.inserirTreino(treino, callback);
    }

    public void finalizarTreino(Treino treino) {
        repository.finalizarTreino(treino);
    }

    public void inserirSerie(Serie serie) {
        repository.inserirSerie(serie);
    }

    public LiveData<List<SerieComExercicio>> buscarSeriesDoTreino(int treinoId) {
        return repository.buscarSeriesComExercicio(treinoId);
    }

    public LiveData<Treino> buscarTreinoPorId(int treinoId) {
        return repository.buscarTreinoPorId(treinoId);
    }
}