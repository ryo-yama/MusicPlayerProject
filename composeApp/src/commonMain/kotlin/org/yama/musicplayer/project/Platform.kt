package org.yama.musicplayer.project

interface Platform {
    val name: String
    val applicationContext: Any? // Add this line
}

expect fun getPlatform(): Platform