package com.manjil.tvapplication.playbackPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.manjil.tvapplication.R

class VideoPlaybackActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_playback)
        supportFragmentManager.beginTransaction().replace(R.id.videoPlaybackFragment,VideoPlaybackFragment()).commit()
    }
}