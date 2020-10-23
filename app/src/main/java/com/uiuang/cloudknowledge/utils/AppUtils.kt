package com.uiuang.cloudknowledge.utils

import android.content.pm.PackageManager
import com.uiuang.mvvm.base.appContext

object AppUtils {

    /**
     * Return the application's version name.
     *
     * @param packageName The name of the package.
     * @return the application's version name
     */
    fun getAppVersionName(packageName: String?): String? {
        return if (packageName.isNullOrEmpty()) "" else try {
            val pm: PackageManager = appContext.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }
}



