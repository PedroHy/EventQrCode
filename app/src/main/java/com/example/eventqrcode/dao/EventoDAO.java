package com.example.eventqrcode.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.eventqrcode.model.Evento;
import com.example.eventqrcode.util.ConnectionFactory;

import java.util.ArrayList;

public class EventoDAO {
    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public EventoDAO(Context context){
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    public long create(Evento evento){
        ContentValues values = new ContentValues();
        values.put("nome", evento.getNome());
        values.put("maximoPessoas", evento.getMaximoPessoas());

        return(banco.insert("Evento", null, values));
    }

    public void delete(Integer id){
        String[] args = {id.toString()};
        banco.delete("Evento","id=?", args);
    }

    public Evento find(Integer id){
        Evento evento = new Evento();
        String[] args = {id.toString()};

        Cursor cursor = banco.query("Evento", new String[]{"id", "nome", "maximoPessoas", "pessoas"},
                "id=?", args, null, null, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            evento.setId(cursor.getInt(0));
            evento.setNome((cursor.getString(1)));
            evento.setMaximoPessoas((cursor.getInt(2)));
            evento.setPessoas((cursor.getInt(3)));
        }
        return evento;
    }

    public ArrayList<Evento> findAll(){
        ArrayList<Evento> eventos = new ArrayList<Evento>();

        Cursor cursor = banco.query("Evento", new String[]{"id", "nome", "maximoPessoas", "pessoas"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Evento evento = new Evento();
            evento.setId(cursor.getInt(0));
            evento.setNome((cursor.getString(1)));
            evento.setMaximoPessoas((cursor.getInt(2)));
            evento.setPessoas((cursor.getInt(3)));
            eventos.add(evento);
        }

        return eventos;
    }

    public void update(Evento evento){
        ContentValues values = new ContentValues();
        values.put("nome", evento.getNome());
        values.put("maximoPessoas", evento.getMaximoPessoas());
        values.put("pessoas", evento.getPessoas());

        String args[] = {evento.getId().toString()};
        banco.update("Evento", values,"id=?",args);
    }
}
