package com.example.guilherme.franquiadesktop;

import com.orm.SugarRecord;

/**
 * Created by Guilherme on 26/10/2016.
 */

public class FranquiaBD extends SugarRecord {
    private String franquiaTotal;
    private String franquiaDiaria;
    private String consumoMaximoPermitidoAteODiaCorrente;
    private String consumidoDownload;
    private String quantoAindaPodeConsumirHoje;
    private String quantoAindaPodeConsumirNesseMes;
    private String consumidoUpload;
    private String consumidoTotal;

    public String getFranquiaTotal() {
        return franquiaTotal;
    }

    public void setFranquiaTotal(String franquiaTotal) {
        this.franquiaTotal = franquiaTotal;
    }

    public String getConsumidoDownload() {
        return consumidoDownload;
    }

    public void setConsumidoDownload(String consumidoDownload) {
        this.consumidoDownload = consumidoDownload;
    }

    public String getConsumidoUpload() {
        return consumidoUpload;
    }

    public void setConsumidoUpload(String consumidoUpload) {
        this.consumidoUpload = consumidoUpload;
    }

    public String getConsumidoTotal() {
        return consumidoTotal;
    }

    public void setConsumidoTotal(String consumidoTotal) {
        this.consumidoTotal = consumidoTotal;
    }

    public String getFranquiaDiaria() {
        return franquiaDiaria;
    }

    public void setFranquiaDiaria(String franquiaDiaria) {
        this.franquiaDiaria = franquiaDiaria;
    }

    public String getConsumoMaximoPermitidoAteODiaCorrente() {
        return consumoMaximoPermitidoAteODiaCorrente;
    }

    public void setConsumoMaximoPermitidoAteODiaCorrente(String consumoMaximoPermitidoAteODiaCorrente) {
        this.consumoMaximoPermitidoAteODiaCorrente = consumoMaximoPermitidoAteODiaCorrente;
    }

    public String getQuantoAindaPodeConsumirHoje() {
        return quantoAindaPodeConsumirHoje;
    }

    public void setQuantoAindaPodeConsumirHoje(String quantoAindaPodeConsumirHoje) {
        this.quantoAindaPodeConsumirHoje = quantoAindaPodeConsumirHoje;
    }

    public String getQuantoAindaPodeConsumirNesseMes() {
        return quantoAindaPodeConsumirNesseMes;
    }

    public void setQuantoAindaPodeConsumirNesseMes(String quantoAindaPodeConsumirNesseMes) {
        this.quantoAindaPodeConsumirNesseMes = quantoAindaPodeConsumirNesseMes;
    }
}
