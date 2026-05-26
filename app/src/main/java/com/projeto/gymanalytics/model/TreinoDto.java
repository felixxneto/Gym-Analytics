package com.projeto.gymanalytics.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TreinoDto {

    @SerializedName("id")
    public int id;

    @SerializedName("nome")
    public String nome;

    @SerializedName("data_inicio")
    public long dataInicio;

    @SerializedName("data_fim")
    public Long dataFim;

    @SerializedName("series")
    public List<SerieDto> series;
}