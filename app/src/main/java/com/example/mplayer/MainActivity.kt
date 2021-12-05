package com.example.mplayer

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerView

class MainActivity : AppCompatActivity() {

    lateinit var playBTN: ImageButton;
    lateinit var rewindBTN: ImageButton;
    lateinit var forwardBTN: ImageButton;
    lateinit var urlET: EditText;


    lateinit var exoPlayer: ExoPlayer;
    lateinit var player: PlayerView;
    lateinit var mediaItem: MediaItem;

    lateinit var progressbar: ProgressBar;


    var newUrl: Boolean = false;

    var isplaying: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playBTN = findViewById<ImageButton>(R.id.play);
        rewindBTN = findViewById<ImageButton>(R.id.rewind);
        forwardBTN = findViewById<ImageButton>(R.id.forward);
        urlET = findViewById<EditText>(R.id.url);

        progressbar = findViewById<ProgressBar>(R.id.progressBar)


        initilize();




        urlET.addTextChangedListener {

            newUrl = true;
        }

        playBTN.setOnClickListener {


            if (!checkUrl(urlET.text.toString())) {


                if (newUrl) {
                    mediaItem = MediaItem.fromUri(urlET.text.toString());
                    exoPlayer.addMediaItem(mediaItem);

                    while (exoPlayer.isLoading) {
                        progressbar.isVisible = true;
                    }
                    progressbar.isVisible = false;


                    exoPlayer.prepare();
                    exoPlayer.play();
                    isplaying = true;
                    playBTN.setBackgroundResource(R.drawable.pause);
                    player.isVisible = true
                    newUrl = false;
                    player.useController = false;

//                    startService(Intent(this, MusicService::class.java));

                } else {
                    if (isplaying) {
                        isplaying = false;
                        playBTN.setBackgroundResource(R.drawable.play);
                        exoPlayer.pause();

//                        stopService(Intent(this, MusicService::class.java))

                    } else {
                        exoPlayer.play();
                        isplaying = true;
                        playBTN.setBackgroundResource(R.drawable.pause);
                        player.isVisible = true

                    }

                }


            } else {
                Toast.makeText(this, "Enter Url to play song", Toast.LENGTH_SHORT).show();
            }
        }


//        playBTN.setOnClickListener {
//
//            if(!checkUrl(urlET.text.toString()))
//            {
//                    val intent= Intent(this,MusicService::class.java);
//                    bindService(intent,Service.con);
//            }
//            else
//            {
//                Toast.makeText(this, "Enter Url to play song", Toast.LENGTH_SHORT).show();
//            }
//
//        }

        forwardBTN.setOnClickListener {

//            Toast.makeText(this, exoPlayer.currentPosition.toString(), Toast.LENGTH_SHORT).show();


            if (exoPlayer.currentPosition < exoPlayer.duration - 5000) {
                exoPlayer.seekTo(exoPlayer.currentPosition + 5000);
            }


        }
        rewindBTN.setOnClickListener {

//            Toast.makeText(this, exoPlayer.currentPosition.toString(), Toast.LENGTH_SHORT).show();
            if (exoPlayer.currentPosition >= 5000) {
                exoPlayer.seekTo(exoPlayer.currentPosition - 5000);
            }

        }


    }


    private fun initilize() {
        exoPlayer = ExoPlayer.Builder(this).build();
        player = findViewById<PlayerView>(R.id.player);
        player.player = exoPlayer;
    }

    private fun checkUrl(url: String): Boolean {
        return url.isEmpty();
    }

    private fun setPlayerNotificationManager(player :ExoPlayer)
    {
        val manager =PlayerNotificationManager(this,"123",1234,)
    }


}