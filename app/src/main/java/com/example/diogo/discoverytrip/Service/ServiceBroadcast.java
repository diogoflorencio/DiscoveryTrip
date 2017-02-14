package com.example.diogo.discoverytrip.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by diogo on 04/02/17.
 */

public class ServiceBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent intentLembrete = new Intent(context, ServiceLembrete.class);
            context.startService(intentLembrete);
        }
    }
}
