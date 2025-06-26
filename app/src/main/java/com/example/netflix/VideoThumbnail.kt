// VideoThumbnail.kt
package com.example.netflix

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.core.net.toUri

fun getVideoFrame(context: Context, resId: Int): Bitmap? {
    //forma la miniaturas de los videos
    val retriever = MediaMetadataRetriever()
    return try {
        val uri = "android.resource://${context.packageName}/$resId".toUri()
        retriever.setDataSource(context, uri)
        retriever.getFrameAtTime(1_000_000, MediaMetadataRetriever.OPTION_CLOSEST) // 1s
    } catch (e: Exception) {
        null
    } finally {
        retriever.release()
    }
}
