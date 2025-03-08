package com.media.data.repository

import com.media.data.mediastore.MediaStoreAlbumFetcher
import com.media.data.mediastore.MediaStoreMediaFetcher
import com.media.domain.model.Album
import com.media.domain.model.MediaItem
import com.media.domain.repository.AlbumRepository
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumFetcher: MediaStoreAlbumFetcher,
    private val mediaFetcher: MediaStoreMediaFetcher
) : AlbumRepository {

    override suspend fun getAlbums(): List<Album> {
        return albumFetcher.getAlbums()
    }

    override suspend fun fetchMediaItemsForAlbum(albumName: String): List<MediaItem> {
        return mediaFetcher.fetchMediaForAlbum(albumName)
    }
}