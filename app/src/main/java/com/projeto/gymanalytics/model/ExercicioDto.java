package com.projeto.gymanalytics.model;

import com.google.gson.annotations.SerializedName;

/**
 * DTO — representa o JSON recebido/enviado pela API.
 * Separado da entidade Room intencionalmente:
 * mudanças no contrato da API não afetam o banco local.
 */
public class ExercicioDto {

    @SerializedName("id")
    public int id;

    @SerializedName("nome")
    public String nome;

    @SerializedName("grupo_muscular")
    public String grupoMuscular;
}