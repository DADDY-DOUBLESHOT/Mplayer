package com.example.mplayer


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.exoplayer2.*
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


    lateinit var notificationManager: NotificationManager;
    lateinit var notificationChannel: NotificationChannel;
    lateinit var notificationCompatBuilder: NotificationCompat.Builder;


//    exoplayer notification manager

//    lateinit var playerNotificationManager: PlayerNotificationManager;
//    lateinit var mediaDescriptionAdapter: PlayerNotificationManager.MediaDescriptionAdapter;
//    lateinit var playerNotificationListner: PlayerNotificationManager.NotificationListener;


    var title = "Current Song Name";
    var desc = "Song details..."
    val notificationId: Int = 1234;
    val channelId = "MusicPlayer_channel";
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
//        mediaDescriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
//            override fun getCurrentContentTitle(player: Player): CharSequence {
//                return title;
//            }
//
//            override fun createCurrentContentIntent(player: Player): PendingIntent? {
//                return null
//            }
//
//            override fun getCurrentContentText(player: Player): CharSequence? {
//                return desc;
//            }
//
//            override fun getCurrentLargeIcon(
//                player: Player,
//                callback: PlayerNotificationManager.BitmapCallback
//            ): Bitmap? {
//                return null;
//            }
//
//        }
//        playerNotificationListner = object : PlayerNotificationManager.NotificationListener {
//            override fun onNotificationPosted(
//                notificationId: Int,
//                notification: Notification,
//                ongoing: Boolean
//            ) {
//                super.onNotificationPosted(notificationId, notification, ongoing)
//            }
//
//            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
//                super.onNotificationCancelled(notificationId, dismissedByUser)
//            }
//        }
//
//
//        playerNotificationManager = PlayerNotificationManager.Builder(this, 1234, "MusicPlayer_id")
//            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
//            .setNotificationListener(playerNotificationListner)
//            .build();
//
        initilize();

        urlET.addTextChangedListener {
            newUrl = true;
        }

        playBTN.setOnClickListener {
            playpause();
        }


        forwardBTN.setOnClickListener {
            forward()
        }

        rewindBTN.setOnClickListener {
            rewind();
        }


    }


    override fun onPause() {
        super.onPause()
        createNotification();
    }

    override fun onDestroy() {
        super.onDestroy()

//        playerNotificationManager.setPlayer(null);
        exoPlayer.release();
        notificationManager.notify(1234,null);
    }


    fun createNotification() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val intentPlay = Intent(this, ActionReceiver::class.java)
            intentPlay.putExtra("action", "play");
            val intentPause = Intent(this, ActionReceiver::class.java)
            intentPause.putExtra("action", "pause");
            val intentRewind = Intent(this, ActionReceiver::class.java)
            intentRewind.putExtra("action", "rewind");
            val intentForward = Intent(this, ActionReceiver::class.java)
            intentForward.putExtra("action", "forward");

            val pintentPlay =
                PendingIntent.getBroadcast(this, 1, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
            val pintentPause =
                PendingIntent.getBroadcast(this, 2, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            val pintentRewind =
                PendingIntent.getBroadcast(
                    this,
                    3,
                    intentRewind,
                    PendingIntent.FLAG_UPDATE_CURRENT
                );
            val pintentForward =
                PendingIntent.getBroadcast(
                    this,
                    4,
                    intentForward,
                    PendingIntent.FLAG_UPDATE_CURRENT
                );


            val action1 =
                NotificationCompat.Action(R.drawable.play_pause, "Play/Pause", pintentPlay);
            val action2 =
                NotificationCompat.Action(R.drawable.backward_black, "Rewind", pintentRewind);
            val action3 =
                NotificationCompat.Action(R.drawable.forward_black, "Forward", pintentForward);

            Toast.makeText(this, "App is paused ", Toast.LENGTH_SHORT).show();

            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            notificationChannel =
                NotificationChannel(channelId, desc, NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(notificationChannel)
            notificationCompatBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.ic_baseline_music_video_24
                    )
                )
                .addAction(action1)
                .addAction(action2)
                .addAction(action3)
                .setAutoCancel(true)
            notificationManager.notify(1234, notificationCompatBuilder.build());

        }


    }


    fun initilize() {
        exoPlayer = ExoPlayer.Builder(this).build();
        player = findViewById<PlayerView>(R.id.player);
        player.player = exoPlayer;

//        playerNotificationManager.setPlayer(exoPlayer);
//        playerNotificationManager.setVisibility(VISIBILITY_PUBLIC);
    }

    fun checkUrl(url: String): Boolean {
        return url.isEmpty();
    }

    fun playpause() {

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


//                    playerNotificationManager.setPlayer(exoPlayer);
                val intent = Intent(this, MusicService::class.java);
                intent.putExtra("state", "play")
                intent.putExtra("musicUrl", urlET.text.toString());
//                    startService(intent);

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

    fun forward() {

        if (exoPlayer.currentPosition < exoPlayer.duration - 5000) {
            exoPlayer.seekTo(exoPlayer.currentPosition + 5000);
        }


    }

    fun rewind() {
        if (exoPlayer.currentPosition >= 5000) {
            exoPlayer.seekTo(exoPlayer.currentPosition - 5000);
        }
    }


    inner class ActionReceiver: BroadcastReceiver() {


        override fun onReceive(context: Context?, intent: Intent?) {

            val action = intent?.getStringExtra("action");

            if (action.equals("play")) {
                Log.d("test", "action play");
                playpause();

            } else if (action.equals("pause")) {
                Log.d("test", "action pause");
                playpause();

            } else if (action.equals("rewind")) {
                Log.d("test", "action rewind");
                rewind();

            } else if (action.equals("forward")) {
                Log.d("test", "action forward");
                forward();
            }
        }


    }
}
