package com.manjil.tvapplication.playbackPage

import android.net.Uri
import android.os.Build
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
import com.manjil.tvapplication.CardPresenter
import com.manjil.tvapplication.detailsPage.DetailsDescriptionPresenter
import com.manjil.tvapplication.model.Movie
import java.io.Serializable

class VideoPlaybackFragment : VideoSupportFragment() {
    private var selectedMovie: Movie? = null
    private lateinit var mAdapter: ArrayObjectAdapter
    override fun onStart() {
        super.onStart()
        selectedMovie = getSerializable("movie")
        setupPlayerGlue()
    }

    private fun setupPlayerGlue() {
        val playerAdapter = MediaPlayerAdapter(context)
        val playerGlue = VideoPlayerGlue(requireContext(),playerAdapter)

        playerGlue.host = VideoSupportFragmentGlueHost(this@VideoPlaybackFragment)
        playerGlue.addPlayerCallback(object : PlaybackGlue.PlayerCallback(){
            override fun onPreparedStateChanged(glue: PlaybackGlue?) {
                if (glue!!.isPrepared){
//                    playerGlue.seekProvider = Mysee
                    playerGlue.play()
                }
            }
        })
        playerGlue.title = selectedMovie!!.title
        val videoUrl = "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4"
        playerAdapter.setDataSource(Uri.parse(videoUrl))
    }



    private fun setupRows() {
        val presenterSelector = ClassPresenterSelector()
        val playbackControlsRowPresenter =
            PlaybackControlsRowPresenter(DetailsDescriptionPresenter())

        presenterSelector.addClassPresenter(
            PlaybackControlsRow::class.java,
            playbackControlsRowPresenter
        )
        presenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())

        mAdapter = ArrayObjectAdapter(presenterSelector)
    }

    private fun setupController() {
        val controllerActionAdapter = ArrayObjectAdapter(ControlButtonPresenterSelector())
        val playbackControlsRow = PlaybackControlsRow(selectedMovie)
        mAdapter.add(playbackControlsRow)

        playbackControlsRow.primaryActionsAdapter = controllerActionAdapter

        val context = requireContext()
        val playPauseAction = PlaybackControlsRow.PlayPauseAction(context)
        val skipNextAction = PlaybackControlsRow.SkipNextAction(context)
        val skipPreviousAction = PlaybackControlsRow.SkipPreviousAction(context)
        val fastForwardAction = PlaybackControlsRow.FastForwardAction(context)
        val rewindAction = PlaybackControlsRow.RewindAction(context)

        controllerActionAdapter.run {
            add(skipPreviousAction)
            add(rewindAction)
            add(playPauseAction)
            add(fastForwardAction)
            add(skipNextAction)
        }
    }

//    setupRows()
//    setupController()
//    addOtherRows()
//
//    isControlsOverlayAutoHideEnabled = true
//    adapter = mAdapter
    private fun addOtherRows() {
        val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        listRowAdapter.add(
            Movie(
                "First Title",
                "Description for first title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"
            )
        )
        listRowAdapter.add(
            Movie(
                "Second Title",
                "Description for second title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg"
            )
        )
        listRowAdapter.add(
            Movie(
                "Third Title",
                "Description for third title",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"
            )
        )
        mAdapter.add(ListRow(HeaderItem("More like this"), listRowAdapter))
    }

    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> getSerializable(key: String): T? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            requireActivity().intent.getSerializableExtra(key) as T
        else
            requireActivity().intent.getSerializableExtra(key, T::class.java)
}