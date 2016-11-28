package com.br.guilhermelp.franquiadesktop;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.br.guilhermelp.franquiadesktop.model.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Guilherme on 20/11/2016.
 */

public class AdapterFranquia extends BaseAdapter{
    private Context context;
    private final List<Item> items;
    private final Activity activity;

    public AdapterFranquia(List<Item> items, Activity activity){
        this.items = items;
        this.activity = activity;
        this.context = activity.getApplicationContext();
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        Item item = items.get(position);

        if(view != null){
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.custom_adapter, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.nome.setText(item.getNome());
        holder.valor.setText(item.getValor());

        return view;
    }

    static class ViewHolder{
        @BindView(R.id.idValor) TextView valor;
        @BindView(R.id.idNome) TextView nome;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
