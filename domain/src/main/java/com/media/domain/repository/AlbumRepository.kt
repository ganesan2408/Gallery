package com.media.domain.repository

import com.media.domain.model.Album
import com.media.domain.model.MediaItem

interface AlbumRepository {
    suspend fun getAlbums(): List<Album>
    suspend fun fetchMediaItemsForAlbum(albumName: String): List<MediaItem>
}