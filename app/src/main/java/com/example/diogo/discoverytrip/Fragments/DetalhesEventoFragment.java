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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.DataBase.DiscoveryTripBD;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.Model.VisualizationType;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.DeleteEventoResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.Util.ListAdapterPontosTuristicos;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetalhesEventoFragment extends Fragment implements View.OnClickListener{

    public static Atracao atracao;
    public static VisualizationType visualizationType;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
    private static SimpleDateFormat normalDateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm");

    private TextView titulo, descricao, endereco, tipo, inicio, fim, preco;
    private Button lembrar;
    private ImageButton deletar;
    private ImageView foto;
    
    public DetalhesEventoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalhes_evento, container, false);

        titulo = (TextView) view.findViewById(R.id.detalhe_evento_titulo);
        descricao = (TextView) view.findViewById(R.id.detalhe_evento_descricao);
        endereco = (TextView) view.findViewById(R.id.detalhe_evento_endereco);
        tipo = (TextView) view.findViewById(R.id.detalhe_evento_categoria);
        inicio = (TextView) view.findViewById(R.id.detalhe_evento_inicio);
        fim = (TextView) view.findViewById(R.id.detalhe_evento_fim);
        preco = (TextView) view.findViewById(R.id.detalhe_evento_preco);
        foto = (ImageView) view.findViewById(R.id.detalhe_evento_img);

        lembrar = (Button) view.findViewById(R.id.detalhe_evento_marcar);
        deletar = (ImageButton) view.findViewById(R.id.detalhe_evento_deletar);

        lembrar.setOnClickListener(this);
        deletar.setOnClickListener(this);

        titulo.setText(atracao.getName());
        descricao.setText(atracao.getDescription());
        tipo.setText(atracao.getKind());
        try {
            inicio.setText(normalDateFormat.format(dateFormat.parse(atracao.getStartDate())));
            fim.setText(normalDateFormat.format(dateFormat.parse(atracao.getEndDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(atracao.getPrice() != null && !atracao.getPrice().equals("0")){
            preco.setText("R$ "+atracao.getPrice());
        }
        else{
            preco.setText("Gratuito");
        }

        if(atracao.getPhotoId() != null && !atracao.getPhotoId().equals("")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ListAdapterPontosTuristicos.loadImage(foto,atracao.getPhotoId(),getContext());
                }
            }).start();
        }

        if(atracao.getLocation() != null && atracao.getLocation().getStreetName() != null){
            endereco.setText(atracao.getLocation().getStreetName()+
                    ", "+atracao.getLocation().getStreetNumber()+
                    ", "+atracao.getLocation().getCity());
        }

        if(visualizationType.equals(VisualizationType.Lembrar_Evento)){
            deletar.setEnabled(false);
            deletar.setVisibility(View.INVISIBLE);
        }
        else if(visualizationType.equals(VisualizationType.Editar)){
            lembrar.setEnabled(false);
            lembrar.setVisibility(View.INVISIBLE);
        }
        else if(visualizationType.equals(VisualizationType.Visualizar)){
            deletar.setEnabled(false);
            deletar.setVisibility(View.INVISIBLE);
            lembrar.setEnabled(false);
            lembrar.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.detalhe_evento_marcar:
                postInterestedEvent();
                lembrarEvento();
                Toast.makeText(getContext(),"Evento adicionado a sua lista de lembretes",Toast.LENGTH_SHORT).show();
                
                break;
            case R.id.detalhe_evento_deletar:
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
                deleteEventBD();
            }
        });
        builder.show();
    }

    private void deleteEvent(){
        final AlertDialog dialog = createLoadingDialog();
        dialog.show();

        String token = AcessToken.recuperar(getActivity().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<DeleteEventoResponse> call = ApiClient.API_SERVICE.deleteEvento("bearer "+token,atracao.getId());
        call.enqueue(new Callback<DeleteEventoResponse>() {
            @Override
            public void onResponse(Call<DeleteEventoResponse> call, Response<DeleteEventoResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Logger","deleteEventos ok");
                    Toast.makeText(getContext(),"Evento deletado com sucesso!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    getActivity().onBackPressed();
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

    private void lembrarEvento(){
        DiscoveryTripBD bd = new DiscoveryTripBD(getContext());
        bd.insertLembretesTable(atracao);
    }

    private void postInterestedEvent(){
        String token = AcessToken.recuperar(this.getContext().getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<ResponseBody> call = ApiClient.API_SERVICE.interestedEvent("bearer "+token, atracao.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                    Log.d("Logger","InterestedEvent ok");
                else
                    Log.d("Logger","InterestedEvent error:" + response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Logger","InterestedEvent failure");
            }
        });
    }

    private void deleteEventBD(){
        DiscoveryTripBD bd = new DiscoveryTripBD(getActivity());
        bd.deleteLembreteTable(atracao);
    }
}