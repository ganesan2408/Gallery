package com.media.domain.model

sealed class MediaItem {
    data class Image(val path: String?) : MediaItem()
    data class Video(val path: String?) : MediaItem()
}