package com.media.data.mediastore


import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import com.media.domain.model.MediaItem
import javax.inject.Inject

class MediaStoreMediaFetcher @Inject constructor(private val context: Context) {

    suspend fun fetchMediaForAlbum(albumName: String): List<MediaItem> {
        val mediaItems = mutableListOf<MediaItem>()
        val contentResolver: ContentResolver = context.contentResolver
        val isAllMedia = albumName == AlbumConstants.ALL_IMAGES || albumName == AlbumConstants.ALL_VIDEOS

        val selection = when (albumName) {
            AlbumConstants.ALL_IMAGES, AlbumConstants.ALL_VIDEOS -> "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
            else -> "${MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME} = ? AND ${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (?, ?)"
        }
        val selectionArgs = when (albumName) {
            AlbumConstants.ALL_IMAGES -> arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
            AlbumConstants.ALL_VIDEOS -> arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
            else -> arrayOf(
                albumName,
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )
        }

        MediaStoreHelper.queryContentResolver(
            contentResolver, MediaStore.Files.getContentUri("external"), arrayOf(
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MEDIA_TYPE
            ), selection, selectionArgs, null
        )?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val mediaTypeColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)

            while (cursor.moveToNext()) {
                val mediaPath = cursor.getString(dataColumn)
                val mediaType = cursor.getInt(mediaTypeColumn)

                if (mediaPath != null) {
                    val mediaItem =
                        if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) MediaItem.Video(
                            mediaPath
                        )
                        else MediaItem.Image(mediaPath)
                    mediaItems.add(mediaItem)
                }
            }
        }

        return mediaItems
    }
}
