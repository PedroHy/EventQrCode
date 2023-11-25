package com.example.eventqrcode.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConnectionFactory extends SQLiteOpenHelper {

    private static final String NAME = "qrCode.db";
    private static final int VERSION = 1;

    public ConnectionFactory(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Evento(" +
                "       id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "       nome VARCHAR(60) NOT NULL, " +
                "       capacidade INTEGER NOT NULL, " +
                "       pessoas INTEGER DEFAULT(0)" +
                "   )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Pessoa(" +
                "       id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "       nome VARCHAR(60) NOT NULL, " +
                "       cpf VARCHAR(60) NOT NULL, " +
                "       evento INTEGER NOT NULL, " +
                "       horaEntrada INTEGER DEFAULT(unixepoch()), " +
                "       horaSaida INTEGER, " +
                "       FOREIGN KEY(evento) REFERENCES Evento(id)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Evento");
        db.execSQL("DROP TABLE IF EXISTS Pessoa");
        onCreate(db);
    }
}