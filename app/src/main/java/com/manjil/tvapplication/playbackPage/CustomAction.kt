package com.manjil.tvapplication.playbackPage

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.widget.PlaybackControlsRow.MultiAction
import com.manjil.tvapplication.R

class CustomAction(context: Context) : MultiAction(R.id.tv_custom_action_id) {
    init {
        val drawables = arrayOf(
            AppCompatResources.getDrawable(context, R.drawable.ic_play),
            AppCompatResources.getDrawable(context, R.drawable.ic_pause)
        )
        setDrawables(drawables)
        val labels = arrayOf("Play", "Pause")
        setLabels(labels)
    }

    companion object {
        const val INDEX_PLAY = 0
        const val INDEX_PAUSE = 1
    }
}