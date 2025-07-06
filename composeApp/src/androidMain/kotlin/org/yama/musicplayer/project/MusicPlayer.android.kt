package org.yama.musicplayer.project

import android.media.MediaPlayer
import android.net.Uri

actual class MusicPlayer actual constructor(private val platform: Platform) {
    private var mediaPlayer: MediaPlayer? = null

    actual fun play(song: Song) {
        mediaPlayer?.release() // Release any previous player
        val context = platform.applicationContext as? android.content.Context
        if (context != null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(song.uri))
            mediaPlayer?.start()
            println("Playing ${song.title}")
        } else {
            println("Error: Android Context not available for playback.")
        }
    }

    actual fun pause() {
        mediaPlayer?.pause()
        println("Pausing")
    }

    actual fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        println("Stopping")
    }
}