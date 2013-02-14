package org.opengeo.geodroid;

import java.io.File;
import java.io.IOException;

import org.jeo.data.CachedRegistry;
import org.jeo.data.Registry;
import org.jeo.nano.FeatureHandler;
import org.jeo.nano.NanoJeoServer;
import org.jeo.nano.TileHandler;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class GeoDroidService extends Service {

    NanoJeoServer server;

    @Override
    public void onCreate() {
        File wwwRoot = new File(Environment.getExternalStorageDirectory(), "www");
        LocationManager locMgr = 
            (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        try {
            Registry reg = new CachedRegistry(new MapsRegistry());

            server = new NanoJeoServer(8000, wwwRoot, reg, new CurrentLocationHandler(locMgr), 
                new TileHandler(reg), new FeatureHandler(reg));
        }
        catch(IOException e) {
            Log.wtf("NanoHTTPD did not start", e);
        }

        Log.i("service", "GeoDroid started");
        notifyStarted();
    }

    void notifyStarted() {
        PendingIntent intent = TaskStackBuilder.create(this)
            .addNextIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("http://localhost:8000/www/")))
            .getPendingIntent(0,  PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
            .setContentTitle("GeoDroid").setContentText("Server online")
            .setSmallIcon(R.drawable.ic_notify).setContentIntent(intent);

        
        NotificationManager nMgr = 
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.notify(1, nBuilder.build());
    }

    void notifyStopped() {
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
        .setContentTitle("GeoDroid").setContentText("Server offline")
        .setSmallIcon(R.drawable.ic_notify);
    
        NotificationManager nMgr = 
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.notify(2, nBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        server.stop();

        Log.i("service", "GeoDroid stopped");
        notifyStopped();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

}
