package com.qrilt.trell_demo

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.content.ContentResolverCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qrilt.trell_demo.adapters.VideosAdapter
import com.qrilt.trell_demo.model.UserVideo
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), VideosAdapter.InteractionListener {
    // Views
    private lateinit var videosRecyclerView: RecyclerView
    private lateinit var streamButton: Button

    // Properties
    private lateinit var viewAdapter: VideosAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var userVideos = mutableListOf<UserVideo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init stream button
        streamButton = findViewById(R.id.activity_main_internet_button)
        streamButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra("uri", "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
            }
            startActivity(intent)
        })

        // init recycler view
        viewManager = LinearLayoutManager(this)
        viewAdapter = VideosAdapter(userVideos, this)
        videosRecyclerView =
            findViewById<RecyclerView>(R.id.activity_main_videos_recycler_view).apply {
                setHasFixedSize(true);

                // linear layout manager for list
                layoutManager = viewManager

                // adapter to bind videos -> list items
                adapter = viewAdapter
            }

        // get videos
        getUserVideos()
    }

    fun getUserVideos() {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE
        )
        val selection = null
        val selectionArgs = null
        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

        val query = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                userVideos.add(UserVideo(contentUri, "name", size))
                Log.d("DebugK", userVideos[0].uri.toString())
            }
            viewAdapter.setContent(userVideos)
            viewAdapter.notifyDataSetChanged()
        }
    }

    private fun playVideo(userVideo: UserVideo) {
        val intent = Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra("uri", userVideo.uri.toString())
        }
        startActivity(intent)
    }

    override fun onItemSelected(position: Int) {
        playVideo(userVideos[position])
    }
}