package com.br.guilhermelp.franquiadesktop;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Guilherme on 20/11/2016.
 */

public class ExtratorService extends AsyncTask<String, Object, Franquia> {

    private static final String IMAGEM_CABECALHO = "imagens/sac_cabecalho.png";
    private static final String IMAGEM_FRANQUIA = "imagens/bitazul.png";
    private static final int IMAGEM_CONSUMIDO_DOWNLOAD = 3;
    private static final int IMAGEM_CONSUMIDO_UPLOAD = 4;
    private static final int IMAGEM_CONSUMIDO_TOTAL = 5;

    private static final String URL_FRANQUIA = "http://sac.desktop.com.br/Cliente_Quota.jsp";
    private static final String URL_INICIAL = "http://sac.desktop.com.br/Cliente_Menu.jsp";
    private static final String URL_LOGIN = "http://sac.desktop.com.br/Cliente_Login.jsp";

    @Override
    protected Franquia doInBackground(String... strings) {
        Franquia franquia = new Franquia();
        Document page = null;
        String usuario, senha;
        usuario = strings[0];
        senha = strings[1];

        try{
            Connection.Response loginForm = Jsoup.connect(URL_LOGIN)
                    .method(Connection.Method.GET)
                    .execute();

            Jsoup.connect(URL_INICIAL)
                    .data("acao", "entrar")
                    .data("num", usuario)
                    .data("senha", senha)
                    .cookies(loginForm.cookies())
                    .method(Connection.Method.POST)
                    .execute();

            page = Jsoup.connect(URL_FRANQUIA)
                    .cookies(loginForm.cookies())
                    .get();
        } catch(IOException e){
            e.printStackTrace();
        }

        Elements images = page.select("img[src~=(?i)\\.(png)]");
        int count = 0;
        for (Element image : images) {
            count++;
            if (!image.attr("src").equals(IMAGEM_CABECALHO)) {
                if (image.attr("src").equals(IMAGEM_FRANQUIA)) {
                    franquia.setFranquia(image.parentNode().childNode(3).childNode(0).toString().replaceAll(" Giga", ""));
                } else {
                    String valor = image.parentNode().childNode(2).childNode(0).toString().replaceAll(" Giga", "");
                    if (count == IMAGEM_CONSUMIDO_DOWNLOAD) {
                        franquia.setConsumoDownload(valor);
                    } else if (count == IMAGEM_CONSUMIDO_UPLOAD) {
                        franquia.setConsumoUpload(valor);
                    } else if (count == IMAGEM_CONSUMIDO_TOTAL) {
                        franquia.setConsumoTotal(valor);
                    }
                }
            }
        }
        return franquia;
    }
}
