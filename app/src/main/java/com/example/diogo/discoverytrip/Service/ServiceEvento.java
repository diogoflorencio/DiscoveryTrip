package com.example.diogo.discoverytrip.Service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.diogo.discoverytrip.DataBase.DiscoveryTripBD;
import com.example.diogo.discoverytrip.DataBase.TableLembretes;
import com.example.diogo.discoverytrip.DataHora.DataHoraSystem;

/**
 * Created by diogo on 04/02/17.
 * Service para disparar notificações sobre eventos a serem lembrados
 */

public class ServiceEvento extends Service {
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
        Log.d("Logger", "onCreate ServiceEvento");
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
                            notificacao();
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
        Log.d("Logger", "ServiceEvento timeSleep: " + timeSleep);
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

    private void notificacao(){
        Cursor cursor = discoveryTripBD.selectTableLembrete();
        if(cursor.getCount() == 0 || !isRun())
            return;//não notifica em caso de usuário deslogado ou não haver lembretes para o dia
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(TableLembretes.Column.COLUMN_Nome));
            String descricao = cursor.getString(cursor.getColumnIndexOrThrow(TableLembretes.Column.COLUMN_Descricao));
            //envia notificação
            cursor.moveToNext();
        }
        discoveryTripBD.deleteTableLembrete();
    }

    public static boolean isRun(){
        return run;
    }
}
