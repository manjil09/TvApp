package com.manjil.tvapplication.playbackPage

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.leanback.app.PlaybackSupportFragment
import androidx.leanback.app.PlaybackSupportFragmentGlueHost
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackGlue
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.ControlButtonPresenterSelector
import androidx.leanback.widget.DetailsOverviewRowPresenter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.PlaybackControlsRow
import androidx.leanback.widget.PlaybackControlsRowPresenter
import androidx.leanback.widget.PlaybackSeekDataProvider
import com.manjil.tvapplication.CardPresenter
import com.manjil.tvapplication.detailsPage.DetailsDescriptionPresenter
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
                    playerGlue.seekProvider = CustomSeekDataProvider(requireContext(),videoUrl,20000L)
                    playerGlue.play()
                }
            }
        })
        playerGlue.title = selectedMovie!!.title
        playerAdapter.setDataSource(Uri.parse(videoUrl))
    }

    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> getSerializable(key: String): T? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            requireActivity().intent.getSerializableExtra(key) as T
        else
            requireActivity().intent.getSerializableExtra(key, T::class.java)
}