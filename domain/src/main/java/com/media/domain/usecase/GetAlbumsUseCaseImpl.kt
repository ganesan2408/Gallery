package com.media.domain.usecase

import com.media.domain.model.Album
import com.media.domain.model.MediaItem
import com.media.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumsUseCaseImpl @Inject constructor(
    private val repository: AlbumRepository
) : GetAlbumsUseCase {
    override suspend fun getAlbums(): List<Album> {
        return repository.getAlbums()
    }

    override suspend fun fetchMediaItemsForAlbum(albumName: String): List<MediaItem> {
        return repository.fetchMediaItemsForAlbum(albumName)
    }
}
