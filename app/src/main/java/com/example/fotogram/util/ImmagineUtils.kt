package com.example.fotogram.util

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun base64ToImageBitmap(base64: String?): ImageBitmap? {
    if (base64.isNullOrBlank()) {
        return null
    }

    return try {
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        bitmap?.asImageBitmap()
    } catch (errore: Exception) {
        null
    }
}