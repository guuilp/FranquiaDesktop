package com.br.guilhermelp.franquiadesktop;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.br.guilhermelp.franquiadesktop.model.Cliente;
import com.br.guilhermelp.franquiadesktop.model.FranquiaBD;
import com.br.guilhermelp.franquiadesktop.model.Item;
import com.br.guilhermelp.franquiadesktop.model.Login;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @BindView(R.id.activity_main) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lista) ListView listaDeCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        SugarContext.init(this);

        List<Item> items = null;

        Cliente cliente = SugarRecord.findById(Cliente.class, 1);
        String nome = null;

        if(isConnected()) {
            if (cliente.getNome() == null) {
                cliente = new Cliente();
                cliente.setId(1L);
                Login login = SugarRecord.findById(Login.class, 1);
                try {
                    nome = new ExtratorClienteService().execute(login.getUsuario(), login.getSenha()).get();
                    cliente.setNome(nome);
                    cliente.save();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                nome = cliente.getNome();
            }
        } else {
            nome = cliente.getNome();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(nome);
        actionBar.getSubtitle();

        try {
            items = getFranquia();
        } catch (IOException e) {
            e.printStackTrace();
        }


        AdapterFranquia adapter = new AdapterFranquia(items, this);

        listaDeCursos.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getFranquia();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("RESUME", "onResume()");
        try {
            getFranquia();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNomeCliente(){

        return "Guilherme Lima Pereira";
    }

    private List<Item> getFranquia() throws IOException {

        try {
            if (isConnected()){
                Login login = SugarRecord.findById(Login.class, 1);
                salvarInformacoesNoBanco(new ExtratorFranquiaService().execute(login.getUsuario(), login.getSenha()).get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        FranquiaBD franquiaBD = FranquiaBD.findById(FranquiaBD.class, 1);

        List<Item> items = new ArrayList<>();

        popularListaDeValoresFranquia(franquiaBD, items);

        if(Double.parseDouble(franquiaBD.getConsumoMaximoPermitidoAteODiaCorrente()) < Double.parseDouble(franquiaBD.getConsumidoDownload())){
            Snackbar.make(swipeRefreshLayout, "Você já consumiu mais do que o permitido", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(swipeRefreshLayout, "Tudo OK!", Snackbar.LENGTH_LONG).show();
        }

        swipeRefreshLayout.setRefreshing(false);

        return items;
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            return true;
        }
        return false;
    }

    private void popularListaDeValoresFranquia(FranquiaBD franquiaBD, List<Item> items) {
        if(franquiaBD != null){

            items.add(new Item("Total", franquiaBD.getFranquiaTotal() + " GB"));
            items.add(new Item("Diária", franquiaBD.getFranquiaDiaria() + " GB"));

            items.add(new Item("Consumo Download", franquiaBD.getConsumidoDownload() + " GB"));
            items.add(new Item("Consumo Upload", franquiaBD.getConsumidoUpload() + " GB"));

            //items.add(new Item("Total (Download + Upload)", franquiaBD.getConsumidoTotal() + " GB"));

//            items.add(new Item("Permitido até o dia corrente", franquiaBD.getConsumoMaximoPermitidoAteODiaCorrente() + " GB"));

            items.add(new Item("Disponível para consumo hoje", franquiaBD.getQuantoAindaPodeConsumirHoje() + " GB"));
            items.add(new Item("Disponível para consumo no mês", franquiaBD.getQuantoAindaPodeConsumirNesseMes() + " GB"));


        }
    }

    private void salvarInformacoesNoBanco(Franquia franquia) {
        Calendar c = Calendar.getInstance();
        int ultimoDiaDoMes = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int diaCorrente = c.get(Calendar.DAY_OF_MONTH);

        Double franquiaDiaria = Double.parseDouble(franquia.getFranquia()) / ultimoDiaDoMes;

        Double usoMaximoDaFranquiaAteODiaCorrente = franquiaDiaria * diaCorrente;

        Double quantoAindaPodeConsumirNoDia = usoMaximoDaFranquiaAteODiaCorrente - Double.parseDouble(franquia.getConsumoDownload());

        Double quantoAindaPodeConsumirNoMes = Double.parseDouble(franquia.getFranquia()) - Double.parseDouble(franquia.getConsumoDownload());

        FranquiaBD franquiaBD = FranquiaBD.findById(FranquiaBD.class, 1);

        if(franquiaBD == null){
            franquiaBD = new FranquiaBD();
            franquiaBD.setId(1L);
        }

        franquiaBD.setFranquiaTotal(franquia.getFranquia());
        franquiaBD.setFranquiaDiaria(String.format(Locale.US,"%.2f", franquiaDiaria));
        franquiaBD.setConsumoMaximoPermitidoAteODiaCorrente(String.format(Locale.US, "%.2f", usoMaximoDaFranquiaAteODiaCorrente));
        franquiaBD.setConsumidoDownload(franquia.getConsumoDownload());
        franquiaBD.setQuantoAindaPodeConsumirHoje(String.format(Locale.US, "%.2f", quantoAindaPodeConsumirNoDia));
        franquiaBD.setQuantoAindaPodeConsumirNesseMes(String.format(Locale.US, "%.2f", quantoAindaPodeConsumirNoMes));
        franquiaBD.setConsumidoUpload(franquia.getConsumoUpload());
        franquiaBD.setConsumidoTotal(franquia.getConsumoTotal());
        franquiaBD.save();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}