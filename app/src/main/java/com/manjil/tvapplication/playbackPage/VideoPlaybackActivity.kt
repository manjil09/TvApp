package com.manjil.tvapplication.playbackPage

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.manjil.tvapplication.R
import com.manjil.tvapplication.model.Movie
import java.io.Serializable

class VideoPlaybackActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_playback)

        val selectedMovie = getSerializable<Movie>("movie")
        supportFragmentManager.beginTransaction().replace(R.id.videoPlaybackFragment,VideoPlaybackFragment.newInstance(selectedMovie)).commit()
    }

    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> getSerializable(key: String): T? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(key) as T
        else
            intent.getSerializableExtra(key, T::class.java)
}