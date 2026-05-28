package com.projeto.gymanalytics.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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