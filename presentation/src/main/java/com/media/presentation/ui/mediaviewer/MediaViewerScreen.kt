package com.media.presentation.ui.mediaviewer

import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.media.domain.model.MediaItem

@Composable
fun MediaViewerScreen(mediaItem: MediaItem) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (mediaItem) {

            is MediaItem.Video -> {
                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            val mediaController = MediaController(context)
                            mediaController.setAnchorView(this)
                            setVideoPath(mediaItem.path)
                            setMediaController(mediaController)
                            setOnPreparedListener {
                                start()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            is MediaItem.Image -> {
                AsyncImage(
                    model = mediaItem.path,
                    contentDescription = "Image Viewer",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
