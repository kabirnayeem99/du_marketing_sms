package io.github.kabirnayeem99.dumarketingadmin.common.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants.IMAGE_QUALITY_LESSEN_PERCENT
import timber.log.Timber
import java.io.ByteArrayOutputStream

object AssetUtilities {


    /**
     * This method takes a bitmap and transforms it into
     * a jpeg image with the reduced quality
     * to reduce the size of it.
     *
     * @param bitmap of [Bitmap] type
     *
     * @return [ByteArray]
     */
    fun bitmapToJpeg(bitmap: Bitmap): ByteArray {

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            IMAGE_QUALITY_LESSEN_PERCENT,
            byteArrayOutputStream
        )

        return byteArrayOutputStream.toByteArray()
    }

    /**
     * Gets a uri and turns it into a bitmap image
     *
     * @param dataUri, which is a [Uri]
     * @param contentResolver, which is a [ContentResolver]
     *
     * @return nullable [Bitmap]
     */
    fun dataUriToBitmap(dataUri: Uri?, contentResolver: ContentResolver): Bitmap? {
        try {
            if (dataUri == null) {
                Timber.e("dataUriToBitmap: the uri is null")
                return null
            }

            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {

                    // if this is something new like, Android Pie, use this new method
                    val source = ImageDecoder.createSource(contentResolver, dataUri)
                    ImageDecoder.decodeBitmap(source)
                }
                else -> {
                    // if it is older than Android Pie, than use deprecated getBitmap method
                    MediaStore.Images.Media.getBitmap(contentResolver, dataUri)

                }
            }
        } catch (e: Exception) {
            Timber.e(e, "dataUriToBitmap: $e")
            return null
        }
    }

    private const val TAG = "AssetUtilities"
}