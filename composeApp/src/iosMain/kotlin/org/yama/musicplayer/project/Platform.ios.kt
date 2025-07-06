package org.yama.musicplayer.project

import platform.UIKit.UIDevice

class IOSPlatform: Platform<Any?> {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val applicationContext: Any? = null

    override fun getAppVersion(): String {
        val bundle = NSBundle.mainBundle
        return bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "Unknown"
    }
}

actual fun getPlatform(): Platform<Any?> = IOSPlatform()