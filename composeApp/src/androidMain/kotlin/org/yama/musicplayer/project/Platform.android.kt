package org.yama.musicplayer.project

import android.os.Build
import android.content.Context

class AndroidPlatform(private val context: Context) : Platform<Context> {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val applicationContext: Context = context

    override fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName.toString()
        } catch (e: Exception) {
            "Unknown"
        }
    }
}

actual fun getPlatform(): Platform<Any?> = AndroidPlatform(AndroidContext.applicationContext)