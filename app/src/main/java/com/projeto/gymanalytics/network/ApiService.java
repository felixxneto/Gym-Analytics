package com.projeto.gymanalytics.network;

import com.projeto.gymanalytics.model.ExercicioDto;
import com.projeto.gymanalytics.model.SerieDto;
import com.projeto.gymanalytics.model.TreinoDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("exercicios/")
    Call<List<ExercicioDto>> listarExercicios();

    @POST("treinos/")
    Call<TreinoDto> criarTreino(@Body TreinoDto treino);

    @GET("treinos/")
    Call<List<TreinoDto>> listarTreinos();

    @POST("series/")
    Call<SerieDto> criarSerie(@Body SerieDto serie);

    @GET("treinos/{id}/series/")
    Call<List<SerieDto>> listarSeriesPorTreino(@Path("id") int treinoId);
}