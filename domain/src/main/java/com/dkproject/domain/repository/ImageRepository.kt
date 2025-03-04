package com.dkproject.domain.repository

import com.sun.jndi.toolkit.url.Uri

interface ImageRepository {
    suspend fun uploadProfileImage(uid: String, photoUri: String): String
}