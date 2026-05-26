package com.projeto.gymanalytics.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Entidade que representa o catálogo fixo de exercícios.
 * Não armazena dados de esforço — apenas o "o quê" foi feito.
 */
@Entity(tableName = "exercicio")
public class Exercicio {

    @PrimaryKey(autoGenerate = false)
    public int id;

    @NonNull
    public String nome;

    @NonNull
    public String grupoMuscular;

    public Exercicio(@NonNull String nome, @NonNull String grupoMuscular) {
        this.nome = nome;
        this.grupoMuscular = grupoMuscular;
    }
}