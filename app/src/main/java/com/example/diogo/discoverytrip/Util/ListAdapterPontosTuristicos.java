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

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.ResponseBody;

import static com.example.diogo.discoverytrip.Activities.HomeActivity.EVENT_TYPE;

/**
 * Created by renato on 07/02/17.
 */
public class ListAdapterPontosTuristicos extends ArrayAdapter<Atracao>{
    private LayoutInflater inflater;
    private List<Atracao> atracoes;
    private Context context;
    private Handler handler = new Handler();
    private SimpleDateFormat BDFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat nomalFormat = new SimpleDateFormat("dd/M/yyyy");

    public ListAdapterPontosTuristicos(Context context, LayoutInflater inflater, List<Atracao> atracoes){
        super(context, R.layout.item_evento,atracoes);

        this.inflater = inflater;
        this.atracoes = atracoes;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Atracao atracao = atracoes.get(position);
        View view = inflater.inflate(R.layout.item_ponto_turistico, null, true);
        ImageView foto = (ImageView) view.findViewById(R.id.iten_img);
        ImageView icone = (ImageView) view.findViewById(R.id.iten_icon);

        final TextView titulo  = (TextView) view.findViewById(R.id.iten_name);

        titulo.setText(atracao.getName());

        if(atracao.getType().equals(EVENT_TYPE)){
            if(atracao.getPhotoId() != null){
                //loadImage(foto,position,true);
            }
        }
        else{
            icone.setImageResource(R.drawable.ponto_turistico_icon);
            //loadImage(foto,position,false);
        }
        return view;
    }

    private void loadImage(final ImageView imgView, final int position, boolean isEvent){

        String photoId;
        if(isEvent){
            photoId = atracoes.get(position).getPhotoId();
        }
        else{
            photoId = atracoes.get(position).getPhotos().get(0);
        }

        retrofit2.Call<ResponseBody> call = ApiClient.API_SERVICE.downloadFoto("bearer "+AcessToken.recuperar(context.getSharedPreferences("acessToken", Context.MODE_PRIVATE)),
                photoId);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {

            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
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
                } else {
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Pesquisa de pontos turisticos",error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                Log.e("Pesquisa de pontos turisticos","Erro ao baixar imagem");
            }
        });
    }

    @Override
    public Atracao getItem(int position){
        return atracoes.get(position);
    }
}
