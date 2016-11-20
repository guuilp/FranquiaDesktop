package com.br.guilhermelp.franquiadesktop;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SugarContext.init(this);

        ListView listaDeCursos = (ListView) findViewById(R.id.lista);
        linearLayout = (LinearLayout) findViewById(R.id.activity_main);

        List<Item> items = getFranquia();

        AdapterFranquia adapter = new AdapterFranquia(items, this);

        listaDeCursos.setAdapter(adapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("RESUME", "onResume()");
        getFranquia();
    }

    private List<Item> getFranquia() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.31.185:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FranquiaAPI service = retrofit.create(FranquiaAPI.class);
        Call<Franquia> call = service.getFranquia();
        call.enqueue(new Callback<Franquia>() {
            @Override
            public void onResponse(Call<Franquia> call, Response<Franquia> response) {
                try {
                    Log.d("TESTE", "onResponse()");

                    salvarInformacoesNoBanco(response);

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Franquia> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

        FranquiaBD franquiaBD = FranquiaBD.findById(FranquiaBD.class, 1);

        List<Item> items = new ArrayList<>();

        popularListaDeValoresFranquia(franquiaBD, items);

        if(Double.parseDouble(franquiaBD.getConsumoMaximoPermitidoAteODiaCorrente()) < Double.parseDouble(franquiaBD.getConsumidoDownload())){
            Snackbar.make(linearLayout, "Você já consumiu mais do que o permitido", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(linearLayout, "Tudo OK!", Snackbar.LENGTH_LONG).show();
        }
        return items;
    }

    private void popularListaDeValoresFranquia(FranquiaBD franquiaBD, List<Item> items) {
        if(franquiaBD != null){

            items.add(new Item("Franquia Total", franquiaBD.getFranquiaTotal() + " GB"));
            items.add(new Item("Franquia Diária", franquiaBD.getFranquiaDiaria() + " GB"));
            items.add(new Item("Consumo máximo permitido até o dia corrente", franquiaBD.getConsumoMaximoPermitidoAteODiaCorrente() + " GB"));
            items.add(new Item("Consumido Download", franquiaBD.getConsumidoDownload() + " GB"));
            items.add(new Item("Quanto ainda pode consumir hoje", franquiaBD.getQuantoAindaPodeConsumirHoje() + " GB"));
            items.add(new Item("Quanto ainda pode consumir nesse mês", franquiaBD.getQuantoAindaPodeConsumirNesseMes() + " GB"));
            items.add(new Item("Consumido Upload", franquiaBD.getConsumidoUpload() + " GB"));
            items.add(new Item("Consumido Total", franquiaBD.getConsumidoTotal() + " GB"));


        }
    }

    private void salvarInformacoesNoBanco(Response<Franquia> response) {
        Calendar c = Calendar.getInstance();
        int ultimoDiaDoMes = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int diaCorrente = c.get(Calendar.DAY_OF_MONTH);

        Double franquiaDiaria = Double.parseDouble(response.body().getFranquia()) / ultimoDiaDoMes;

        Double usoMaximoDaFranquiaAteODiaCorrente = franquiaDiaria * diaCorrente;

        Double quantoAindaPodeConsumirNoDia = usoMaximoDaFranquiaAteODiaCorrente - Double.parseDouble(response.body().getConsumoDownload());

        Double quantoAindaPodeConsumirNoMes = Double.parseDouble(response.body().getFranquia()) - Double.parseDouble(response.body().getConsumoDownload());

        FranquiaBD franquiaBD = FranquiaBD.findById(FranquiaBD.class, 1);

        if(franquiaBD == null){
            franquiaBD = new FranquiaBD();
            franquiaBD.setId(1L);
        }

        franquiaBD.setFranquiaTotal(response.body().getFranquia());
        franquiaBD.setFranquiaDiaria(String.format(Locale.US,"%.2f", franquiaDiaria));
        franquiaBD.setConsumoMaximoPermitidoAteODiaCorrente(String.format(Locale.US, "%.2f", usoMaximoDaFranquiaAteODiaCorrente));
        franquiaBD.setConsumidoDownload(response.body().getConsumoDownload());
        franquiaBD.setQuantoAindaPodeConsumirHoje(String.format(Locale.US, "%.2f", quantoAindaPodeConsumirNoDia));
        franquiaBD.setQuantoAindaPodeConsumirNesseMes(String.format(Locale.US, "%.2f", quantoAindaPodeConsumirNoMes));
        franquiaBD.setConsumidoUpload(response.body().getConsumoUpload());
        franquiaBD.setConsumidoTotal(response.body().getConsumoTotal());
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