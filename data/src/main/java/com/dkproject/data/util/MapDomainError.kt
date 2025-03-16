package com.dkproject.data.util

import com.dkproject.domain.Error.DomainError
import com.google.firebase.firestore.FirebaseFirestoreException
import retrofit2.HttpException
import java.io.IOException

fun <T>Result<T>.mapDomainError(): Result<T> =
    recoverCatching { throwable ->
        throw when(throwable) {
            is IOException -> DomainError.NetworkError
            is FirebaseFirestoreException -> {
                when(throwable.code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED -> DomainError.PermissionDenied
                    FirebaseFirestoreException.Code.NOT_FOUND -> DomainError.DocumentNotFound
                    else -> DomainError.UnknownError
                }
            }
            is NoSuchElementException -> DomainError.DocumentNotFound
            is HttpException -> DomainError.HttpError(code = throwable.code())
            else -> DomainError.UnknownError
        }
    }