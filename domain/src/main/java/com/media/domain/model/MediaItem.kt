package com.media.domain.model

sealed class MediaItem {
    data class Image(val path: String?) : MediaItem()
    data class Video(val path: String?) : MediaItem()

    fun getMediaPath(): String? {
        return when (this) {
            is Image -> this.path
            is Video -> this.path
        }
    }
}