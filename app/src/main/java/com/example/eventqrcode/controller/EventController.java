package com.example.eventqrcode.controller;

import com.example.eventqrcode.model.Evento;
import com.example.eventqrcode.model.Pessoa;

import java.util.ArrayList;

public class EventController {

    public void criarEvento(String nome, Interger maximoPessoas){
        Evento e = newEvento(null, nome, maximoPessoas, null);
    }

    public void pegarEvento(String eventID){

    }

    public ArrayList<Evento> listarEventos(){

        return new ArrayList<Evento>();
    }

    public void registrarEntrada(Integer pessoaId, Integer eventID){

    }

    public void gerarQrCode(){

    }

    public void registrarSaida(){

    }

    public void finalizarEvento(){

    }

    public ArrayList<Pessoa> listarPessoas(){

        return new ArrayList<Pessoa>();
    }

    public Pessoa criarPessoa(){

        return new Pessoa();
    }
}
