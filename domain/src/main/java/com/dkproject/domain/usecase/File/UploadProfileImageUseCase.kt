package com.dkproject.domain.usecase.File

import com.dkproject.domain.repository.ImageRepository

class UploadProfileImageUseCase(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(uid: String, photoUri: String): String {
        return imageRepository.uploadProfileImage(uid = uid, photoUri = photoUri)
    }
}