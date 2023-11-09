package com.manjil.tvapplication.playbackPage

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackGlue
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import com.manjil.tvapplication.detailsPage.CardPresenter
import com.manjil.tvapplication.model.Movie
import com.manjil.tvapplication.model.MovieRepo
import java.io.Serializable

class VideoPlaybackFragment : VideoSupportFragment() {
    private var selectedMovie: Movie? = null
    private val movieRepo = MovieRepo()
    private val relatedMovieList = movieRepo.getMovieList().shuffled()
    private lateinit var playerGlue: VideoPlayerGlue
    private lateinit var relatedVideoAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (adapter.presenterSelector as ClassPresenterSelector).addClassPresenter(
            ListRow::class.java,
            ListRowPresenter()
        )
        setupRelatedVideo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedMovie = getSerializable("movie")
        setupPlayerGlue()

        setOnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is Movie && row is ListRow && row.adapter == relatedVideoAdapter) {
                playNewMovie(item)
            }

        }
        setOnKeyInterceptListener { v, keyCode, event ->
            if (!isControlsOverlayVisible) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    setSelectedPosition(0)
                    if (playerGlue.isPlaying) playerGlue.pause() else playerGlue.play()
                    return@setOnKeyInterceptListener true
                }
            }
            false
        }

    }

    private fun setupRelatedVideo() {
        relatedVideoAdapter = ArrayObjectAdapter(CardPresenter())
        relatedVideoAdapter.addAll(0, relatedMovieList)

        val relatedVideoRow = ListRow(1L, HeaderItem("Related Videos"), relatedVideoAdapter)
        Log.d("vidPlayback", "onViewCreated: adapter added.")
        (adapter as ArrayObjectAdapter).add(relatedVideoRow)
    }

    private fun setupPlayerGlue() {
        val playerAdapter = MediaPlayerAdapter(context)
        playerGlue = VideoPlayerGlue(requireContext(), playerAdapter)

        playerGlue.host = VideoSupportFragmentGlueHost(this)
        playerGlue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
            override fun onPreparedStateChanged(glue: PlaybackGlue?) {
                if (glue!!.isPrepared) {
                    playerGlue.seekProvider =
                        CustomSeekDataProvider(requireContext(), selectedMovie!!.videoUrl, 20000L)
                }
            }

            override fun onPlayCompleted(glue: PlaybackGlue?) {
                playNewMovie(relatedMovieList[0])
            }
        })
        playerGlue.playWhenPrepared()
        playerGlue.title = selectedMovie?.title
        playerAdapter.setDataSource(Uri.parse(selectedMovie!!.videoUrl))
    }

    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> getSerializable(key: String): T? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            requireActivity().intent.getSerializableExtra(key) as T
        else
            requireActivity().intent.getSerializableExtra(key, T::class.java)

    private fun playNewMovie(movie: Movie) {
        val intent = Intent(context, VideoPlaybackActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        playerGlue.pause()
    }
}