package com.example.eventqrcode.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConnectionFactory extends SQLiteOpenHelper {

    private static final String NAME ="qrCode.db";
    private static final int VERSION = 1;

    public ConnectionFactory(@Nullable Context context) {
        super(context, NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE aluno(ra integer primary key, "+
                "nome varchar(50), cpf varchar(50), telefone varchar(50))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS aluno";
        db.execSQL(sql);
        onCreate(db);
    }
}