package com.example.wearnotifyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class WearActionReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("WEAR_ACTION".equals(intent.getAction())) {
            Toast.makeText(context, "Action on Wearable clicked", Toast.LENGTH_SHORT).show();
            Log.i("info", "Action on wear clicked");
        }
    }
}
