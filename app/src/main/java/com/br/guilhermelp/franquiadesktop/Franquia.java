package com.br.guilhermelp.franquiadesktop;

import java.util.HashMap;
import java.util.Map;

public class Franquia{

    private String franquia;
    private String consumoDownload;
    private String consumoUpload;
    private String consumoTotal;

    public String getFranquia() {
        return franquia;
    }

    public void setFranquia(String franquia) {
        this.franquia = franquia;
    }

    public String getConsumoDownload() {
        return consumoDownload;
    }

    public void setConsumoDownload(String consumoDownload) {
        this.consumoDownload = consumoDownload;
    }

    public String getConsumoUpload() {
        return consumoUpload;
    }

    public void setConsumoUpload(String consumoUpload) {
        this.consumoUpload = consumoUpload;
    }

    public String getConsumoTotal() {
        return consumoTotal;
    }

    public void setConsumoTotal(String consumoTotal) {
        this.consumoTotal = consumoTotal;
    }

}