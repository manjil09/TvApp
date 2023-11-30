package com.manjil.tvapplication.playbackPage

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import androidx.annotation.OptIn
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackGlue
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.leanback.LeanbackPlayerAdapter
import com.manjil.tvapplication.R
import com.manjil.tvapplication.detailsPage.CardPresenter
import com.manjil.tvapplication.model.Movie
import com.manjil.tvapplication.model.MovieRepo
import java.io.Serializable
import java.time.Duration

private const val ARG_MOVIE = "movie"
private const val HIDE_CONTROL_DELAY = 4000L

class VideoPlaybackFragment : VideoSupportFragment() {
    private var exoplayer: ExoPlayer? = null
    private var selectedMovie: Movie? = null
    private val movieRepo = MovieRepo()
    private val relatedMovieList = movieRepo.getMovieList().shuffled()

    //    private lateinit var playerGlue: VideoPlayerGlue
    private lateinit var relatedVideoAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null && !::relatedVideoAdapter.isInitialized) {
            (adapter.presenterSelector as ClassPresenterSelector).addClassPresenter(
                ListRow::class.java, ListRowPresenter()
            )
            setupRelatedVideo()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedMovie = getSerializable(ARG_MOVIE)
//        setupPlayerGlue()

        setOnItemViewClickedListener { _, item, _, row ->
            if (item is Movie && row is ListRow) {
                playNewMovie(item)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    private fun setupRelatedVideo() {
        relatedVideoAdapter = ArrayObjectAdapter(CardPresenter())
        relatedVideoAdapter.addAll(0, relatedMovieList)

        val relatedVideoRow = ListRow(1L, HeaderItem("Related Videos"), relatedVideoAdapter)
        val moreVideoRow = ListRow(
            HeaderItem("More"),
            ArrayObjectAdapter(CardPresenter()).apply { addAll(0, relatedMovieList.shuffled()) })
        (adapter as ArrayObjectAdapter).add(relatedVideoRow)
        (adapter as ArrayObjectAdapter).add(moreVideoRow)
    }

    @UnstableApi
    private fun prepareGlue(localExoplayer: ExoPlayer) {
        ExoplayerGlue(
            requireContext(),
            LeanbackPlayerAdapter(
                requireContext(),
                localExoplayer,
                PLAYER_UPDATE_INTERVAL_MILLIS.toInt()
            )
        ) {}.apply {
            host = VideoSupportFragmentGlueHost(this@VideoPlaybackFragment)
            title = selectedMovie?.title

            isSeekEnabled = true
        }
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer() {
        exoplayer = ExoPlayer.Builder(requireContext()).build().apply {
            prepare()
            val uri = Uri.parse(selectedMovie!!.videoUrl)
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepareGlue(this)
            play()
        }
    }

    private fun destroyPlayer() {
        exoplayer?.let {
            it.pause()
            it.release()
            exoplayer = null
        }
    }

//    private fun setupPlayerGlue() {
//        val playerAdapter = object : MediaPlayerAdapter(context) {
//            override fun next() {
//                playNewMovie(relatedMovieList[0])
//            }
//
//            override fun previous() {
//                if (parentFragmentManager.backStackEntryCount > 0)
//                    parentFragmentManager.popBackStack()
//            }
//        }
//        playerGlue = VideoPlayerGlue(requireContext(), playerAdapter)
//
//        playerGlue.host = VideoSupportFragmentGlueHost(this)
//        playerGlue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
//            override fun onPreparedStateChanged(glue: PlaybackGlue?) {
//                if (glue!!.isPrepared) {
//                    playerGlue.seekProvider =
//                        CustomSeekDataProvider(requireContext(), selectedMovie!!.videoUrl, 20000L)
//                }
//            }
//
//            override fun onPlayCompleted(glue: PlaybackGlue?) {
//                playNewMovie(relatedMovieList[0])
//            }
//        })
//        playerGlue.playWhenPrepared()
//        playerGlue.title = selectedMovie?.title
//        playerAdapter.setDataSource(Uri.parse(selectedMovie!!.videoUrl))
//    }

    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> getSerializable(key: String): T? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) arguments?.getSerializable(key) as T
        else arguments?.getSerializable(key, T::class.java)

    private fun playNewMovie(movie: Movie) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.videoPlaybackFragment, newInstance(movie)).addToBackStack(null).commit()
    }

    override fun onResume() {
        super.onResume()
        showControlsOverlay(false)
        setSelectedPosition(0)
    }

    override fun onStop() {
        super.onStop()
        destroyPlayer()
    }

    companion object {

        private val PLAYER_UPDATE_INTERVAL_MILLIS = Duration.ofMillis(50).toMillis()
        private const val MEDIA_SESSION_TAG = "ReferenceAppKotlin"
        fun newInstance(movie: Movie?) = VideoPlaybackFragment().apply {
            arguments = Bundle().apply { putSerializable(ARG_MOVIE, movie) }
        }
    }
}