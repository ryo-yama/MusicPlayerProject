package org.yama.musicplayer.project

import android.os.Build
import android.content.Context

class AndroidPlatform(private val context: Context) : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val applicationContext: Any? = context
}

actual fun getPlatform(): Platform = AndroidPlatform(AndroidContext.applicationContext)