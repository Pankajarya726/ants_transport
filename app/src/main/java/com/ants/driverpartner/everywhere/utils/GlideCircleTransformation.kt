//package com.ants.driverpartner.everywhere.uitl
//
//
//import android.content.Context
//import android.graphics.*
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.Transformation
//import com.bumptech.glide.load.engine.Resource
//import com.bumptech.glide.load.resource.bitmap.BitmapResource
//import java.security.MessageDigest
//
//class GlideCircleTransformation(ctx: Context?) :
//    Transformation<Bitmap> {
//
//    val mBitmapPool = ctx?.let { Glide.get(it).bitmapPool }
//
//
//    override fun transform(
//        context: Context,
//        resource: Resource<Bitmap>,
//        outWidth: Int,
//        outHeight: Int
//    ): Resource<Bitmap> {
//        val source = resource.get()
//        val size = Math.min(source.width, source.height)
//
//        val width = (source.width - size) / 2
//        val height = (source.height - size) / 2
//
//        var bitmap: Bitmap? = mBitmapPool?.get(size, size, Bitmap.Config.ARGB_8888)
//        if (bitmap == null) {
//            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
//        }
//
//        val canvas = Canvas(bitmap!!)
//        val paint = Paint()
//        val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//        if (width != 0 || height != 0) {
//            // source isn't square, move viewport to center
//            val matrix = Matrix()
//            matrix.setTranslate((-width).toFloat(), (-height).toFloat())
//            shader.setLocalMatrix(matrix)
//        }
//        paint.shader = shader
//        paint.isAntiAlias = true
//
//        val r = size / 2f
//        canvas.drawCircle(r, r, r, paint)
//
//        return mBitmapPool?.let { BitmapResource.obtain(bitmap, it) } as Resource<Bitmap>
//    }
//
//    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//}