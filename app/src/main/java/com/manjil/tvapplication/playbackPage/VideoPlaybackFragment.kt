package com.manjil.tvapplication.playbackPage

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackGlue
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.PlaybackControlsRow
import com.manjil.tvapplication.MovieCardPresenter
import com.manjil.tvapplication.R
import com.manjil.tvapplication.detailsPage.CardPresenter
import com.manjil.tvapplication.model.Movie
import com.manjil.tvapplication.model.MovieRepo
import java.io.Serializable


class VideoPlaybackFragment : VideoSupportFragment() {
    private var selectedMovie: Movie? = null
    private val movieRepo = MovieRepo()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (adapter.presenterSelector as ClassPresenterSelector)
            .addClassPresenter(ListRow::class.java, ListRowPresenter())

        val relatedVideoAdapter = ArrayObjectAdapter(MovieCardPresenter())
        relatedVideoAdapter.addAll(0, movieRepo.getMovieList())

        val relatedVideoRow = ListRow(1L, HeaderItem("Up Next"), relatedVideoAdapter)
        Log.d("vidPlayback", "onViewCreated: adapter added.")
        (adapter as ArrayObjectAdapter).add(relatedVideoRow)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedMovie = getSerializable("movie")
        setupPlayerGlue()
    }


    private fun setupPlayerGlue() {
        val videoUrl =
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4"
        val playerAdapter = MediaPlayerAdapter(context)
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)
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