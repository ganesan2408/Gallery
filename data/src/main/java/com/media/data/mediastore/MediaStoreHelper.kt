package com.media.data.mediastore

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri

object MediaStoreHelper {
    fun queryContentResolver(
        contentResolver: ContentResolver,
        uri: Uri,
        projection: Array<String>,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
    }
}
