package com.manjil.tvapplication.playbackPage

import android.content.Context
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.media.PlayerAdapter
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.PlaybackControlsRow

class VideoPlayerGlue(
    context: Context,
    playerAdapter: PlayerAdapter,
) : PlaybackTransportControlGlue<PlayerAdapter>(context, playerAdapter) {
    private var skipPreviousAction: PlaybackControlsRow.SkipPreviousAction
    private var skipNextAction: PlaybackControlsRow.SkipNextAction
    private var fastForwardAction: PlaybackControlsRow.FastForwardAction
    private var rewindAction: PlaybackControlsRow.RewindAction

    init {
        skipNextAction = PlaybackControlsRow.SkipNextAction(context)
        skipPreviousAction = PlaybackControlsRow.SkipPreviousAction(context)
        fastForwardAction = PlaybackControlsRow.FastForwardAction(context)
        rewindAction = PlaybackControlsRow.RewindAction(context)
    }

    override fun onCreatePrimaryActions(primaryActionsAdapter: ArrayObjectAdapter?) {
        primaryActionsAdapter?.apply {
            add(skipPreviousAction)
            add(rewindAction)
            super.onCreatePrimaryActions(primaryActionsAdapter)
            add(fastForwardAction)
            add(skipNextAction)
        }
    }

    override fun onActionClicked(action: Action?) {
        when (action) {
            rewindAction -> {
                var newPosition: Long = currentPosition - 10000
                newPosition = if (newPosition < 0) 0 else newPosition
                playerAdapter.seekTo(newPosition)
            }

            fastForwardAction -> {
                var newPosition: Long = currentPosition + 10000
                newPosition = if (newPosition > duration) duration else newPosition
                playerAdapter.seekTo(newPosition)
            }

            else -> super.onActionClicked(action)
        }
    }
}