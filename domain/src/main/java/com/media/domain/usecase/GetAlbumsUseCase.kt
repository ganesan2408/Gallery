package com.media.domain.usecase

import com.media.domain.model.Album
import com.media.domain.model.MediaItem

interface GetAlbumsUseCase {
    suspend fun getAlbums(): List<Album>
    suspend fun fetchMediaItemsForAlbum(albumName: String) : List<MediaItem>
}