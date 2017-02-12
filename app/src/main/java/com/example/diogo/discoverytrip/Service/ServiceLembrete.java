package com.example.diogo.discoverytrip.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.diogo.discoverytrip.Activities.HomeActivity;
import com.example.diogo.discoverytrip.DataBase.DiscoveryTripBD;
import com.example.diogo.discoverytrip.DataHora.DataHoraSystem;
import com.example.diogo.discoverytrip.R;

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
        discoveryTripBD = new DiscoveryTripBD(getApplicationContext());
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
        super.onDestroy();
    }

    private void verificaNotificacao(){
        Cursor cursor = discoveryTripBD.selectLembretesTable();
        if(cursor.getCount() == 0 || !isRun()) return;
        enviaNotificacao();
    }

    private void enviaNotificacao() {
        /*Obs. falta inserir activity que trata dos lembretes no notificationIntent
        * falta redenrizar icon para notificação.
        * */
        Intent notificationIntent = new Intent(this, HomeActivity.class);

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

        /*som da notificação
        * Obs. definir toque da notificação*/
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
}
