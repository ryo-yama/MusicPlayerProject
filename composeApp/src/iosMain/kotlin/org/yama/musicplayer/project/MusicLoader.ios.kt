package org.yama.musicplayer.project

import platform.Foundation.NSURL
import platform.MediaPlayer.MPMediaItem
import platform.MediaPlayer.MPMediaLibrary
import platform.MediaPlayer.MPMediaQuery
import platform.MediaPlayer.MPMediaPropertyPersistentID
import platform.MediaPlayer.MPMediaPropertyTitle
import platform.MediaPlayer.MPMediaPropertyArtist
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual suspend fun loadMusic(): List<Song> = suspendCancellableCoroutine {
    MPMediaLibrary.requestAuthorization { status ->
        if (status == platform.MediaPlayer.MPMediaLibraryAuthorizationStatusAuthorized) {
            val songs = mutableListOf<Song>()
            val query = MPMediaQuery.songsQuery()
            query.items?.forEach { item ->
                val title = item.valueForProperty(MPMediaPropertyTitle) as? String ?: "Unknown Title"
                val artist = item.valueForProperty(MPMediaPropertyArtist) as? String ?: "Unknown Artist"
                val persistentID = item.valueForProperty(MPMediaPropertyPersistentID) as? Long ?: 0L
                songs.add(Song(title, artist, persistentID.toString())) // Use persistentID as URI for now
            }
            it.resume(songs)
        } else {
            // Handle unauthorized case, e.g., return empty list or show error
            println("MPMediaLibrary access denied: $status")
            it.resume(emptyList())
        }
    }
}