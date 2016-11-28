package com.br.guilhermelp.franquiadesktop.model;

import com.orm.SugarRecord;

/**
 * Created by Guilherme on 22/11/2016.
 */

public class Login extends SugarRecord {
    private String usuario;
    private String senha;
    private String nome;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
