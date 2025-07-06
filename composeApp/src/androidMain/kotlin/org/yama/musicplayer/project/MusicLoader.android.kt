package org.yama.musicplayer.project

import android.content.ContentUris
import android.provider.MediaStore
import android.net.Uri
import android.os.Environment
import java.io.File

actual suspend fun loadMusic(): List<Song> {
    val songs = mutableListOf<Song>()
    val applicationContext = getPlatform().applicationContext as? android.content.Context ?: return emptyList()

    // Set this to true to enable direct folder scanning
    val enableDirectFolderScan = true // User requested to set this to true for demonstration

    if (enableDirectFolderScan) {
        // Direct folder scanning logic
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)

        fun scanDirectory(directory: File) {
            directory.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    scanDirectory(file) // Recursively scan subdirectories
                } else if (file.isFile && file.extension.lowercase() in listOf("mp3", "wav", "aac", "flac", "m4a", "ogg")) {
                    // For direct file paths, we'll use the file path as the URI
                    songs.add(Song(file.nameWithoutExtension, "Local File", file.absolutePath))
                }
            }
        }

        if (downloadDir != null && downloadDir.exists()) {
            scanDirectory(downloadDir)
        }
        if (musicDir != null && musicDir.exists()) {
            scanDirectory(musicDir)
        }

    } else {
        // MediaStore scanning logic (existing)
        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        applicationContext.contentResolver.query(
            collection,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                songs.add(Song(title, artist, contentUri.toString()))
            }
        }
    }
    return songs
}