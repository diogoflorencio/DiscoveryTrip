package com.example.diogo.discoverytrip.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Model.PontoTuristico;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by renato on 07/02/17.
 */
public class ListAdapterPontosTuristicos extends ArrayAdapter<PontoTuristico>{
    private LayoutInflater inflater;
    private List<PontoTuristico> pontosTuristicos;
    private Context context;
    private Handler handler = new Handler();


    public ListAdapterPontosTuristicos(Context context, LayoutInflater inflater, List<PontoTuristico> pontosTuristicos){
        super(context, R.layout.item_evento,pontosTuristicos);

        this.inflater = inflater;
        this.pontosTuristicos = pontosTuristicos;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(R.layout.item_evento, null, true);
        ImageView foto = (ImageView) view.findViewById(R.id.item_evento_img);

        TextView titulo  = (TextView) view.findViewById(R.id.item_evento_txtTitulo);
        TextView descricao  = (TextView) view.findViewById(R.id.item_evento_txtDescricao);

        titulo.setText(pontosTuristicos.get(position).getName());
        descricao.setText(pontosTuristicos.get(position).getDescription());
        loadImage(foto,position);
        return view;
    }

    private void loadImage(final ImageView imgView, final int position){
        new Thread(){
            public void run(){
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .addHeader("Content-Type","application/json")
                        .addHeader("Authorization","bearer "+ AcessToken.recuperar(context.getSharedPreferences("acessToken", Context.MODE_PRIVATE)))
                        .url(ApiClient.BASE_URL + "api/photos/:"+pontosTuristicos.get(position).getPhotos().get(0)+"/download/")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream input = response.body().byteStream();

                        //Convert a foto em Bitmap
                        final Bitmap img = BitmapFactory.decodeStream(input);

                        //Coloca a foto na imageView
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imgView.setImageBitmap(img);
                            }
                        });
                    }
                });
            }
        }.start();
    }
}
