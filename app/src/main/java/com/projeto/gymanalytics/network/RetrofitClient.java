package com.projeto.gymanalytics.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton que fornece uma instância única do cliente Retrofit.
 * BASE_URL aponta para o emulador Android → localhost do seu PC.
 * Em produção, substituir pelo endereço real da API.
 */
public class RetrofitClient {

    // 10.0.2.2 é o alias do localhost no emulador Android
    private static final String BASE_URL = "http://10.0.2.2:8000/";

    private static ApiService INSTANCE;

    public static ApiService getApiService() {
        if (INSTANCE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            INSTANCE = retrofit.create(ApiService.class);
        }
        return INSTANCE;
    }
}