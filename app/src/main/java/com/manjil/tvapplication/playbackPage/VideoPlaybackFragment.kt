package com.manjil.tvapplication.playbackPage

import android.net.Uri
import android.os.Build
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackGlue
import com.manjil.tvapplication.model.Movie
import java.io.Serializable

class VideoPlaybackFragment : VideoSupportFragment() {
    private var selectedMovie: Movie? = null
    override fun onStart() {
        super.onStart()
        selectedMovie = getSerializable("movie")
        setupPlayerGlue()
    }

    private fun setupPlayerGlue() {
        val videoUrl =
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4"
        val playerAdapter = MediaPlayerAdapter(context)
        val playerGlue = VideoPlayerGlue(requireContext(), playerAdapter)

        playerGlue.host = VideoSupportFragmentGlueHost(this)
        playerGlue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
            override fun onPreparedStateChanged(glue: PlaybackGlue?) {
                if (glue!!.isPrepared) {
                    playerGlue.seekProvider =
                        CustomSeekDataProvider(requireContext(), videoUrl, 20000L)
                    playerGlue.play()
                }
            }
        })
        playerGlue.title = selectedMovie?.title
        playerAdapter.setDataSource(Uri.parse(videoUrl))
    }

    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> getSerializable(key: String): T? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            requireActivity().intent.getSerializableExtra(key) as T
        else
            requireActivity().intent.getSerializableExtra(key, T::class.java)
}