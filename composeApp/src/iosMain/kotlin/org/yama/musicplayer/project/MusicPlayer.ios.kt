package org.yama.musicplayer.project

import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.Foundation.NSURL
import platform.MediaPlayer.MPMediaLibrary
import platform.MediaPlayer.MPMediaPropertyPersistentID
import platform.MediaPlayer.MPMediaQuery

actual class MusicPlayer actual constructor(private val platform: Platform<out Any?>) {
    private var player: AVPlayer? = null

    actual fun play(song: Song) {
        player?.pause()
        player = null

        // Use MPMediaLibrary to get the actual asset URL from persistent ID
        val query = MPMediaQuery.songsQuery()
        query.addFilterPredicate(MPMediaPropertyPersistentID.predicateForProperty(MPMediaPropertyPersistentID, song.uri.toLong()))
        val mediaItem = query.items?.firstOrNull()

        if (mediaItem != null && mediaItem.assetURL != null) {
            player = AVPlayer(playerItem = AVPlayerItem(uRL = mediaItem.assetURL!!))
            player?.play()
            println("Playing ${song.title} from iOS Media Library")
        } else {
            println("Error: Could not find media item or asset URL for ${song.title}")
        }
    }

    actual fun pause() {
        player?.pause()
        println("Pausing")
    }

    actual fun stop() {
        player?.pause()
        player = null
        println("Stopping")
    }

    actual fun isPlaying(): Boolean {
        return player?.rate ?: 0.0f > 0.0f
    }
}