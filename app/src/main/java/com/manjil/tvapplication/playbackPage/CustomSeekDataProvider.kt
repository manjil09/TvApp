package com.manjil.tvapplication.playbackPage

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.leanback.widget.PlaybackSeekDataProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CustomSeekDataProvider(private val videoUrl: String, private val interval: Long) :
    PlaybackSeekDataProvider() {
    private var mSeekPositions: LongArray

    init {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoUrl, HashMap())

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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val thumbnail = loadThumbnailInBackground(index)

                withContext(Dispatchers.Main){
                    callback?.onThumbnailLoaded(thumbnail, index)
                }
            }catch (e: Exception){
                Log.d("TAG", "getThumbnail: ${e.printStackTrace()}")
            }
        }
    }

    private fun loadThumbnailInBackground(index: Int): Bitmap? {
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(videoUrl)
            val time = mSeekPositions[index] * 1000
            return retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "getThumbnail: exception at index $index")
        }finally {
            retriever.release()
        }
        return null
    }
}
