package com.example.eventqrcode.controller;

import android.content.Context;

import com.example.eventqrcode.dao.PessoaDAO;
import com.example.eventqrcode.model.Evento;
import com.example.eventqrcode.model.Pessoa;

import com.example.eventqrcode.dao.EventoDAO;

import java.util.ArrayList;

public class EventController {

    public void criarEvento(Context context, String nome, Integer maximoPessoas) {
        Evento e = new Evento(null, nome, maximoPessoas, null);

        EventoDAO dao = new EventoDAO(context);

        dao.create(e);
    }

    public Evento pegarEvento(Context context, Integer id) {
        EventoDAO dao = new EventoDAO(context);

        return dao.find(id);
    }

    public ArrayList<Evento> listarEventos(Context context) {
        EventoDAO dao = new EventoDAO(context);

        return dao.findAll();
    }

    public boolean registrarEntrada(Context context, String nome, String cpf, Integer idEvento) {
        EventoDAO eventoDAO = new EventoDAO(context);
        Evento e = eventoDAO.find(idEvento);

        if(e.getPessoas() == e.getMaximoPessoas()){
            // Atingiu o maximo de pessoas
            return false;
        }

        Pessoa p = new Pessoa(null, nome, cpf, idEvento, null, null);
        PessoaDAO pessoaDAO = new PessoaDAO(context);
        pessoaDAO.create(p);


        e.setPessoas(e.getPessoas() + 1);
        eventoDAO.update(e);
        return true;
    }

    public void gerarQrCode() {

    }

    public void registrarSaida(Context context, Integer idPessoa) {
        PessoaDAO pessoaDAO = new PessoaDAO(context);
        Pessoa p = pessoaDAO.find(idPessoa);

        long unix = System.currentTimeMillis() / 1000L;
        p.setHoraSaida(Integer.parseInt(unix + ""));

        pessoaDAO.delete(idPessoa);
    }

    public void finalizarEvento(Context context, Integer idEvento) {
        EventoDAO eventoDAO = new EventoDAO(context);

        eventoDAO.delete(idEvento);
    }

    public ArrayList<Pessoa> listarPessoas(Context context, Integer idEvento) {
        PessoaDAO pessoaDAO = new PessoaDAO(context);
        return pessoaDAO.getPessoasFromEvent(idEvento);
    }
}
