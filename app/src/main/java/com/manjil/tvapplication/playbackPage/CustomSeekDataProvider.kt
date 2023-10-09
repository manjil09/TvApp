package com.manjil.tvapplication.playbackPage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.leanback.widget.PlaybackSeekDataProvider
import com.manjil.tvapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CustomSeekDataProvider(private val context: Context, videoUrl: String, interval: Long) :
    PlaybackSeekDataProvider() {
    private var mSeekPositions: LongArray
    private var retriever = MediaMetadataRetriever()
    private val thumbnailCache = HashMap<Int, Bitmap?>()

    init {
        retriever.setDataSource(videoUrl)

        val duration =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong()

        val size: Int = ((duration / interval) + 1).toInt()

        mSeekPositions = LongArray(size)
        for (i in 0..<size) seekPositions[i] = i * interval
    }

    override fun getSeekPositions(): LongArray {
        return mSeekPositions
    }

    override fun getThumbnail(index: Int, callback: ResultCallback?) {
        var thumbnail = thumbnailCache[index]

        if (thumbnail == null) {
            val placeholder = (ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.place_holder_image,
                null
            ) as VectorDrawable).toBitmap()

            callback?.onThumbnailLoaded(placeholder, index)
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (thumbnail == null) {
                    thumbnail = loadThumbnailInBackground(index)
                    thumbnailCache[index] = thumbnail
                }
                withContext(Dispatchers.Main) {
                    callback?.onThumbnailLoaded(thumbnail, index)
                }
            } catch (e: Exception) {
                Log.d("TAG", "getThumbnail: ${e.printStackTrace()}")
            }
        }
    }

    private fun loadThumbnailInBackground(index: Int): Bitmap? {
        try {
            val time = mSeekPositions[index] * 1000

            return retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "getThumbnail: exception at index $index")
        }
        return null
    }
}
