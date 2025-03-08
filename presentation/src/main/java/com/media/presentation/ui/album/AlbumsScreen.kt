package com.media.presentation.ui.album

import android.Manifest
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.media.domain.model.Album
import com.media.domain.model.MediaItem

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit,
    onBackClick: () -> Unit,
    reloadAlbums: () -> Unit
) {
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
    } else {
        listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    var isGridView by rememberSaveable { mutableStateOf(true) }

    val gridState = rememberLazyGridState()

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            reloadAlbums()
        } else {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "Gallery") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { isGridView = !isGridView }) {
                    if (isGridView) {
                        Icon(
                            imageVector = Icons.Default.ViewList,
                            contentDescription = "Switch to List View"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Switch to Grid View"
                        )
                    }
                }
            }
        )
        if (permissionState.allPermissionsGranted) {
            if (isGridView) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    state = rememberLazyGridState(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(albums.size) { index ->
                        AlbumCard(album = albums[index], onAlbumClick = onAlbumClick)
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
                    items(albums.size) { index ->
                        AlbumListItem(album = albums[index], onAlbumClick = onAlbumClick)
                    }
                }
            }
        } else {
            PermissionRequiredDialog(onGrantPermission = {
                permissionState.launchMultiplePermissionRequest()
            })
        }
    }
}

@Composable
fun AlbumCard(album: Album, onAlbumClick: (Album) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp)
            .clickable { onAlbumClick(album) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (val mediaItem = album.mediaItem) {
                is MediaItem.Video -> {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(mediaItem.path)
                            .decoderFactory(coil.decode.VideoFrameDecoder.Factory())
                            .build(),
                        contentDescription = "Video Frame",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is MediaItem.Image -> {
                    AsyncImage(
                        model = mediaItem.path,
                        contentDescription = "Album Cover",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Row(modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.BottomCenter)) {
                Text(text = album.name, modifier = Modifier.weight(1f))
                Text(text = "${album.count}")
            }
        }
    }
}

@Composable
fun AlbumListItem(album: Album, onAlbumClick: (Album) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAlbumClick(album) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (val mediaItem = album.mediaItem) {
            is MediaItem.Video -> {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(mediaItem.path)
                        .decoderFactory(coil.decode.VideoFrameDecoder.Factory())
                        .build(),
                    contentDescription = "Album Cover",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }

            is MediaItem.Image -> {
                AsyncImage(
                    model = mediaItem.path,
                    contentDescription = "Album Cover",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = album.name,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${album.count} items",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun PermissionRequiredDialog(onGrantPermission: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 4.dp) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Permission Required",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "This app requires access to your media files to display albums and media items. Please grant the required permissions.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onGrantPermission, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Grant Permission")
                }
            }
        }
    }
}
