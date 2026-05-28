package com.projeto.gymanalytics.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "treino")
public class Treino {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String nome;

    public long dataInicio;
    public Long dataFim;

    public Treino(@NonNull String nome, long dataInicio) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = null;
    }
}