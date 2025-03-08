package com.media.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.media.domain.model.Album
import com.media.domain.model.MediaItem
import com.media.domain.usecase.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    private val _albumsWithMedia = MutableStateFlow<List<Album>>(emptyList())
    val albumsWithMedia: StateFlow<List<Album>> = _albumsWithMedia

    private val _selectedAlbumMedia = MutableStateFlow<Pair<Album, List<MediaItem>>?>(null)
    val selectedAlbumMedia: StateFlow<Pair<Album, List<MediaItem>>?> = _selectedAlbumMedia

    init {
        loadAlbumsWithMedia()
    }

    fun loadAlbumsWithMedia() {
        viewModelScope.launch {
            val albums = getAlbumsUseCase.getAlbums()
            _albumsWithMedia.value = albums
        }
    }

    fun onAlbumClick(albumName: String) {
        viewModelScope.launch {
            val album = _albumsWithMedia.value.find { it.name == albumName }
            album?.let {
                val mediaItems = getAlbumsUseCase.fetchMediaItemsForAlbum(it.name)
                _selectedAlbumMedia.value = Pair(it, mediaItems)
            }
        }
    }
}
