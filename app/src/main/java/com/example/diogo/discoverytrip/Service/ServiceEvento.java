package com.example.diogo.discoverytrip.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.diogo.discoverytrip.Activities.HomeActivity;
import com.example.diogo.discoverytrip.DataBase.DiscoveryTripBD;
import com.example.diogo.discoverytrip.R;


/**
 * Created by diogo on 04/02/17.
 */

public class ServiceEvento extends Service {
    private static boolean run = false;
    private DiscoveryTripBD discoveryTripBD;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("Logger", "onCreate ServiceEvento");

        /*discoveryTripBD = new DiscoveryTripBD(getApplicationContext());
        discoveryTripBD.insertTableLembrete("nome","descrição", DataHoraSystem.data());

        Cursor cursor = discoveryTripBD.selectTableLembrete();
        cursor.moveToFirst();
        Log.d("Logger", "ServiceEvento DiscoveryTripBD " + cursor.getString(cursor.getColumnIndexOrThrow(TableLembretes.Column.COLUMN_Nome)));
        discoveryTripBD.deleteTableLembrete();
        */
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (run){

                }
            }
        };
        new Thread(runnable).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void notificacao(String nome, String descricao){

    }
}
