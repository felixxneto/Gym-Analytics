package com.projeto.gymanalytics.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.projeto.gymanalytics.dao.GymDao;
import com.projeto.gymanalytics.model.Exercicio;
import com.projeto.gymanalytics.model.Serie;
import com.projeto.gymanalytics.model.Treino;

@Database(entities = {Exercicio.class, Treino.class, Serie.class}, version = 3, exportSchema = false)
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
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}