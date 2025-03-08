package com.media.presentation.ui.mediaviewer

import android.widget.VideoView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.media.domain.model.MediaItem

@Composable
fun MediaViewerScreen(mediaItem: MediaItem) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        when (mediaItem) {

            is MediaItem.Video -> {
                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            setVideoPath(mediaItem.getMediaPath())
                            start()
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                AsyncImage(
                    model = mediaItem.getMediaPath(),
                    contentDescription = "Image Viewer",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
