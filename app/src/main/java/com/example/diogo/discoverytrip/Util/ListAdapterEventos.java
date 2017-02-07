package com.example.diogo.discoverytrip.Util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.diogo.discoverytrip.Model.Evento;
import com.example.diogo.discoverytrip.R;

import java.util.List;

/**
 * Created by renato on 07/02/17.
 */
public class ListAdapterEventos extends ArrayAdapter<Evento>{
    private LayoutInflater inflater;
    private List<Evento> eventos;

    public ListAdapterEventos(Context context, LayoutInflater inflater, List<Evento> eventos){
        super(context, R.layout.item_evento,eventos);

        this.inflater = inflater;
        this.eventos = eventos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(R.layout.item_evento, null, true);
        TextView titulo  = (TextView) view.findViewById(R.id.item_evento_txtTitulo);
        TextView descricao  = (TextView) view.findViewById(R.id.item_evento_txtDescricao);
        TextView data  = (TextView) view.findViewById(R.id.item_evento_txtData);

        titulo.setText(eventos.get(position).getName());
        descricao.setText(eventos.get(position).getDescription());
        data.setText(eventos.get(position).getEndDate());

        return view;
    }
}
