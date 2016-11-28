package com.br.guilhermelp.franquiadesktop;

import android.os.AsyncTask;

import com.br.guilhermelp.franquiadesktop.model.Cliente;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Guilherme on 27/11/2016.
 */

public class ExtratorClienteService extends AsyncTask<String, Object, String> {

    private static final String URL_DADOS_CLIENTE = "http://sac.desktop.com.br/Cliente_Dados.jsp";
    private static final String URL_INICIAL = "http://sac.desktop.com.br/Cliente_Menu.jsp";
    private static final String URL_LOGIN = "http://sac.desktop.com.br/Cliente_Login.jsp";

    @Override
    protected String doInBackground(String... strings) {
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

            page = Jsoup.connect(URL_DADOS_CLIENTE)
                    .cookies(loginForm.cookies())
                    .get();
        } catch(IOException e){
            e.printStackTrace();
        }

        Elements select2 = page.select("b");
        return select2.get(5).childNode(0).toString();
    }
}
