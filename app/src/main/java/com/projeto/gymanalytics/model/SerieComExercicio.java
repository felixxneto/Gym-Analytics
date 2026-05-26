package com.projeto.gymanalytics.model;

/**
 * POJO (Plain Old Java Object) — não é uma entidade do Room.
 * Usado exclusivamente como tipo de retorno de queries com JOIN.
 * Encapsula os dados que a UI e o analytics precisam juntos.
 */
public class SerieComExercicio {
    public int serieId;
    public int treinoId;
    public String nomeExercicio;
    public String grupoMuscular;
    public double pesoKg;
    public int repeticoes;
    public int ordemSerie;
}