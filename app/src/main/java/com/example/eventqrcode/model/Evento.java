package com.example.eventqrcode.model;

public class Evento {

    private Integer id;
    private String nome;
    private Integer maximoPessoas;
    private Integer pessoas;

    public Evento(){}
    public Evento(Integer id, String nome, Integer maximoPessoas, Integer pessoas) {
        this.id = id;
        this.nome = nome;
        this.maximoPessoas = maximoPessoas;
        this.pessoas = pessoas;
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

    public Integer getMaximoPessoas() {
        return maximoPessoas;
    }

    public void setMaximoPessoas(Integer maximoPessoas) {
        this.maximoPessoas = maximoPessoas;
    }

    public Integer getPessoas() {
        return pessoas;
    }

    public void setPessoas(Integer pessoas) {
        this.pessoas = pessoas;
    }
}
