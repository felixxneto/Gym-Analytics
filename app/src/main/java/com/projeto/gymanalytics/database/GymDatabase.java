package com.projeto.gymanalytics.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.projeto.gymanalytics.dao.GymDao;
import com.projeto.gymanalytics.model.Exercicio;
import com.projeto.gymanalytics.model.Serie;
import com.projeto.gymanalytics.model.Treino;

/**
 * Ponto central de acesso ao banco de dados local.
 *
 * Singleton thread-safe: uma única instância por processo,
 * criada com double-checked locking para evitar condições de corrida.
 *
 * version: incremente este número + forneça uma Migration sempre que
 * alterar o schema. Nunca use fallbackToDestructiveMigration() em produção.
 */
@Database(
        entities = { Exercicio.class, Treino.class, Serie.class },
        version = 2,  // ← era 1
        exportSchema = true
)
public abstract class GymDatabase extends RoomDatabase {

    public abstract GymDao gymDao();

    private static volatile GymDatabase INSTANCE;

    public static GymDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GymDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    GymDatabase.class,
                                    "gym_analytics.db"
                            )
                            .fallbackToDestructiveMigration() // ← apaga e recria o banco
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}