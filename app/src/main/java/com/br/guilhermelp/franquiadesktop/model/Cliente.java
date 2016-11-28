package com.br.guilhermelp.franquiadesktop.model;

import com.orm.SugarRecord;

/**
 * Created by Guilherme on 27/11/2016.
 */

public class Cliente extends SugarRecord {
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
