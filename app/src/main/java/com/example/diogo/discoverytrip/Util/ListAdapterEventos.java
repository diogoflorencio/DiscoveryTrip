package com.example.diogo.discoverytrip.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diogo.discoverytrip.Model.Evento;
import com.example.diogo.discoverytrip.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by renato on 07/02/17.
 */
public class ListAdapterEventos extends ArrayAdapter<Evento>{
    private LayoutInflater inflater;
    private List<Evento> eventos;
    private Handler handler = new Handler();


    public ListAdapterEventos(Context context, LayoutInflater inflater, List<Evento> eventos){
        super(context, R.layout.item_evento,eventos);

        this.inflater = inflater;
        this.eventos = eventos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(R.layout.item_evento, null, true);
        ImageView foto = (ImageView) view.findViewById(R.id.item_evento_img);

        TextView titulo  = (TextView) view.findViewById(R.id.item_evento_txtTitulo);
        TextView descricao  = (TextView) view.findViewById(R.id.item_evento_txtDescricao);
        TextView data  = (TextView) view.findViewById(R.id.item_evento_txtData);

        titulo.setText(eventos.get(position).getName());
        descricao.setText(eventos.get(position).getDescription());
        data.setText(eventos.get(position).getEndDate());
        loadImage(foto,position);
        return view;
    }

    private void loadImage(final ImageView imgView, final int position){
        new Thread(){
            public void run(){
                try {
                    //Estabeloce conexão com o servidor da foto
                    URL url = new URL(eventos.get(position).getPhoto());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    //Baixa a foto
                    InputStream input = connection.getInputStream();

                    //Convert a foto em Bitmap
                    final Bitmap img = BitmapFactory.decodeStream(input);

                    //Coloca a foto na imageView
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imgView.setImageBitmap(img);
                        }
                    });
                }catch (IOException e){
                    Log.e("Events","Download image failed");
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
