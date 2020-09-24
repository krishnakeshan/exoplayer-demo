package com.qrilt.trell_demo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util

class VideoPlayerActivity : AppCompatActivity() {
    // Views
    private lateinit var playerView: PlayerView

    // Properties
    private lateinit var uri: Uri
    private lateinit var player: SimpleExoPlayer
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT < 24)) {
            initPlayer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // find views
        playerView = findViewById(R.id.activity_video_player_playerview)

        // init player
        initPlayer()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(this).build();
        playerView.player = player

        // get media source
        val uri =
            Uri.parse(intent.getStringExtra("uri"))
        val mediaSource = buildMediaSource(uri)

        // set player parameters
        player.playWhenReady = playWhenReady
        player.seekTo(currentWindow, playbackPosition)
        player.prepare(mediaSource)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(this, "trell-exoplayer")
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    private fun hideSystemUi() {
    }

    private fun releasePlayer() {
        playWhenReady = player.getPlayWhenReady()
        playbackPosition = player.getCurrentPosition()
        currentWindow = player.getCurrentWindowIndex();
        player.release();
    }
}