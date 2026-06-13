package com.example.fotogram.util

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import java.io.ByteArrayOutputStream

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

private const val LIMITE_BASE64_IMMAGINE = 80_000

fun uriToBase64ConLimite(
    context: Context,
    uri: Uri
): String {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw Exception("Impossibile aprire l'immagine")

    val bitmapOriginale = BitmapFactory.decodeStream(inputStream)
    inputStream.close()

    if (bitmapOriginale == null) {
        throw Exception("Formato immagine non valido")
    }

    val dimensioniMassime = listOf(1000, 800, 600, 500, 400)
    val qualitaPossibili = listOf(80, 70, 60, 50, 40)

    for (dimensioneMassima in dimensioniMassime) {
        val bitmapRidimensionata = ridimensionaBitmap(
            bitmap = bitmapOriginale,
            dimensioneMassima = dimensioneMassima
        )

        for (qualita in qualitaPossibili) {
            val base64 = comprimiBitmapInBase64(
                bitmap = bitmapRidimensionata,
                qualita = qualita
            )

            if (base64.length <= LIMITE_BASE64_IMMAGINE) {
                Log.d("ImmagineUtils", "Immagine convertita: ${base64.length} caratteri, dimensione max $dimensioneMassima, qualità $qualita")
                return base64
            }
        }
    }

    throw Exception("Immagine troppo grande. Scegli un'immagine più piccola.")
}

private fun ridimensionaBitmap(
    bitmap: Bitmap,
    dimensioneMassima: Int
): Bitmap {
    val larghezza = bitmap.width
    val altezza = bitmap.height

    val latoMaggiore = maxOf(larghezza, altezza)

    if (latoMaggiore <= dimensioneMassima) {
        return bitmap
    }

    val scala = dimensioneMassima.toFloat() / latoMaggiore.toFloat()

    val nuovaLarghezza = (larghezza * scala).toInt()
    val nuovaAltezza = (altezza * scala).toInt()

    return Bitmap.createScaledBitmap(
        bitmap,
        nuovaLarghezza,
        nuovaAltezza,
        true
    )
}

private fun comprimiBitmapInBase64(
    bitmap: Bitmap,
    qualita: Int
): String {
    val outputStream = ByteArrayOutputStream()

    bitmap.compress(
        Bitmap.CompressFormat.JPEG,
        qualita,
        outputStream
    )

    return Base64.encodeToString(
        outputStream.toByteArray(),
        Base64.NO_WRAP
    )
}