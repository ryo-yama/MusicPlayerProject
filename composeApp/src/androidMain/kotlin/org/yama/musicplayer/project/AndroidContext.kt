package org.yama.musicplayer.project

import android.content.Context

object AndroidContext {
    private var _applicationContext: Context? = null

    val applicationContext: Context
        get() = _applicationContext ?: error("Application context not initialized.")

    fun setApplicationContext(context: Context) {
        if (_applicationContext == null) {
            _applicationContext = context.applicationContext
        }
    }
}