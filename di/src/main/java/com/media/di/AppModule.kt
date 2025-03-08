package com.media.di

import android.content.Context
import com.media.data.mediastore.MediaStoreAlbumFetcher
import com.media.data.mediastore.MediaStoreMediaFetcher
import com.media.data.repository.AlbumRepositoryImpl
import com.media.domain.repository.AlbumRepository
import com.media.domain.usecase.GetAlbumsUseCase
import com.media.domain.usecase.GetAlbumsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMediaStoreAlbumFetcher(@ApplicationContext context: Context): MediaStoreAlbumFetcher {
        return MediaStoreAlbumFetcher(context)
    }

    @Provides
    @Singleton
    fun provideMediaStoreMediaFetcher(@ApplicationContext context: Context): MediaStoreMediaFetcher {
        return MediaStoreMediaFetcher(context)
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(
        mediaStoreAlbumFetcher: MediaStoreAlbumFetcher,
        mediaStoreMediaFetcher: MediaStoreMediaFetcher
    ): AlbumRepository {
        return AlbumRepositoryImpl(mediaStoreAlbumFetcher, mediaStoreMediaFetcher)
    }

    @Provides
    @Singleton
    fun provideGetAlbumsUseCase(repository: AlbumRepository): GetAlbumsUseCase {
        return GetAlbumsUseCaseImpl(repository)
    }
}