package com.example.diogo.discoverytrip.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;
import static com.example.diogo.discoverytrip.Activities.HomeActivity.EVENT_TYPE;

/**
 * Created by renato on 07/02/17.
 */
public class ListAdapterPontosTuristicos extends ArrayAdapter<Atracao>{
    private List<View> views;
    private LayoutInflater inflater;
    private List<Atracao> atracoes;
    private Activity context;
    private SimpleDateFormat BDFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat nomalFormat = new SimpleDateFormat("dd/M/yyyy");
    private static Semaphore semaphore = new Semaphore(1);
    final static Handler handler = new Handler();

    public ListAdapterPontosTuristicos(Activity context, LayoutInflater inflater, List<Atracao> atracoes){
        super(context, R.layout.item_evento,atracoes);

        this.inflater = inflater;
        this.atracoes = atracoes;
        this.context = context;
        this.views = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(views.size() > position){
            return views.get(position);
        }

        Log.d("Logger","getView "+position);
        final Atracao atracao = atracoes.get(position);
        View view = inflater.inflate(R.layout.item_ponto_turistico, null, true);
        final ImageView foto = (ImageView) view.findViewById(R.id.iten_img);
        ImageView icone = (ImageView) view.findViewById(R.id.iten_icon);

        final TextView titulo  = (TextView) view.findViewById(R.id.iten_name);
        String photoId = null;

        titulo.setText(atracao.getName());

        if(atracao.getType().equals(EVENT_TYPE)){
            if(atracao.getPhotoId() != null){
                photoId = atracao.getPhotoId();
                final String finalPhotoId = photoId;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadImage(foto, finalPhotoId, context);
                    }
                }).start();
            }
        }
        else{
            icone.setImageResource(R.drawable.ponto_turistico_icon);
            photoId = atracao.getPhotos().get(0);
            final String finalPhotoId1 = photoId;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadImage(foto, finalPhotoId1, context);
                }
            }).start();
        }

        views.add(view);
        return view;
    }

    public static void loadImage(final ImageView imgView, final String photoId, final Context context){
        try{
            semaphore.acquire();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("Logger","loadImage");
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    imgView.setImageBitmap(loadImageFromInternalStorage(photoId,context));
                    Log.d("Looger","loadImage finish");
                    semaphore.release();

                } catch (FileNotFoundException e) {
                    retrofit2.Call<ResponseBody> call = ApiClient.API_SERVICE.downloadFoto("bearer "+AcessToken.recuperar(context.getSharedPreferences("acessToken", Context.MODE_PRIVATE)),
                            photoId);
                    call.enqueue(new retrofit2.Callback<ResponseBody>() {

                        @Override
                        public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                InputStream input = response.body().byteStream();
                                //Convert a foto em Bitmap
                                final Bitmap img = BitmapFactory.decodeStream(input);

                                saveImageToInternalStorage(photoId,img,context);

                                //Coloca a foto na imageView
                                try {
                                    imgView.setImageBitmap(loadImageFromInternalStorage(photoId,context));
                                } catch (FileNotFoundException e1) {
                                    Log.e("Logger","Erro ao carregar foto");
                                    e1.printStackTrace();
                                }

                            } else {
                                try {
                                    ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                                    Log.e("Pesquisa de pontos turisticos",error.getErrorDescription());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            semaphore.release();
                            Log.d("Logger","Load image finish");
                        }

                        @Override
                        public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("Pesquisa de pontos turisticos","Erro ao baixar imagem");
                            semaphore.release();
                        }
                    });
                }
            }
        });
    }

    private static void saveImageToInternalStorage(String imageName, Bitmap imagem, Context context){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        try{
            FileOutputStream fos = context.openFileOutput(imageName,MODE_PRIVATE);
            fos.write(byteArray);
            fos.close();
        }catch (IOException e) {
            Log.w("InternalStorage", "Error writing", e);
        }
    }

    private static Bitmap loadImageFromInternalStorage(String imageName, Context context) throws FileNotFoundException {

        FileInputStream fis = context.openFileInput(imageName);
        try {
            byte[] imageBytes = new byte[(int) fis.getChannel().size()];
            fis.read(imageBytes,0, (int) fis.getChannel().size());
            fis.close();
            Log.d("Logger","Image file "+imageBytes);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length,null);
            return imageBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Atracao getItem(int position){
        return atracoes.get(position);
    }
}