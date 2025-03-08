package com.media.presentation.ui.albumdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.media.domain.model.Album
import com.media.domain.model.MediaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumPair: Pair<Album, List<MediaItem>>,
    onMediaClick: (MediaItem) -> Unit,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = albumPair.first.name) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
        albumPair.second.takeIf { it.isNotEmpty() }?.let {
            MediaItemGrid(
                mediaPaths = it,
                onMediaClick = onMediaClick
            )
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No media found in this album", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun MediaItemGrid(mediaPaths: List<MediaItem>, onMediaClick: (MediaItem) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        contentPadding = PaddingValues(1.dp)
    ) {
        items(mediaPaths.size) { index ->
            val mediaPath = mediaPaths[index]
            MediaItem(mediaPath = mediaPath, onMediaClick = onMediaClick)
        }
    }
}

@Composable
fun MediaItem(mediaPath: MediaItem, onMediaClick: (MediaItem) -> Unit) {
    val isVideo = mediaPath is MediaItem.Video
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onMediaClick(mediaPath) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(1.dp)
        ) {
            AsyncImage(
                model = mediaPath.getMediaPath(),
                contentDescription = "Album Cover",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 1.dp)
            )

            if (isVideo) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}
