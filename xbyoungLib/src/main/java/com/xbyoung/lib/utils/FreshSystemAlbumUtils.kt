package com.xbyoung.lib.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import java.io.*
import java.lang.ref.WeakReference


/**
 * Created by Administrator on 2018/5/30.
 *
 *
 */
object FreshSystemAlbumUtils {
    @JvmStatic
    fun refreshLocalAlbum(context: WeakReference<Context>, file: File) {
        Log.d("file_", "file    ${file.exists()}   ${file.absolutePath}")
        context.get()?.let { mContext ->
            val mimeType = when (file.name.substringAfterLast(".")) {
                "jpg" -> "image/jpeg"
                "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                "mp4" -> "video/mp4"
                "mkv" -> "video/mp4"
                else -> "video/mp4"
            }
            val targetUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                var publicPath = Environment.DIRECTORY_DCIM + File.separator + "Camera"
                insertFileIntoMediaStore(mContext, file.name, mimeType, publicPath)

            } else {
                val targetFile = getFileCache(file.name, mimeType)
                var uri: Uri? = null
                targetFile?.let {
                    uri = Uri.fromFile(targetFile)
                }
                uri
            }

            targetUri?.let { uri ->
                try {
                    copyFile(mContext, file.absolutePath, uri)
                    file.delete()
                    val localIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
                    mContext.sendBroadcast(localIntent)

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }



    /**
     * AndroidQ以上保存图片到公共目录
     *
     */
    private fun insertFileIntoMediaStore(
        context: Context,
        name: String,
        type: String,
        relativePath: String
    ): Uri? {
        return if (type.contains("mp4")) {
            insertVideo(context, name, type, relativePath)
        } else {
            insertImage(context, name, type, relativePath)
        }

    }

    private fun insertImage(
        context: Context,
        imageName: String,
        imageType: String,
        relativePath: String
    ): Uri? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return null
        }
        if (TextUtils.isEmpty(relativePath)) {
            return null
        }
        var insertUri: Uri? = null
        val resolver: ContentResolver = context.contentResolver
        //设置文件参数到ContentValues中
        val values = ContentValues()
        //设置文件名
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
        //设置文件描述，这里以文件名代替
        values.put(MediaStore.Images.Media.DESCRIPTION, imageName)
        //设置文件类型为image/*
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/$imageType")
        //注意：MediaStore.Images.Media.RELATIVE_PATH需要targetSdkVersion=29,
        //故该方法只可在Android10的手机上执行
        values.put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
        //EXTERNAL_CONTENT_URI代表外部存储器
        val external: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        //insertUri表示文件保存的uri路径
        insertUri = resolver.insert(external, values)
        return insertUri
    }

    private fun insertVideo(
        context: Context,
        name: String,
        type: String,
        relativePath: String
    ): Uri? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return null
        }
        if (TextUtils.isEmpty(relativePath)) {
            return null
        }
        var insertUri: Uri? = null
        val resolver: ContentResolver = context.contentResolver
        //设置文件参数到ContentValues中
        val values = ContentValues()
        //设置文件名
        values.put(MediaStore.Video.Media.DISPLAY_NAME, name)
        //设置文件描述，这里以文件名代替
        values.put(MediaStore.Video.Media.DESCRIPTION, name)
        //设置文件类型为image/*
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/$type")
        //注意：MediaStore.Images.Media.RELATIVE_PATH需要targetSdkVersion=29,
        //故该方法只可在Android10的手机上执行
        values.put(MediaStore.Video.Media.RELATIVE_PATH, relativePath)
        //EXTERNAL_CONTENT_URI代表外部存储器
        val external: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        //insertUri表示文件保存的uri路径
        insertUri = resolver.insert(external, values)
        return insertUri
    }

    private fun copyFile(context: Context, sourceFilePath: String?, insertUri: Uri?): Boolean {
        if (insertUri == null) {
            return false
        }
        val resolver: ContentResolver = context.contentResolver
        var `is`: InputStream? = null //输入流
        var os: OutputStream? = null //输出流
        return try {
            os = resolver.openOutputStream(insertUri)
            if (os == null) {
                return false
            }
            val sourceFile = File(sourceFilePath)
            if (sourceFile.exists()) { // 文件存在时
                `is` = FileInputStream(sourceFile) // 读入原文件
                //输入流读取文件，输出流写入指定目录
                return copyFileWithStream(os, `is`)
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            try {
                `is`?.close()
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    private fun copyFileWithStream(os: OutputStream?, `is`: InputStream?): Boolean {
        if (os == null || `is` == null) {
            return false
        }
        var read = 0
        while (true) {
            return try {
                val buffer = ByteArray(1444)
                while (`is`.read(buffer).also { read = it } != -1) {
                    os.write(buffer, 0, read)
                    os.flush()
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            } finally {
                try {
                    os.close()
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * < Q
     */
    private fun getFileCache(fileName: String, type: String): File? {

        //创建项目图片公共缓存目录
        val file = File(
            Environment.getExternalStorageDirectory().toString() +
                    File.separator +
                    Environment.DIRECTORY_DCIM +
                    File.separator +
                    "Camera"

        )
        if (!file.exists()) {
            file.mkdirs()
        }
        //创建对应图片的缓存路径
        return File(file.toString() + File.separator + fileName)
    }
}