package com.media.data.mediastore


import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import com.media.domain.model.MediaItem
import com.media.domain.model.Album
import javax.inject.Inject

class MediaStoreAlbumFetcher @Inject constructor(private val context: Context) {

    suspend fun getAlbums(): List<Album> {
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

                if (mediaPath != null) {
                    val mediaItem =
                        if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) MediaItem.Video(
                            mediaPath
                        )
                        else MediaItem.Image(mediaPath)

                    val currentCount = albums[bucketName]?.third ?: 0
                    albums[bucketName] = Triple(mediaPath, mediaItem, currentCount + 1)
                }
            }
        }

        return albums.map { (name, value) ->
            Album(name = name, mediaItem = value.second, count = value.third)
        }
    }
}
