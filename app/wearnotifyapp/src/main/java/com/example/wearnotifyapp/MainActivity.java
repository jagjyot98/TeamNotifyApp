package com.example.wearnotifyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;

import com.example.wearnotifyapp.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding mainBinding;
    private static final String CHANNEL_ID = "CHANNEL_01";

    private static final int SPEECH_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);
        init();
    }

    private void init() {
        createNotificationChannel();
        mainBinding.btnNotify.setOnClickListener(this);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.channel_name);
            String channelDescription = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == mainBinding.btnNotify.getId()) {
            setAgenda();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(results != null && results.size() > 0) {
                String spokenText = results.get(0);
                mainBinding.txtAgenda.setText(String.valueOf(spokenText));
                sendNotification(spokenText);
            }
        }
    }
    private void setAgenda(){
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //RecognizerIntent =  used for launching a speech recognition activity and receiving the transcribed speech as text
        //main purpose to interact with device's speech recognition engine
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        EXTRA_LANGUAGE_MODEL = specify the language model that the speech recognition engine should use.
//        influences how the system interprets and processes the spoken input
        startActivityForResult(voiceIntent, SPEECH_REQUEST_CODE);
    }

    private void sendNotification(String agendaText){
        long randomNumber = System.currentTimeMillis();
        int requestCode = (int) randomNumber;

        Intent intent = new Intent(this, NotificationdetailsActivity.class);
        intent.putExtra("Attendees","John Tosland, John Leiska");
        intent.putExtra("Location", "3G21");
        intent.putExtra("AGENDA",agendaText);

        //defining a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent, PendingIntent.FLAG_IMMUTABLE);
        //intent=target activity | requestcode=unique integer code for pending intent to distinguish it from other pending intents | FLAG_IMMUTABLE=changes made to original intent after pending intent got created won't affect the pending intent

        String notificationTitle = getString(R.string.notification_title);
        String notificationText = getString(R.string.notification_text);

        Intent wearActionIntent = new Intent(this, WearActionReciever.class);
        wearActionIntent.setAction("WEAR_ACTION");
        PendingIntent wearActionPendingIntent = PendingIntent.getBroadcast(this, 0, wearActionIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationText);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentIntent(pendingIntent);        //setting pendingIntent on a notification
        builder.extend(new NotificationCompat.WearableExtender().addAction(new NotificationCompat.Action(R.drawable.ic_notification, "Wearable Action", wearActionPendingIntent)));
        builder.setAutoCancel(true);        //to remove notification from status bar after getting clicked (just for convenience)


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        //check if permission is already granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.POST_NOTIFICATIONS},5);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        managerCompat.notify(2, builder.build());
    }

}
