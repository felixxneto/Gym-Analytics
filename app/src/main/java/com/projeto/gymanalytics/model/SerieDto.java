package com.projeto.gymanalytics.model;

import com.google.gson.annotations.SerializedName;

public class SerieDto {
    @SerializedName("id")          public int id;
    @SerializedName("treino_id")   public int treinoId;
    @SerializedName("exercicio_id") public int exercicioId;
    @SerializedName("peso_kg")     public double pesoKg;
    @SerializedName("repeticoes")  public int repeticoes;
    @SerializedName("ordem_serie") public int ordemSerie;
}