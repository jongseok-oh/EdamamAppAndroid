package com.example.finalproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    MediaPlayer player;
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
    }
    @Override
    public void onDestroy(){
        player.stop();
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        player = MediaPlayer.create(this,R.raw.white_christmas);
        player.setLooping(true);
        player.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
