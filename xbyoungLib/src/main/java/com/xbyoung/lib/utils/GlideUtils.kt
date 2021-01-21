package com.xbyoung.lib.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import jp.wasabeef.glide.transformations.ColorFilterTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


/**
 * Created by Administrator on 2018/2/28.
 */
open class GlideUtils {
    companion object {

        @JvmStatic
        fun loadNormalImage(activity: Activity, url: String, imageView: ImageView) {
            if (activity.isDestroyed) return
            GlideApp.with(activity).asBitmap()
                .load(url)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)

        }

        @JvmStatic
        fun loadNormalImage(activity: Context, url: Any, normalRes: Int, imageView: ImageView) {
            GlideApp.with(activity)
                .asBitmap()
                .load(url)
                .centerCrop()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(normalRes)
                .placeholder(normalRes)
                .into(imageView)
        }

        @JvmStatic
        fun loadNormalImage(
            activity: Context,
            url: Any,
            normalRes: Int,
            width: Int,
            height: Int,
            imageView: ImageView
        ) {
            GlideApp.with(activity)
                .asBitmap()
                .load(url)
                .override(width, height)
               // .centerInside()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(normalRes)
                .placeholder(normalRes)
                .into(imageView)
        }

        @JvmStatic
        fun loadGif(activity: Activity, normalRes: Int, imageView: ImageView) {
            if (activity.isDestroyed) return
            GlideApp.with(activity).asGif()
                .load(normalRes)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)
        }

        @JvmStatic
        fun loadNormalImage(activity: Context, res: Int, normalRes: Int, imageView: ImageView) {
            GlideApp.with(activity).asBitmap()
                .load(res)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(normalRes)
                .placeholder(normalRes)
                .into(imageView)

        }

        @JvmStatic
        fun loadRoundImage(
            activity: Context,
            imageView: ImageView,
            res: Any,
            normalRes: Int,
            cropWidth: Int,
            mask: Int = Color.TRANSPARENT
        ) {
            val multi = MultiTransformation<Bitmap>(
                CropTransformation(cropWidth, cropWidth),
                RoundedCornersTransformation(5, 0),
                ColorFilterTransformation(mask)
            )
            GlideApp.with(activity)
                .asBitmap()
                .load(res)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(normalRes)
                .placeholder(normalRes)
                .apply(RequestOptions.bitmapTransform(multi))
                .into(imageView)
        }


        @JvmStatic
        fun loadRoundImage(
            requests: GlideRequests,
            imageView: ImageView,
            width: Int,
            height: Int,
            res: Any,
            normalRes: Int,
            radius: Int
        ) {
            LogUtils.d("loadRoundImage    $width  $height")
            val multi = MultiTransformation<Bitmap>(
                CropTransformation(width, height),
                RoundedCornersTransformation(radius, 0)
            )
            requests
                .asBitmap()
                .load(res)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(normalRes)
                .placeholder(normalRes)
                .apply(RequestOptions.bitmapTransform(multi))
                .thumbnail(loadTransform(imageView.context, width, height, normalRes, radius))
                .into(imageView)

        }

        private fun loadTransform(
            context: Context,
            width: Int,
            height: Int,
            @DrawableRes placeholderId: Int,
            radius: Int
        ): RequestBuilder<Bitmap> {
            val multi = MultiTransformation<Bitmap>(
                CropTransformation(width, height),
                RoundedCornersTransformation(radius, 0)
            )
            return Glide.with(context)
                .asBitmap()
                .load(placeholderId)
                .apply(RequestOptions.bitmapTransform(multi))


        }

        @JvmStatic
        fun loadRoundImage(
            activity: Context,
            imageView: ImageView,
            res: Any,
            normalRes: Int,
            cropWidth: Int,
            cropHeight: Int,
            mask: Int = Color.TRANSPARENT,
            corners: Int
        ) {
            val multi = MultiTransformation<Bitmap>(
                CropTransformation(cropWidth, cropHeight),
                RoundedCornersTransformation(corners, 0),
                ColorFilterTransformation(mask)
            )
            GlideApp.with(activity)
                .asBitmap()
                .load(res)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(normalRes)
                .placeholder(imageView.drawable ?: activity.getDrawable(normalRes))
                .thumbnail(
                    loadTransform(
                        imageView.context,
                        cropWidth,
                        cropHeight,
                        normalRes,
                        corners
                    )
                )
                .apply(RequestOptions.bitmapTransform(multi))
                .into(imageView)
        }


    }


}