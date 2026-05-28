package com.projeto.gymanalytics.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "serie",
        foreignKeys = {
                @ForeignKey(entity = Treino.class,
                        parentColumns = "id", childColumns = "treinoId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Exercicio.class,
                        parentColumns = "id", childColumns = "exercicioId",
                        onDelete = ForeignKey.CASCADE)
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
    public int ordemSerie;

    public Serie(int treinoId, int exercicioId, double pesoKg, int repeticoes, int ordemSerie) {
        this.treinoId = treinoId;
        this.exercicioId = exercicioId;
        this.pesoKg = pesoKg;
        this.repeticoes = repeticoes;
        this.ordemSerie = ordemSerie;
    }
}