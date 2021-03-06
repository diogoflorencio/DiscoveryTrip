package com.example.diogo.discoverytrip.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.diogo.discoverytrip.Activities.HomeActivity;
import com.example.diogo.discoverytrip.Activities.NotificacaoActivity;
import com.example.diogo.discoverytrip.DataBase.AcessToken;
import com.example.diogo.discoverytrip.DataBase.DiscoveryTripBD;
import com.example.diogo.discoverytrip.DataHora.DataHoraSystem;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.SearchResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diogo on 04/02/17.
 * Service para disparar notificações sobre eventos a serem lembrados
 */

public class ServiceLembrete extends Service {
    private static boolean run = false;
    private DiscoveryTripBD discoveryTripBD;
    private String horaLembrete = "09:00:00";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("Logger", "onCreate ServiceLembrete");

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if(!isRun()) {
            run = true;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while (isRun()) {
                        try {
                            Thread.sleep(timeSleep());
                            verificaNotificacao();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            new Thread(runnable).start();
        }
        return START_STICKY;
    }

    private int timeSleep(){
        int timeSleep = calcMiliSeconds(horaLembrete) - calcMiliSeconds(DataHoraSystem.hora()) ;
        if(timeSleep <= 0) timeSleep = 24 * 3600000 + timeSleep;
        Log.d("Logger", "ServiceLembrete timeSleep: " + timeSleep);
        return timeSleep;
    }

    private int calcMiliSeconds(String hora){
        int horaMS = new Integer(hora.substring(0, 2)) * 3600000; //1 hora = 3.600.000 milisegundos
        int minutoMS = Integer.parseInt(hora.substring(3,5)) * 60000; //1 minuto = 60.000 milisegundos
        int segundoMS = Integer.parseInt(hora.substring(6,8)) * 1000; // 1 segundo = 1.000 milisegundos
        return horaMS + minutoMS + segundoMS;
    }

    @Override
    public void onDestroy() {
        Log.d("Logger", "onDestroy ServiceLembrete");
        super.onDestroy();
    }

    private void verificaNotificacao(){
        discoveryTripBD = new DiscoveryTripBD(getApplicationContext());
        List<Atracao> lembretes = discoveryTripBD.selectDayLembretesTable();
        if(lembretes.isEmpty() || !isRun()) return;
        getEventsOfDay();
        enviaNotificacao();
    }

    private void enviaNotificacao() {
        Intent notificationIntent = new Intent(this, NotificacaoActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle("Discovery Trip")
                .setContentText("Today's Events")
                .setContentIntent(pendingIntent).build();

        /*vibração da notificação*/
        notification.vibrate = new long[] { 100, 250, 100, 500 };

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.mipmap.icon, notification);

        /*som da notificação*/
        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this,som);
            toque.play();
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("Logger", "ServiceLembrete notificação");
    }

    public static boolean isRun(){
        return run;
    }

    private void getEventsOfDay(){
        String token = AcessToken.recuperar(getSharedPreferences("acessToken", Context.MODE_PRIVATE));
        Call<SearchResponse> call = ApiClient.API_SERVICE.eventsOfDay("bearer "+token);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if(response.isSuccessful()){
                    Log.d("Logger","EventsOfDay ok");
                    DiscoveryTripBD bd = new DiscoveryTripBD(getApplicationContext());
                    bd.updateBD(response.body().getAtracoes());
                }else {
                    try {
                        ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                        Log.e("Logger", "EventsOfDay ServerResponse "+error.getErrorDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.e("Logger","EventsOfDay error: "+t.toString());
            }
        });
    }
}
