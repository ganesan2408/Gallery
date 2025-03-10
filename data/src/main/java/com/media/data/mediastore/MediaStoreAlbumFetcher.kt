package com.media.data.mediastore


import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import com.media.domain.model.MediaItem
import com.media.domain.model.Album
import javax.inject.Inject

class MediaStoreAlbumFetcher @Inject constructor(private val context: Context) {

    suspend fun getAlbums(): List<Album> {
        var allImagesCount = 0
        var allVideosCount = 0
        var allImagesCover: String? = null
        var allVideosCover: String? = null

        val albums = mutableMapOf<String, Triple<String, MediaItem, Int>>()
        val contentResolver: ContentResolver = context.contentResolver

        MediaStoreHelper.queryContentResolver(
            contentResolver,
            MediaStore.Files.getContentUri("external"),
            arrayOf(
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MEDIA_TYPE
            ),
            "${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (?, ?)",
            arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            ),
            "${MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME} ASC"
        )?.use { cursor ->
            val bucketColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val mediaTypeColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)

            while (cursor.moveToNext()) {
                val bucketName = cursor.getString(bucketColumn) ?: "Unknown"
                val mediaPath = cursor.getString(dataColumn)
                val mediaType = cursor.getInt(mediaTypeColumn)
                val mediaItem: MediaItem? = when (mediaType) {
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> {
                        allVideosCount++
                        if (allVideosCover == null) allVideosCover = mediaPath
                        MediaItem.Video(mediaPath)
                    }

                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> {
                        allImagesCount++
                        if (allImagesCover == null) allImagesCover = mediaPath
                        MediaItem.Image(mediaPath)
                    }

                    else -> {
                        null
                    }
                }

                if (mediaItem != null) {
                    val currentCount = albums[bucketName]?.third ?: 0
                    albums[bucketName] = Triple(mediaPath, mediaItem, currentCount + 1)
                }
            }
        }
        val albumsList = albums.map { (name, value) ->
            Album(name = name, mediaItem = value.second, count = value.third)
        }
        val allMedias = arrayListOf(
            Album(
                name = AlbumConstants.ALL_IMAGES,
                mediaItem = MediaItem.Image(allImagesCover),
                count = allImagesCount
            ),
            Album(
                name = AlbumConstants.ALL_VIDEOS,
                mediaItem = MediaItem.Image(allVideosCover),
                count = allVideosCount
            )
        )
        return allMedias + albumsList
    }
}
