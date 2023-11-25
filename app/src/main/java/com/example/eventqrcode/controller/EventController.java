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

    public void registrarEntrada(Context context, String nome, String cpf, Integer idEvento) {
        // Fazer erro caso o maximo tenha sido atingido
        // Cria Pessoa no banco de dados
        Pessoa p = new Pessoa(null, nome, cpf, idEvento, null, null);
        PessoaDAO pessoaDAO = new PessoaDAO(context);
        pessoaDAO.create(p);

        // Aumenta em 1 as pessoas do evento
        EventoDAO eventoDAO = new EventoDAO(context);
        Evento e = eventoDAO.find(idEvento);
        e.setPessoas(e.getPessoas() + 1);
        eventoDAO.update(e);
    }

    public void gerarQrCode() {

    }

    public void registrarSaida() {

    }

    public void finalizarEvento() {

    }

    public ArrayList<Pessoa> listarPessoas() {

        return new ArrayList<Pessoa>();
    }

    public Pessoa criarPessoa() {

        return new Pessoa();
    }
}
