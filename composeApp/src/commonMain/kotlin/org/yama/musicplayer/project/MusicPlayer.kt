package org.yama.musicplayer.project

expect class MusicPlayer(platform: Platform<out Any?>) {
    fun play(song: Song)
    fun pause()
    fun stop()
    fun isPlaying(): Boolean
}