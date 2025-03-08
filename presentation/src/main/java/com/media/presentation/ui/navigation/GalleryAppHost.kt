package com.media.presentation.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.media.presentation.ui.Screen
import com.media.presentation.ui.album.AlbumsScreen
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
                onAlbumClick = { albumName ->
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
    }
}
