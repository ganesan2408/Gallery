package com.media.presentation.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.media.presentation.ui.Screen
import com.media.presentation.ui.album.AlbumsScreen
import com.media.presentation.ui.albumdetail.AlbumDetailScreen
import com.media.presentation.viewmodel.GalleryViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun GalleryAppHost(viewModel: GalleryViewModel, onFinish: () -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Albums.route) {
        composable(Screen.Albums.route) {
            val albumsWithMedia = viewModel.albumsWithMedia.collectAsState().value
            AlbumsScreen(
                albums = albumsWithMedia,
                onAlbumClick = { album ->
                    viewModel.onAlbumClick(album.name)
                    navController.navigate(Screen.AlbumDetail.route)
                },
                onBackClick = {
                    if (!navController.popBackStack()) {
                        onFinish()
                    }
                },
                reloadAlbums = {
                    viewModel.loadAlbumsWithMedia()
                }
            )
        }

        composable(Screen.AlbumDetail.route) {
            val selectedAlbumMedia = viewModel.selectedAlbumMedia.collectAsState().value

            if (selectedAlbumMedia != null) {
                AlbumDetailScreen(
                    albumPair = selectedAlbumMedia,
                    onMediaClick = { mediaItem ->
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
