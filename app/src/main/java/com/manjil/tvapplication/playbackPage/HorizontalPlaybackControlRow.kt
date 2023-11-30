package com.manjil.tvapplication.playbackPage

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HorizontalGridView
import androidx.leanback.widget.PlaybackControlsRow
import androidx.leanback.widget.PlaybackRowPresenter

class HorizontalPlaybackControlRow(context: Context): PlaybackControlsRow(context){
    private val controlsRowView: HorizontalGridView

    init {
        val inflater = LayoutInflater.from(context)
        controlsRowView = HorizontalGridView(context)
        controlsRowView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Set the number of rows and columns as needed
        controlsRowView.numRows = 1
        controlsRowView.numColumns = 2

        // Set the adapter for the controls row
        controlsRowView.adapter = ArrayObjectAdapter(PlaybackRowPresenter())

        // Add the view to the controls row
        addView(controlsRowView)
    }
}