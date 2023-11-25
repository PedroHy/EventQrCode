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
        values.put("id", evento.getId());
        values.put("nome", evento.getNome());
        values.put("maximoPessoas", evento.getMaximoPessoas());
        values.put("pessoas", evento.getPessoas());
        return(banco.insert("evento", null, values));
    }

    public void finish(Integer eventoID){
        String args[] = {eventoID.toString()};
        banco.delete("evento","id=?",args);
    }

    public Evento find(Integer eventoID){
        String args[] = {String.valueOf(eventoID)};
        Evento evento = new Evento();

        Cursor cursor = banco.query("evento", new String[]{"id", "nome", "maximoPessoas", "pessoas"},
                "id=?", args, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            evento.setId(cursor.getInt(0));
            evento.setNome((cursor.getString(1)));
            evento.setMaximoPessoas((cursor.getInt(2)));
            evento.setPessoas((cursor.getInt(3)));
        }
        return new Evento();
    }

    public ArrayList<Evento> findAll(Integer eventoID){
        ArrayList<Evento> eventos = new ArrayList<Evento>();

        Cursor cursor = banco.query("evento", new String[]{"id", "nome", "maximoPessoas", "pessoas"},
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

    private void updatePessoa(Evento evento){
        ContentValues values = new ContentValues();
        values.put("id", evento.getId());
        values.put("nome", evento.getNome());
        values.put("maximoPessoas", evento.getMaximoPessoas());
        values.put("pessoas", evento.getPessoas());

        String args[] = {evento.getId().toString()};
        banco.update("evento", values,"id=?",args);
    }
}
