package com.media.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.media.domain.model.Album
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

    init {
        loadAlbumsWithMedia()
    }

    fun loadAlbumsWithMedia() {
        viewModelScope.launch {
            val albums = getAlbumsUseCase.getAlbums()
            _albumsWithMedia.value = albums
        }
    }
}
