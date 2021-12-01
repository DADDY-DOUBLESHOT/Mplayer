package com.example.mplayer

import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView

class MainActivity : AppCompatActivity() {

    lateinit var playBTN :ImageButton;
    lateinit var rewindBTN :ImageButton;
    lateinit var forwardBTN : ImageButton;
    lateinit var urlET :EditText;

    lateinit var trackSelector: DefaultTrackSelector;
    lateinit var exoplayer:SimpleExoPlayer;

    lateinit var mediaSession :MediaSession;
    lateinit var playbackstateCompact :PlaybackState.Builder;

    lateinit var  media:Uri;
    lateinit var mediaSource:ExtractorMediaSource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playBTN=findViewById<ImageButton>(R.id.play);
        rewindBTN=findViewById<ImageButton>(R.id.rewind);
        forwardBTN=findViewById<ImageButton>(R.id.forward);
        urlET=findViewById<EditText>(R.id.url);


        trackSelector=DefaultTrackSelector();












        playBTN.setOnClickListener {

            if(!checkUrl(urlET.text.toString()))
            {

            }
            else
            {
                Toast.makeText(this,"Enter Url to play song",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private fun checkUrl(url:String):Boolean
    {
        return url.isEmpty();
    }
}