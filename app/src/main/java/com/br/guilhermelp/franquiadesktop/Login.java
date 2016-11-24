package com.br.guilhermelp.franquiadesktop;

import com.orm.SugarRecord;

/**
 * Created by Guilherme on 22/11/2016.
 */

public class Login extends SugarRecord {
    private String usuario;
    private String senha;

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
}
