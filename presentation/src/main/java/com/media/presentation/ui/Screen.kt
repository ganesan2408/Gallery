package com.media.presentation.ui

sealed class Screen(val route: String) {
    data object Albums : Screen("albums")
    data object AlbumDetail : Screen("albumDetail")
    data object MediaViewer : Screen("mediaViewer")
}