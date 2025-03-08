package com.media.presentation.ui

sealed class Screen(val route: String) {
    data object Albums : Screen("albums")
}