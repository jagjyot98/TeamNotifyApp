package com.example.teamnotifyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MobileActionReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("MOBILE_ACTION".equals(intent.getAction())) {
            Toast.makeText(context, "Action on Mobile clicked", Toast.LENGTH_SHORT).show();
            Log.i("info", "Action on Mobile Clicked");
        }
    }
}
