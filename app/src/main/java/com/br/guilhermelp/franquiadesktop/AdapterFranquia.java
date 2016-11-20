package com.br.guilhermelp.franquiadesktop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Guilherme on 20/11/2016.
 */

public class AdapterFranquia extends BaseAdapter{
    private final List<Item> items;
    private final Activity activity;

    public AdapterFranquia(List<Item> items, Activity activity){
        this.items = items;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.custom_adapter, parent, false);
        Item item = items.get(position);
        TextView valor = (TextView) view.findViewById(R.id.idValor);
        TextView nome = (TextView) view.findViewById(R.id.idNome);

        valor.setText(item.getValor());
        nome.setText(item.getNome());

        return view;
    }
}
