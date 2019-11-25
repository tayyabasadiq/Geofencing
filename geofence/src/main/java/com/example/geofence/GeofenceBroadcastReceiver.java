package com.example.geofence;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

@RequiresApi(api = Build.VERSION_CODES.O)
public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    //  int notifyID = 1;
    Context context;

    String CHANNEL_ID = "my_channel_01";// The id of the channel.
    CharSequence name = "Monitoring Geofence";// The user-visible name of the channel.
    int importance = NotificationManager.IMPORTANCE_HIGH;
    @Override
    public void onReceive(Context contex, Intent intent) {
        this.context=contex;
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {
           // Toast.makeText(this, context.getString(R.string.trans), Toast.LENGTH_LONG).show();

            Log.e("create2", "triggergeofence");
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            // Create a detail message with Geofences received
            String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences);
            // Send notification details as a String

            sendNotification(geofenceTransitionDetails);
        }
    }
    // Create a detail message with Geofences received
    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }
        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER )
            status = "Entering ";
        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT )
            status = "Exiting ";
        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }


    // Send a notification

    private void sendNotification(String msg ) {
        Log.i(TAG, "sendNotification: " + msg);
       // Toast.makeText(this, "send", Toast.LENGTH_LONG).show();
        // Intent to start the main Activity
        Intent notificationIntent = Geofencingg.makeNotificationIntent(
                context, msg
        );
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Geofencingg.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent;
        notificationPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        // Creating and sending Notification
     //   LocationManager locationManager  = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        NotificationManager mNotificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        mNotificationManager.notify(
                GEOFENCE_NOTIFICATION_ID,
                createNotification(msg, notificationPendingIntent)) ;
        Log.e("create1", "createnotifica");
    }
    // Create a notification
    private Notification createNotification(String msg, PendingIntent notificationPendingIntent) {
       // Toast.makeText(this, "create", Toast.LENGTH_LONG).show();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setSmallIcon(R.drawable.googleg_standard_color_18)
                .setColor(Color.RED)
                .setContentTitle(msg)
                .setContentText("Geofence Notification!")
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID);
        return notificationBuilder.build();
    }
    // Handle errors
    private static String getErrorString(GeofenceBroadcastReceiver geofenceBroadcastReceiver, int errorCode) {

        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }

}
