package com.example.wearnotifyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.wearnotifyapp.databinding.ActivityNotificationdetailsBinding;

public class NotificationdetailsActivity extends AppCompatActivity {

    ActivityNotificationdetailsBinding detailsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notificationdetails);
        detailsBinding = ActivityNotificationdetailsBinding.inflate(getLayoutInflater());
        View view = detailsBinding.getRoot();
        setContentView(view);
        init();
    }

    private void init(){                //retrieving Notification details from Pending intent
        Intent notificationIntent = getIntent();
        String attendees = notificationIntent.getStringExtra("Attendees");
        String locatiom = notificationIntent.getStringExtra("Location");
        String agenda = notificationIntent.getStringExtra("Agenda");

        detailsBinding.txtAttendees.setText(attendees);
        detailsBinding.txtLocation.setText(locatiom);
        detailsBinding.txtAgenda.setText(agenda);
    }


}