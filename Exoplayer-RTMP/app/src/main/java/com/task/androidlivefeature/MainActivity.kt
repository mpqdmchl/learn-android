package com.task.androidlivefeature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.task.androidlivefeature.screen.ExoPlayerView
import com.task.androidlivefeature.ui.theme.AndroidLiveFeatureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidLiveFeatureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExoPlayerView()
                }
            }
        }
    }
    companion object{
//        const val EXAMPLE_VIDEO_URI = "rtmp://172.16.16.199/live/test"
        const val EXAMPLE_VIDEO_URI = "rtmp://mobliestream.c3tv.com:554/live/goodtv.sdp"  // 可播放
//        const val EXAMPLE_VIDEO_URI = "rtmp://liteavapp.qcloud.com/live/liteavdemoplayerstreamid" //闪退
    }
}



