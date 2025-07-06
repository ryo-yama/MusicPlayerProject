package org.yama.musicplayer.project

expect class MusicPlayer(platform: Platform) {
    fun play(song: Song)
    fun pause()
    fun stop()
}