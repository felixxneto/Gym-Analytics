package com.projeto.gymanalytics.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.projeto.gymanalytics.model.Exercicio;
import com.projeto.gymanalytics.model.Serie;
import com.projeto.gymanalytics.model.SerieComExercicio;
import com.projeto.gymanalytics.model.Treino;

import java.util.List;

@Dao
public interface GymDao {

    // Exercicio
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void inserirExercicio(Exercicio exercicio);

    @Query("SELECT * FROM exercicio ORDER BY grupoMuscular, nome")
    LiveData<List<Exercicio>> buscarTodosExercicios();

    // Treino
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long inserirTreino(Treino treino);

    @Update
    void atualizarTreino(Treino treino);

    @Delete
    void deletarTreino(Treino treino);

    @Query("SELECT * FROM treino ORDER BY dataInicio DESC")
    LiveData<List<Treino>> buscarTodosTreinos();

    @Query("SELECT * FROM treino WHERE id = :treinoId")
    LiveData<Treino> buscarTreinoPorId(int treinoId);

    // Serie
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserirSerie(Serie serie);

    @Delete
    void deletarSerie(Serie serie);

    @Query("SELECT s.id AS serieId, s.treinoId AS treinoId, " +
            "e.nome AS nomeExercicio, e.grupoMuscular AS grupoMuscular, " +
            "s.pesoKg AS pesoKg, s.repeticoes AS repeticoes, s.ordemSerie AS ordemSerie " +
            "FROM serie s INNER JOIN exercicio e ON s.exercicioId = e.id " +
            "WHERE s.treinoId = :treinoId ORDER BY s.ordemSerie")
    LiveData<List<SerieComExercicio>> buscarSeriesComExercicioPorTreino(int treinoId);
}