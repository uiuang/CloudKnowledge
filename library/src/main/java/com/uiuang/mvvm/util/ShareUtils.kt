package com.uiuang.mvvm.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.uiuang.mvvm.R
import java.io.File


/**
 * @Title:
 * @Description:
 * @author zsc
 * @date 2020/9/3 21:14
 */
object ShareUtils {
    fun share(context: Context, stringRes: Int) {
        share(
            context,
            context.getString(stringRes)
        )
    }

    fun shareImage(context: Context, uri: Uri?, title: String?) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/jpeg"
        context.startActivity(Intent.createChooser(shareIntent, title))
    }


    fun share(context: Context, extraText: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.action_share))
        intent.putExtra(Intent.EXTRA_TEXT, extraText)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.action_share)))
    }

    /**
     * 分享网络图片
     */
    fun shareNetImage(context: Activity, url: String?) {
        Glide.with(context).asBitmap().load(url).into(object : SimpleTarget<Bitmap?>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                //由文件得到uri
                val imageUri = Uri.parse(
                    MediaStore.Images.Media.insertImage(
                        context.contentResolver,
                        resource,
                        null,
                        null
                    )
                )
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                shareIntent.type = "image/*"
                context.startActivity(Intent.createChooser(shareIntent, "分享到"))
            }
        })
    }

    fun getUriWithPath(
        context: Context,
        filepath: String?,
        authority: String
    ): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0以上的读取文件uri要用这种方式了 注意在manifests里注册
            FileProvider.getUriForFile(
                context.applicationContext,
                "${authority}.fileprovider",
                File(filepath)
            )
        } else {
            Uri.fromFile(File(filepath))
        }
    }

}