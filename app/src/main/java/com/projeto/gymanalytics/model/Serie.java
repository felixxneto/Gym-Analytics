package com.projeto.gymanalytics.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidade que representa o registro de carga de uma série.
 * É o núcleo do pipeline de dados: cada linha é um ponto de dado
 * de esforço físico mensurável (peso x reps x exercício x sessão).
 *
 * ForeignKeys garantem integridade referencial no SQLite.
 * onDelete = CASCADE: se um Treino ou Exercicio for deletado,
 * todas as séries associadas são removidas automaticamente.
 */
@Entity(
        tableName = "serie",
        foreignKeys = {
                @ForeignKey(
                        entity = Treino.class,
                        parentColumns = "id",
                        childColumns = "treinoId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Exercicio.class,
                        parentColumns = "id",
                        childColumns = "exercicioId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "treinoId"),
                @Index(value = "exercicioId")
        }
)
public class Serie {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int treinoId;
    public int exercicioId;

    public double pesoKg;
    public int repeticoes;

    /** Define a ordem de execução dentro do treino. */
    public int ordemSerie;

    public Serie(int treinoId, int exercicioId, double pesoKg, int repeticoes, int ordemSerie) {
        this.treinoId = treinoId;
        this.exercicioId = exercicioId;
        this.pesoKg = pesoKg;
        this.repeticoes = repeticoes;
        this.ordemSerie = ordemSerie;
    }
}