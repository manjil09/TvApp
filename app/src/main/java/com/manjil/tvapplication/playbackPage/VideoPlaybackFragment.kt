package com.manjil.tvapplication.playbackPage

import androidx.leanback.app.PlaybackSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ControlButtonPresenterSelector
import androidx.leanback.widget.PlaybackControlsRow
import androidx.leanback.widget.PlaybackControlsRowPresenter

class VideoPlaybackFragment: PlaybackSupportFragment() {
    private lateinit var controllerActionAdapter: ArrayObjectAdapter
    private lateinit var mAdapter: ArrayObjectAdapter
    override fun onStart() {
        super.onStart()
        mAdapter = ArrayObjectAdapter(PlaybackControlsRowPresenter())

        setupController()

        adapter = mAdapter
    }

    private fun setupController() {
        controllerActionAdapter = ArrayObjectAdapter(ControlButtonPresenterSelector())
        val playbackControlsRow = PlaybackControlsRow()
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
}