package com.dkproject.data.Repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import com.dkproject.domain.repository.ImageRepository
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okio.FileNotFoundException
import java.io.ByteArrayOutputStream
import java.net.URI
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    @ApplicationContext private val context: Context
) : ImageRepository {
    override suspend fun uploadProfileImage(uid: String, photoUri: String): String {
        return try {
            val photoString = withContext(context = Dispatchers.IO) {
                val uri = photoUri.toUri()
                val bitmap = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                } ?: throw FileNotFoundException()

                val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Bitmap.CompressFormat.WEBP_LOSSY
                } else {
                    Bitmap.CompressFormat.WEBP
                }

                val imageData = ByteArrayOutputStream().use { outputStream ->
                    val quality = 80 // 품질 80% (조정 가능)
                    bitmap.compress(format, quality, outputStream)
                    outputStream.toByteArray()
                }

                val imageRef = storage.reference.child("profile_images/$uid")
                imageRef.putBytes(imageData).await()
                imageRef.downloadUrl.await().toString()
            }
            photoString
        } catch (e: Exception) {
            throw e
        }
    }
}