package com.example.eventqrcode.model;

public class Pessoa {
    private Integer id;
    private String nome;
    private String cpf;
    private Integer eventoID;
    private Integer horaEntrada;
    private Integer horaSaida;

    public Pessoa(){}
    public Pessoa(Integer id, String nome, String cpf, Integer eventoID, Integer horaEntrada, Integer horaSaida) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.eventoID = eventoID;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getEventoID() {
        return eventoID;
    }

    public void setEventoID(Integer eventoID) {
        this.eventoID = eventoID;
    }

    public Integer getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Integer horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Integer getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(Integer horaSaida) {
        this.horaSaida = horaSaida;
    }

    @Override
    public String toString() {
        return nome;
    }
}
