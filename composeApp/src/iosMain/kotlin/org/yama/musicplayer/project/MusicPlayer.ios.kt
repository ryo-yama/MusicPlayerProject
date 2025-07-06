package org.yama.musicplayer.project

import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.Foundation.NSURL

actual class MusicPlayer(private val platform: Platform) { // Add platform to constructor
    private var player: AVPlayer? = null

    actual fun play(song: Song) {
        // In a real app, you'd get the song's URL from MPMediaLibrary
        // and use it to create the AVPlayerItem.
        // For now, we'll just log that we're "playing".
        println("Playing ${song.title}")
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
}