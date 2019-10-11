package com.ants.driverpartner.everywhere.uitl


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import androidx.annotation.NonNull

import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource

import java.security.MessageDigest
import com.bumptech.glide.load.resource.bitmap.BitmapResource.obtain as obtain1

class GlideCircleTransformation(mBitmapPool: BitmapPool) : Transformation<Any> {
    val mBitmapPool = mBitmapPool

//    override fun transform(
//        context: Context,
//        resource: Nothing,
//        outWidth: Int,
//        outHeight: Int
//    ): Resource<out Any> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    constructor(context: Context) : this(Glide.get(context).bitmapPool) {}

    @NonNull
    override fun transform(
        context: Context,
        resource: Resource<Any>,
        outWidth: Int,
        outHeight: Int
    ): BitmapResource? {
        val source = resource.get() as Bitmap
        val size = Math.min(source.width, source.height)

        val width = (source.width - size) / 2
        val height = (source.height - size) / 2

        var bitmap: Bitmap? = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        val paint = Paint()
        val shader = BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP)
        if (width != 0 || height != 0) {
            // source isn't square, move viewport to center
            val matrix = Matrix()
            matrix.setTranslate((-width).toFloat(), (-height).toFloat())
            shader.setLocalMatrix(matrix)
        }
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)


        return obtain1(bitmap, mBitmapPool)
    }

    override fun updateDiskCacheKey(@NonNull messageDigest: MessageDigest) {

    }
}