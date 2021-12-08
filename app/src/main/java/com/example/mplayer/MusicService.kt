package com.example.mplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView


class MusicService : Service() {




    override fun onBind(intent: Intent): IBinder? {

        return null;
    }

    override fun onRebind(intent: Intent?) {


    }


    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Music Service created ....", Toast.LENGTH_SHORT).show();
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return START_STICKY;

    }


    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Music Service have  been destroyed ....", Toast.LENGTH_SHORT).show();
    }
}