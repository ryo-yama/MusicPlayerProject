package org.yama.musicplayer.project

interface Platform<out T> {
    val name: String
    val applicationContext: T
    fun getAppVersion(): String
}

expect fun getPlatform(): Platform<Any?>