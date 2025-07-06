package org.yama.musicplayer.project

import platform.MediaPlayer.MPMediaQuery
import platform.MediaPlayer.MPMediaPropertyTitle
import platform.MediaPlayer.MPMediaPropertyArtist

actual suspend fun loadMusic(): List<Song> {
    val query = MPMediaQuery.songsQuery()
    val songs = query.items?.mapNotNull {
        val title = it.valueForProperty(MPMediaPropertyTitle) as? String ?: "Unknown Title"
        val artist = it.valueFor_property(MPMediaPropertyArtist) as? String ?: "Unknown Artist"
        Song(title, artist)
    }
    return songs ?: emptyList()
}