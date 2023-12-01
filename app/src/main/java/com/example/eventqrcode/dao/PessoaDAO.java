package com.example.eventqrcode.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.eventqrcode.model.Evento;
import com.example.eventqrcode.model.Pessoa;
import com.example.eventqrcode.util.ConnectionFactory;
import java.util.ArrayList;
import android.content.Context;

public class PessoaDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public PessoaDAO(Context context){
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    public long create(Pessoa pessoa){
        ContentValues values = new ContentValues();
        values.put("nome", pessoa.getNome());
        values.put("cpf", pessoa.getCpf());
        values.put("eventoID", pessoa.getEventoID());

        return(banco.insert("Pessoa", null, values));
    }

    public void delete(Integer id){
        String args[] = {id.toString()};
        banco.delete("Pessoa","id=?",args);
    }

    public Pessoa find(String cpf){

        String args[] = {cpf};
        Pessoa pessoa = new Pessoa();
        Cursor cursor = banco.query("Pessoa", new String[]{"id", "nome", "cpf", "eventoID", "horaEntrada", "horaSaida"},
                "cpf=?", args, null, null, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            pessoa.setId(cursor.getInt(0));
            pessoa.setNome((cursor.getString(1)));
            pessoa.setCpf((cursor.getString(2)));
            pessoa.setEventoID((cursor.getInt(3)));
            pessoa.setHoraEntrada((cursor.getInt(4)));
            pessoa.setHoraSaida((cursor.getInt(5)));
        }
        return pessoa;
    }

    public Pessoa find(Integer id){
        String args[] = {id.toString()};
        Pessoa pessoa = new Pessoa();

        Cursor cursor = banco.query("Pessoa", new String[]{"id", "nome", "cpf", "eventoID", "horaEntrada", "horaSaida"},
                "id=?", args, null, null, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            pessoa.setId(cursor.getInt(0));
            pessoa.setNome((cursor.getString(1)));
            pessoa.setCpf((cursor.getString(2)));
            pessoa.setEventoID((cursor.getInt(3)));
            pessoa.setHoraEntrada((cursor.getInt(4)));
            pessoa.setHoraSaida((cursor.getInt(5)));
        }
        return pessoa;
    }

    public ArrayList<Pessoa> getPessoasFromEvent(Integer eventID){
        ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
        String[] args = {eventID.toString()};

        Cursor cursor = banco.query("Pessoa", new String[]{"id", "nome", "cpf", "eventoID", "horaEntrada", "horaSaida"},
                "eventoID=?", args, null, null, null);

        while (cursor.moveToNext()) {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(cursor.getInt(0));
            pessoa.setNome((cursor.getString(1)));
            pessoa.setCpf((cursor.getString(2)));
            pessoa.setEventoID((cursor.getInt(3)));
            pessoa.setHoraEntrada((cursor.getInt(4)));
            pessoa.setHoraSaida((cursor.getInt(5)));

            pessoas.add(pessoa);
        }

        return pessoas;
    }

    public void update(Pessoa pessoa){
        ContentValues values = new ContentValues();
        values.put("nome", pessoa.getNome());
        values.put("cpf", pessoa.getCpf());
        values.put("eventoID", pessoa.getEventoID());
        values.put("horaSaida", pessoa.getHoraSaida());

        String[] args = {pessoa.getId().toString()};
        banco.update("Pessoa", values,"id=?",args);
    }
}
