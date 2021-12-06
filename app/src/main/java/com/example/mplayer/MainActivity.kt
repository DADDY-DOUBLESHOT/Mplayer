package com.example.mplayer

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
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


    lateinit var playerNotificationManager: PlayerNotificationManager;
    lateinit var mediaDescriptionAdapter: PlayerNotificationManager.MediaDescriptionAdapter;
    lateinit var playerNotificationListner: PlayerNotificationManager.NotificationListener;

    var title ="Current Song Name";
    var desc ="Song details..."
    val notificationId :Int =1234;

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


//        playerNotificationManager=PlayerNotificationManager.Builder(this,1234,"MusicPlayer_id").build();
        mediaDescriptionAdapter=object : PlayerNotificationManager.MediaDescriptionAdapter{
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return title;
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
              return null
            }

            override fun getCurrentContentText(player: Player): CharSequence? {
                return desc;
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
               return null;
            }

        }
        playerNotificationListner=object :PlayerNotificationManager.NotificationListener{
            override fun onNotificationPosted(
                notificationId: Int,
                notification: Notification,
                ongoing: Boolean
            ) {
                super.onNotificationPosted(notificationId, notification, ongoing)
            }

            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                super.onNotificationCancelled(notificationId, dismissedByUser)
            }
        }


        playerNotificationManager=PlayerNotificationManager.Builder(this,1234,"MusicPlayer_id")
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .setNotificationListener(playerNotificationListner)
            .build();



//        playerNotificationManager = PlayerNotificationManager.Builder(
//
//            this,
//            1234,
//            "MusicPlayer_id",
//            object :  PlayerNotificationManager.MediaDescriptionAdapter {
//                override fun getCurrentContentTitle(player: Player): CharSequence {
//
//                    return  title;
//                }
//
//                override fun createCurrentContentIntent(player: Player): PendingIntent? {
//
//                    return null;
//
//                }
//                override fun getCurrentContentText(player: Player): CharSequence? {
//                    return  desc;
//                }
//                override fun getCurrentLargeIcon(
//                    player: Player,
//                    callback: PlayerNotificationManager.BitmapCallback
//                ): Bitmap? {
//                    return null;
//                }
//            }
//
//        ).build();



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
                    playerNotificationManager.setPlayer(exoPlayer);

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

    override fun onDestroy() {
        super.onDestroy()

        playerNotificationManager.setPlayer(null);
        exoPlayer.release();
    }


    private fun initilize() {
        exoPlayer = ExoPlayer.Builder(this).build();
        player = findViewById<PlayerView>(R.id.player);
        player.player = exoPlayer;
        playerNotificationManager.setPlayer(exoPlayer);
        playerNotificationManager.setVisibility(VISIBILITY_PUBLIC);
    }

    private fun checkUrl(url: String): Boolean {
        return url.isEmpty();
    }



}