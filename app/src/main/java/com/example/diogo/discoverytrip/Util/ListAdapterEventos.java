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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.DataBase.DiscoveryTripBD;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by renato on 07/02/17.
 */
public class ListAdapterEventos extends ArrayAdapter<Atracao>{
    private LayoutInflater inflater;
    private List<Atracao> eventos;
    private Context context;
    private Handler handler = new Handler();
    private SimpleDateFormat BDFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat nomalFormat = new SimpleDateFormat("dd/M/yyyy");

    public ListAdapterEventos(Context context, LayoutInflater inflater, List<Atracao> pontosTuristicos){
        super(context, R.layout.item_evento,pontosTuristicos);

        this.inflater = inflater;
        this.eventos = pontosTuristicos;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Atracao atracao = eventos.get(position);
        View view = inflater.inflate(R.layout.item_ponto_turistico, null, true);
        ImageView foto = (ImageView) view.findViewById(R.id.item_pontoTuristico_img);
        ImageButton button = (ImageButton) view.findViewById(R.id.item_pontoTuristico_btn_participate);
        button.setVisibility(View.INVISIBLE);
        final TextView titulo  = (TextView) view.findViewById(R.id.item_pontoTuristico_txtTitulo);
        final TextView descricao  = (TextView) view.findViewById(R.id.item_pontoTuristico_txtDescricao);
        TextView cidade = (TextView) view.findViewById(R.id.item_pontoTuristico_cidade);

        titulo.setText(atracao.getName());
        descricao.setText(atracao.getDescription());
        cidade.setText(atracao.getLocation().getCity() +", "+
                atracao.getLocation().getStreetName());

            TextView data = (TextView) view.findViewById(R.id.item_pontoTuristico_data);
            if(atracao.getStartDate() != null) {
                try {
                    data.setText(nomalFormat.format(BDFormat.parse(atracao.getStartDate())) + " à " +
                            nomalFormat.format(BDFormat.parse(atracao.getEndDate())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    data.setText(nomalFormat.format(BDFormat.parse(atracao.getEndDate())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(atracao.getPhotoId() != null) {
                loadImage(foto, position, true);
            }
        return view;
    }

    private void loadImage(final ImageView imgView, final int position, boolean isEvent){

        String photoId;
        if(isEvent){
            photoId = eventos.get(position).getPhotoId();
        }
        else{
            photoId = eventos.get(position).getPhotos().get(0);
        }

        Log.d("Logger", eventos.get(position).getPhotos().get(0));
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
                Log.e("Logger","Erro ao baixar imagem");
            }
        });
    }
}
