package com.example.flashcard;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    public IBinder onBind(Intent intent) {
        return null; //epeche les autre composan de se lire
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer == null) {
            // ne pas dupliquer la music
            mediaPlayer = MediaPlayer.create(this, R.raw.portal_radio_tune);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        // redemar la music si elle est detruite
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
