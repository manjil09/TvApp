package com.manjil.tvapplication.playbackPage

import android.content.Context
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.media.PlayerAdapter
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.PlaybackControlsRow

class VideoPlayerGlue(
    context: Context,
    playerAdapter: MediaPlayerAdapter,
) : PlaybackTransportControlGlue<MediaPlayerAdapter>(context, playerAdapter) {
    private val skipNextAction = PlaybackControlsRow.SkipNextAction(context)
    private val skipPreviousAction = PlaybackControlsRow.SkipPreviousAction(context)
    private val fastForwardAction = PlaybackControlsRow.FastForwardAction(context)
    private val rewindAction = PlaybackControlsRow.RewindAction(context)
    private val thumbsUpAction = PlaybackControlsRow.ThumbsUpAction(context).apply {
        index = PlaybackControlsRow.ThumbsUpAction.INDEX_OUTLINE
    }
    private val shuffleAction = PlaybackControlsRow.ShuffleAction(context)
    private val repeatAction = PlaybackControlsRow.RepeatAction(context)
    private val thumbsDownAction = PlaybackControlsRow.ThumbsDownAction(context).apply {
        index = PlaybackControlsRow.ThumbsDownAction.INDEX_OUTLINE
    }
    private val customAction = CustomAction(context).apply {
        index = CustomAction.INDEX_PLAY
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

    override fun onCreateSecondaryActions(secondaryActionsAdapter: ArrayObjectAdapter?) {
        secondaryActionsAdapter?.apply {
            add(thumbsUpAction)
            add(thumbsDownAction)
            add(shuffleAction)
            add(repeatAction)
            add(customAction)
        }
    }

    override fun onActionClicked(action: Action?) {
        when (action) {
            rewindAction -> {
                var newPosition = currentPosition - 10000
                newPosition = if (newPosition < 0) 0 else newPosition
                playerAdapter.seekTo(newPosition)
            }

            fastForwardAction -> {
                var newPosition = currentPosition + 10000
                newPosition = if (newPosition > duration) duration else newPosition
                playerAdapter.seekTo(newPosition)
            }

            thumbsUpAction,
            shuffleAction,
            repeatAction,
            thumbsDownAction,
            customAction,
            -> onSecondaryActionPressed(action)

            else -> super.onActionClicked(action)
        }
    }

    private fun onSecondaryActionPressed(action: Action) {
        val adapter = controlsRow.secondaryActionsAdapter as? ArrayObjectAdapter ?: return
        if (action is PlaybackControlsRow.MultiAction) {
            action.nextIndex()
            notifyItemChanged(adapter, action)
        }
    }
}