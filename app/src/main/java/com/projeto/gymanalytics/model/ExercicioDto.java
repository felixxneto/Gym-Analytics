package com.projeto.gymanalytics.model;

import com.google.gson.annotations.SerializedName;

public class ExercicioDto {
    @SerializedName("id")    public int id;
    @SerializedName("nome")  public String nome;
    @SerializedName("grupo_muscular") public String grupoMuscular;
}