package com.projeto.gymanalytics.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Entidade que representa uma sessão de treino (o evento).
 * data_inicio e data_fim são timestamps em milissegundos (epoch).
 */
@Entity(tableName = "treino")
public class Treino {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String nome;

    public long dataInicio;

    /**
     * Nulo enquanto o treino estiver em andamento.
     * Preenchido ao finalizar a sessão.
     */
    public Long dataFim;

    public Treino(@NonNull String nome, long dataInicio) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = null;
    }
}