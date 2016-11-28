package com.br.guilhermelp.franquiadesktop.model;

/**
 * Created by Guilherme on 20/11/2016.
 */

public class Item {

    private String nome;
    private String valor;

    public Item(String nome, String valor){
        this.nome = nome;
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return valor + "\n" +
                nome;
    }
}
