package com.uiuang.cloudknowledge.utils

import android.Manifest.permission
import android.content.Context
import android.os.Vibrator
import androidx.annotation.RequiresPermission
import com.uiuang.mvvm.base.appContext

object VibrateUtils {
    private var vibrator: Vibrator? = null
    /**
     * Vibrate.
     *
     * Must hold `<uses-permission android:name="android.permission.VIBRATE" />`
     *
     * @param milliseconds The number of milliseconds to vibrate.
     */
    @RequiresPermission(permission.VIBRATE)
    fun vibrate(milliseconds: Long) {
        val vibrator = getVibrator() ?: return
        vibrator.vibrate(milliseconds)
    }

    /**
     * Vibrate.
     *
     * Must hold `<uses-permission android:name="android.permission.VIBRATE" />`
     *
     * @param pattern An array of longs of times for which to turn the vibrator on or off.
     * @param repeat  The index into pattern at which to repeat, or -1 if you don't want to repeat.
     */
    @RequiresPermission(permission.VIBRATE)
    fun vibrate(pattern: LongArray?, repeat: Int) {
        val vibrator = getVibrator() ?: return
        vibrator.vibrate(pattern, repeat)
    }

    /**
     * Cancel vibrate.
     *
     * Must hold `<uses-permission android:name="android.permission.VIBRATE" />`
     */
    @RequiresPermission(permission.VIBRATE)
    fun cancel() {
        val vibrator = getVibrator() ?: return
        vibrator.cancel()
    }
    private fun getVibrator(): Vibrator? {
        if (vibrator == null) {
            vibrator = appContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        return vibrator
    }
}