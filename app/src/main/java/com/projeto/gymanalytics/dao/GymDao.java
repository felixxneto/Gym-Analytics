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

/**
 * Interface de acesso ao banco de dados local (Room/SQLite).
 * O Repository é o único consumidor direto desta interface.
 * LiveData garante observabilidade reativa na camada ViewModel/UI.
 */
@Dao
public interface GymDao {

    // -------------------------------------------------------------------------
    // EXERCICIO
    // -------------------------------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void inserirExercicio(Exercicio exercicio);

    @Query("SELECT * FROM exercicio ORDER BY grupoMuscular, nome")
    LiveData<List<Exercicio>> buscarTodosExercicios();

    @Query("SELECT * FROM exercicio WHERE grupoMuscular = :grupo ORDER BY nome")
    LiveData<List<Exercicio>> buscarExerciciosPorGrupo(String grupo);

    // -------------------------------------------------------------------------
    // TREINO
    // -------------------------------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long inserirTreino(Treino treino); // retorna o ID gerado — útil para criar séries em seguida

    @Update
    void atualizarTreino(Treino treino); // usado para preencher dataFim ao finalizar sessão

    @Delete
    void deletarTreino(Treino treino);

    @Query("SELECT * FROM treino ORDER BY dataInicio DESC")
    LiveData<List<Treino>> buscarTodosTreinos();

    @Query("SELECT * FROM treino WHERE id = :treinoId")
    LiveData<Treino> buscarTreinoPorId(int treinoId);

    // -------------------------------------------------------------------------
    // SERIE
    // -------------------------------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserirSerie(Serie serie);

    @Delete
    void deletarSerie(Serie serie);

    @Query("SELECT * FROM serie WHERE treinoId = :treinoId ORDER BY ordemSerie")
    LiveData<List<Serie>> buscarSeriesPorTreino(int treinoId);

    // -------------------------------------------------------------------------
    // QUERY ANALÍTICA — JOIN Serie + Exercicio
    // -------------------------------------------------------------------------

    /**
     * Retorna todas as séries de um treino com os dados do exercício correspondente.
     * Resultado plano (flat JOIN) — ideal para alimentar gráficos de volume e progressão.
     *
     * Volume por grupo muscular = SUM(pesoKg * repeticoes) GROUP BY grupoMuscular
     * Esse cálculo pode ser feito no Repository ou delegado ao analytics.py via API.
     */
    @Query(
            "SELECT " +
                    "    s.id         AS serieId, " +
                    "    s.treinoId   AS treinoId, " +
                    "    e.nome       AS nomeExercicio, " +
                    "    e.grupoMuscular AS grupoMuscular, " +
                    "    s.pesoKg     AS pesoKg, " +
                    "    s.repeticoes AS repeticoes, " +
                    "    s.ordemSerie AS ordemSerie " +
                    "FROM serie s " +
                    "INNER JOIN exercicio e ON s.exercicioId = e.id " +
                    "WHERE s.treinoId = :treinoId " +
                    "ORDER BY s.ordemSerie"
    )
    LiveData<List<SerieComExercicio>> buscarSeriesComExercicioPorTreino(int treinoId);
}