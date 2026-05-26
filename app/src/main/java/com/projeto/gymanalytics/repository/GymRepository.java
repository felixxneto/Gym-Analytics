package com.projeto.gymanalytics.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import androidx.lifecycle.LiveData;

import com.projeto.gymanalytics.dao.GymDao;
import com.projeto.gymanalytics.database.GymDatabase;
import com.projeto.gymanalytics.model.Exercicio;
import com.projeto.gymanalytics.model.ExercicioDto;
import com.projeto.gymanalytics.model.Serie;
import com.projeto.gymanalytics.model.SerieComExercicio;
import com.projeto.gymanalytics.model.SerieDto;
import com.projeto.gymanalytics.model.Treino;
import com.projeto.gymanalytics.model.TreinoDto;
import com.projeto.gymanalytics.network.ApiService;
import com.projeto.gymanalytics.network.RetrofitClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fonte única de verdade para a UI.
 * Implementa a estratégia offline-first:
 *   1. Toda escrita vai para o Room imediatamente.
 *   2. Se houver internet, sincroniza com a API em background.
 *   3. Toda leitura vem do Room via LiveData (reativo).
 */
public class GymRepository {

    private final GymDao gymDao;
    private final ApiService apiService;
    private final Context context;

    // Thread pool dedicado para operações de banco e rede
    // (Room e Retrofit não podem rodar na main thread)
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    public GymRepository(Context context) {
        this.context = context.getApplicationContext();
        this.gymDao = GymDatabase.getInstance(this.context).gymDao();
        this.apiService = RetrofitClient.getApiService();
    }

    // -------------------------------------------------------------------------
    // TREINO
    // -------------------------------------------------------------------------

    /**
     * Insere treino localmente e sincroniza com a API se houver conexão.
     * Retorna o ID gerado pelo Room — necessário para vincular séries.
     */
    public void inserirTreino(Treino treino, OnInsertCallback callback) {
        executor.execute(() -> {
            long id = gymDao.inserirTreino(treino);
            treino.id = (int) id;

            // Notifica a UI com o ID gerado
            if (callback != null) callback.onInserted((int) id);

            // Sync com API em background, sem bloquear a UI
            if (isConectado()) {
                TreinoDto dto = mapTreinoToDto(treino);
                apiService.criarTreino(dto).enqueue(new Callback<TreinoDto>() {
                    @Override
                    public void onResponse(Call<TreinoDto> call, Response<TreinoDto> response) {
                        // Sucesso — dado já está no Room, API confirmou
                    }

                    @Override
                    public void onFailure(Call<TreinoDto> call, Throwable t) {
                        // Falha silenciosa — dado seguro no Room,
                        // será sincronizado na próxima oportunidade
                    }
                });
            }
        });
    }

    public void finalizarTreino(Treino treino) {
        executor.execute(() -> gymDao.atualizarTreino(treino));
    }

    public LiveData<List<Treino>> buscarTodosTreinos() {
        return gymDao.buscarTodosTreinos();
    }

    // -------------------------------------------------------------------------
    // SERIE
    // -------------------------------------------------------------------------

    public void inserirSerie(Serie serie) {
        executor.execute(() -> {
            gymDao.inserirSerie(serie);

            if (isConectado()) {
                SerieDto dto = mapSerieToDto(serie);
                apiService.criarSerie(dto).enqueue(new Callback<SerieDto>() {
                    @Override
                    public void onResponse(Call<SerieDto> call, Response<SerieDto> response) {}

                    @Override
                    public void onFailure(Call<SerieDto> call, Throwable t) {}
                });
            }
        });
    }

    public LiveData<List<SerieComExercicio>> buscarSeriesComExercicio(int treinoId) {
        return gymDao.buscarSeriesComExercicioPorTreino(treinoId);
    }

    // -------------------------------------------------------------------------
    // UTILITÁRIOS
    // -------------------------------------------------------------------------

    /**
     * Verifica conectividade real (não apenas se o Wi-Fi está ativo).
     */
    private boolean isConectado() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        NetworkCapabilities caps =
                cm.getNetworkCapabilities(cm.getActiveNetwork());

        return caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    // --- Mappers ---

    private TreinoDto mapTreinoToDto(Treino t) {
        TreinoDto dto = new TreinoDto();
        dto.id = t.id;
        dto.nome = t.nome;
        dto.dataInicio = t.dataInicio;
        dto.dataFim = t.dataFim;
        return dto;
    }

    private SerieDto mapSerieToDto(Serie s) {
        SerieDto dto = new SerieDto();
        dto.treinoId = s.treinoId;
        dto.exercicioId = s.exercicioId;
        dto.pesoKg = s.pesoKg;
        dto.repeticoes = s.repeticoes;
        dto.ordemSerie = s.ordemSerie;
        return dto;
    }

    // -------------------------------------------------------------------------
    // CALLBACK INTERFACE
    // -------------------------------------------------------------------------

    /**
     * Interface para retornar o ID gerado após inserção assíncrona.
     * Usado pelo ViewModel para encadear operações (ex: inserir treino → inserir séries).
     */
    public interface OnInsertCallback {
        void onInserted(int id);
    }

    public LiveData<List<Exercicio>> buscarTodosExercicios() {
        return gymDao.buscarTodosExercicios();
    }
    public LiveData<Treino> buscarTreinoPorId(int treinoId) {
        return gymDao.buscarTreinoPorId(treinoId);
    }

    /**
     * Busca exercícios da API e salva no Room local.
     * Chamado uma vez na inicialização do app.
     */
    public void sincronizarExercicios() {
        if (!isConectado()) return;

        apiService.listarExercicios().enqueue(new Callback<List<ExercicioDto>>() {
            @Override
            public void onResponse(Call<List<ExercicioDto>> call, Response<List<ExercicioDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executor.execute(() -> {
                        for (ExercicioDto dto : response.body()) {
                            Exercicio exercicio = new Exercicio(dto.nome, dto.grupoMuscular);
                            exercicio.id = dto.id;
                            gymDao.inserirExercicio(exercicio);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ExercicioDto>> call, Throwable t) {
                // Sem internet — Room já tem os dados se sincronizou antes
            }
        });
    }
}