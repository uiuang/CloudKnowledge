package com.uiuang.mvvm.base

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.IntentFilter
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import androidx.lifecycle.ProcessLifecycleOwner
import com.uiuang.mvvm.ext.lifecycle.KtxAppLifeObserver
import com.uiuang.mvvm.network.manager.NetworkStateReceive
import com.uiuang.mvvm.ext.lifecycle.KtxLifeCycleCallBack


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/8/1 18:14
 */

val appContext: Application by lazy { Ktx.app }

class Ktx : ContentProvider() {
    companion object {
        lateinit var app: Application
        private var networkStateReceiver: NetworkStateReceive? = null
        var watchActivityLife = true
        var watchAppLife = true

    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? =null

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun onCreate(): Boolean {
        var application = context!!.applicationContext as Application
        install(application)
        return true
    }

    private fun install(application: Application) {
        app = application
        networkStateReceiver = NetworkStateReceive()
        var intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        app.registerReceiver(networkStateReceiver, intentFilter)
        if (watchActivityLife) application.registerActivityLifecycleCallbacks(KtxLifeCycleCallBack())
        if (watchAppLife) ProcessLifecycleOwner.get().lifecycle.addObserver(KtxAppLifeObserver)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? = null

}