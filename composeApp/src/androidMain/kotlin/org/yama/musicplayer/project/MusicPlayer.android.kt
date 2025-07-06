package org.yama.musicplayer.project

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

actual class MusicPlayer actual constructor(private val platform: Platform<Any?>) {
    private var mediaPlayer: MediaPlayer? = null

    actual fun play(song: Song) {
        mediaPlayer?.stop()
        val context = platform.applicationContext as? Context
        if (context != null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(song.uri))
            mediaPlayer?.start()
        }
    }

    actual fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }
    }

    actual fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    actual fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}