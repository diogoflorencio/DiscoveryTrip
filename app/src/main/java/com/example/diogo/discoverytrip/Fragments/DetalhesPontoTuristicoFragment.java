package com.example.diogo.discoverytrip.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.diogo.discoverytrip.Util.ListAdapterPontosTuristicos;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetalhesPontoTuristicoFragment extends Fragment implements View.OnClickListener{

    public static Atracao pontoTuristico;
    public static VisualizationType visualizationType;

    private TextView titulo, descricao, endereco, categoria, latitude, longitude;
    private ImageButton deletar;
    private ImageView foto;
    
    public DetalhesPontoTuristicoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_detalhes_ponto_turistico, container, false);

        titulo = (TextView) view.findViewById(R.id.detalhes_pt_titulo);
        descricao = (TextView) view.findViewById(R.id.detalhes_pt_descricao);
        endereco = (TextView) view.findViewById(R.id.detalhes_pt_endereco);
        categoria = (TextView) view.findViewById(R.id.detalhes_pt_categoria);
        latitude = (TextView) view.findViewById(R.id.detalhes_pt_latitude);
        longitude = (TextView) view.findViewById(R.id.detalhes_pt_longitude);
        foto = (ImageView) view.findViewById(R.id.detalhes_pt_imagem);

        deletar = (ImageButton) view.findViewById(R.id.detalhes_pt_delete);

        deletar.setOnClickListener(this);

        titulo.setText(pontoTuristico.getName());
        descricao.setText(pontoTuristico.getDescription());
        latitude.setText(pontoTuristico.getLocation().getLatitude());
        longitude.setText(pontoTuristico.getLocation().getLongitude());
        categoria.setText(pontoTuristico.getCategory());

        if(pontoTuristico.getLocation().getStreetName() != null){
            endereco.setText("Rua "+pontoTuristico.getLocation().getStreetName()+
                    ", "+pontoTuristico.getLocation().getStreetNumber()+
                    ", "+pontoTuristico.getLocation().getCity());
        }

        if(visualizationType.equals(VisualizationType.Visualizar)){
            deletar.setEnabled(false);
            deletar.setVisibility(View.INVISIBLE);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ListAdapterPontosTuristicos.loadImage(foto,pontoTuristico.getPhotos().get(0),getContext());
            }
        }).start();

        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        pontoTuristico = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detalhes_pt_delete:
                confirmDelete();
                break;
        }
    }

    private void confirmDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmar");
        builder.setMessage("Tem certeza que deseja deletar esse evento?");
        builder.setCancelable(false);
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteEvent();
            }
        });
        builder.show();
    }

    private void deleteEvent(){
        final AlertDialog dialog = createLoadingDialog();
        dialog.show();

        String token = AcessToken.recuperar(getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<DeleteEventoResponse> call = ApiClient.API_SERVICE.deleteEvento("bearer "+token,pontoTuristico.getId());
        call.enqueue(new Callback<DeleteEventoResponse>() {
            @Override
            public void onResponse(Call<DeleteEventoResponse> call, Response<DeleteEventoResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Logger","deleteEventos ok");
                    Toast.makeText(getContext(),"Ponto turístico deletado com sucesso!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    dialog.dismiss();
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Logger", "deleteEventos ServerResponse "+error.getErrorDescription());
                        Toast.makeText(getContext(),error.getErrorDescription(),Toast.LENGTH_SHORT).show();
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
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_aguarde,null);
        dialog.setView(dialogView);
        dialog.setTitle("Enviando");
        dialog.setCancelable(false);
        return dialog;
    }
}
