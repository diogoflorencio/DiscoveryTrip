package com.example.diogo.discoverytrip.Service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.diogo.discoverytrip.DataHora.DataHoraSystem;
import com.example.diogo.discoverytrip.DiscoveryTripBD.DiscoveryTripBD;
import com.example.diogo.discoverytrip.DiscoveryTripBD.TableLembretes;

import org.w3c.dom.Text;


/**
 * Created by diogo on 04/02/17.
 */

public class ServiceEvento extends Service {

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
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
