package com.projeto.gymanalytics.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import androidx.lifecycle.LiveData;

import com.projeto.gymanalytics.dao.GymDao;
import com.projeto.gymanalytics.database.GymDatabase;
import com.projeto.gymanalytics.model.Exercicio;
import com.projeto.gymanalytics.model.Serie;
import com.projeto.gymanalytics.model.SerieComExercicio;
import com.projeto.gymanalytics.model.SerieDto;
import com.projeto.gymanalytics.model.Treino;
import com.projeto.gymanalytics.model.TreinoDto;
import com.projeto.gymanalytics.model.ExercicioDto;
import com.projeto.gymanalytics.network.ApiService;
import com.projeto.gymanalytics.network.RetrofitClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GymRepository {

    private final GymDao gymDao;
    private final ApiService apiService;
    private final Context context;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    public GymRepository(Context context) {
        this.context = context.getApplicationContext();
        this.gymDao = GymDatabase.getInstance(this.context).gymDao();
        this.apiService = RetrofitClient.getApiService();
    }

    // --- Exercicio ---

    public LiveData<List<Exercicio>> buscarTodosExercicios() {
        return gymDao.buscarTodosExercicios();
    }

    public void sincronizarExercicios() {
        if (!isConectado()) return;
        apiService.listarExercicios().enqueue(new Callback<List<ExercicioDto>>() {
            @Override
            public void onResponse(Call<List<ExercicioDto>> call, Response<List<ExercicioDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executor.execute(() -> {
                        for (ExercicioDto dto : response.body()) {
                            Exercicio e = new Exercicio(dto.nome, dto.grupoMuscular);
                            e.id = dto.id;
                            gymDao.inserirExercicio(e);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<ExercicioDto>> call, Throwable t) {}
        });
    }

    // --- Treino ---

    public LiveData<List<Treino>> buscarTodosTreinos() {
        return gymDao.buscarTodosTreinos();
    }

    public LiveData<Treino> buscarTreinoPorId(int id) {
        return gymDao.buscarTreinoPorId(id);
    }

    public void inserirTreino(Treino treino, OnInsertCallback callback) {
        executor.execute(() -> {
            long id = gymDao.inserirTreino(treino);
            treino.id = (int) id;
            if (callback != null) callback.onInserted((int) id);
            if (isConectado()) {
                TreinoDto dto = new TreinoDto();
                dto.id = treino.id;
                dto.nome = treino.nome;
                dto.dataInicio = treino.dataInicio;
                dto.dataFim = treino.dataFim;
                apiService.criarTreino(dto).enqueue(new Callback<TreinoDto>() {
                    @Override public void onResponse(Call<TreinoDto> c, Response<TreinoDto> r) {}
                    @Override public void onFailure(Call<TreinoDto> c, Throwable t) {}
                });
            }
        });
    }

    public void finalizarTreino(Treino treino) {
        treino.dataFim = System.currentTimeMillis();
        executor.execute(() -> gymDao.atualizarTreino(treino));
    }

    // --- Serie ---

    public LiveData<List<SerieComExercicio>> buscarSeriesComExercicio(int treinoId) {
        return gymDao.buscarSeriesComExercicioPorTreino(treinoId);
    }

    public void inserirSerie(Serie serie) {
        executor.execute(() -> {
            gymDao.inserirSerie(serie);
            if (isConectado()) {
                SerieDto dto = new SerieDto();
                dto.treinoId = serie.treinoId;
                dto.exercicioId = serie.exercicioId;
                dto.pesoKg = serie.pesoKg;
                dto.repeticoes = serie.repeticoes;
                dto.ordemSerie = serie.ordemSerie;
                apiService.criarSerie(dto).enqueue(new Callback<SerieDto>() {
                    @Override public void onResponse(Call<SerieDto> c, Response<SerieDto> r) {}
                    @Override public void onFailure(Call<SerieDto> c, Throwable t) {}
                });
            }
        });
    }

    // --- Utilitário ---

    private boolean isConectado() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkCapabilities caps = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    public interface OnInsertCallback {
        void onInserted(int id);
    }
}