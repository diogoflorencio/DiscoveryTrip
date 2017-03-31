package com.example.diogo.discoverytrip.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.Model.VisualizationType;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.DeleteEventoResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesPontosTuristicosActivity extends AppCompatActivity implements View.OnClickListener{
    public static Atracao pontoTuristico;
    public static VisualizationType visualizationType;

    private TextView titulo, descricao, endereco, categoria, latitude, longitude;
    private ImageButton editar, deletar;
    private ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pontos_turisticos);

//        loadImage(foto);

        titulo = (TextView) findViewById(R.id.detalhes_pt_titulo);
        descricao = (TextView) findViewById(R.id.detalhes_pt_descricao);
        endereco = (TextView) findViewById(R.id.detalhes_pt_endereco);
        categoria = (TextView) findViewById(R.id.detalhes_pt_categoria);
        latitude = (TextView) findViewById(R.id.detalhes_pt_latitude);
        longitude = (TextView) findViewById(R.id.detalhes_pt_longitude);
        foto = (ImageView) findViewById(R.id.detalhes_pt_imagem);

        editar = (ImageButton) findViewById(R.id.detalhes_pt_edit);
        deletar = (ImageButton) findViewById(R.id.detalhes_pt_delete);

        editar.setOnClickListener(this);
        deletar.setOnClickListener(this);

        titulo.setText(pontoTuristico.getName());
        descricao.setText(pontoTuristico.getDescription());
        latitude.setText(pontoTuristico.getLocation().getLatitude());
        longitude.setText(pontoTuristico.getLocation().getLongitude());

        if(pontoTuristico.getLocation().getStreetName() != null){
            endereco.setText("Rua "+pontoTuristico.getLocation().getStreetName()+
                    ", "+pontoTuristico.getLocation().getStreetNumber()+
                    ", "+pontoTuristico.getLocation().getCity());
        }

        if(visualizationType.equals(VisualizationType.Visualizar)){
            editar.setEnabled(false);
            deletar.setEnabled(false);
            editar.setVisibility(View.INVISIBLE);
            deletar.setVisibility(View.INVISIBLE);
        }
    }

    private void loadImage(final ImageView imgView){
        final Handler handler = new Handler();
        retrofit2.Call<ResponseBody> call = ApiClient.API_SERVICE.downloadFoto("bearer "+ AcessToken.recuperar(getSharedPreferences("acessToken", Context.MODE_PRIVATE)),
                pontoTuristico.getPhotos().get(0));
        call.enqueue(new retrofit2.Callback<ResponseBody>() {

            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    InputStream input = response.body().byteStream();
                    //Convert a foto em Bitmap
                    try {
                        final Bitmap img = BitmapFactory.decodeStream(input);
                        //Coloca a foto na imageView
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imgView.setImageBitmap(img);
                            }
                        });
                    } catch (Exception e){
                        Log.e("Logger",e.toString());
                    }
                } else {
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Logger","Carregando foto erro: "+error.getErrorDescription());
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        pontoTuristico = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detalhes_pt_edit:
                //TODO tela de edição e request de edição
                break;

            case R.id.detalhes_pt_delete:
                deleteEvent();
                break;
        }
    }

    private void deleteEvent(){
        final AlertDialog dialog = createLoadingDialog();
        dialog.show();

        String token = AcessToken.recuperar(this.getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<DeleteEventoResponse> call = ApiClient.API_SERVICE.deleteEvento("bearer "+token,pontoTuristico.getId());
        call.enqueue(new Callback<DeleteEventoResponse>() {
            @Override
            public void onResponse(Call<DeleteEventoResponse> call, Response<DeleteEventoResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Logger","deleteEventos ok");
                    Toast.makeText(DetalhesPontosTuristicosActivity.this,"Ponto turístico deletado com sucesso!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    onBackPressed();
                }else {
                    dialog.dismiss();
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Logger", "deleteEventos ServerResponse "+error.getErrorDescription());
                        Toast.makeText(DetalhesPontosTuristicosActivity.this,error.getErrorDescription(),Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteEventoResponse> call, Throwable t) {
                Log.e("Logger","deleteEventos error: "+ t.toString());
            }
        });
    }

    private AlertDialog createLoadingDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_aguarde,null);
        dialog.setView(dialogView);
        dialog.setTitle("Enviando");
        dialog.setCancelable(false);
        return dialog;
    }
}
