package com.manjil.tvapplication.playbackPage

import android.content.Context
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.media.PlayerAdapter
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.PlaybackControlsRow
import java.util.concurrent.TimeUnit

class ExoplayerGlue<T : PlayerAdapter>(
    context: Context,
    impl: T,
    private val updateProgress: () -> Unit,
) : PlaybackTransportControlGlue<T>(context, impl) {

    // Define actions for fast forward and rewind operations.
    private var skipForwardAction: PlaybackControlsRow.FastForwardAction =
        PlaybackControlsRow.FastForwardAction(context)

    private var skipBackwardAction: PlaybackControlsRow.RewindAction =
        PlaybackControlsRow.RewindAction(context)

    override fun onCreatePrimaryActions(primaryActionsAdapter: ArrayObjectAdapter) {
        // super.onCreatePrimaryActions() will create the play / pause action.
        super.onCreatePrimaryActions(primaryActionsAdapter)

        // Add the rewind and fast forward actions following the play / pause action.
//        primaryActionsAdapter.apply {
//            add(skipBackwardAction)
//            add(skipForwardAction)
//        }
    }

    override fun onUpdateProgress() {
        super.onUpdateProgress()
        updateProgress()
    }

    override fun onActionClicked(action: Action) {
        // Primary actions are handled manually. The superclass handles default play/pause action.
        when (action) {
            skipBackwardAction -> skipBackward()
            skipForwardAction -> skipForward()
            else -> super.onActionClicked(action)
        }
    }

    /** Skips backward 30 seconds.  */
    private fun skipBackward() {
        var newPosition: Long = currentPosition - THIRTY_SECONDS
        newPosition = newPosition.coerceAtLeast(0L)
        playerAdapter.seekTo(newPosition)
    }

    /** Skips forward 30 seconds.  */
    private fun skipForward() {
        var newPosition: Long = currentPosition + THIRTY_SECONDS
        newPosition = newPosition.coerceAtMost(duration)
        playerAdapter.seekTo(newPosition)
    }

    companion object {
        private val THIRTY_SECONDS = TimeUnit.SECONDS.toMillis(30)
    }
}