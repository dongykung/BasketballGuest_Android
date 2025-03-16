package com.dkproject.presentation.util

import android.content.Context
import com.dkproject.domain.Error.DomainError
import com.dkproject.presentation.R

object ErrorUtil {
    fun getErrorString(e: Throwable, context: Context): String {
        return when(e) {
            is DomainError.NetworkError -> context.getString(R.string.networkerror)
            is DomainError.PermissionDenied -> context.getString(R.string.notpermission)
            is DomainError.DocumentNotFound -> context.getString(R.string.nosuch)
            is DomainError.UnknownError -> context.getString(R.string.defaulterror)
            is DomainError.HttpError -> context.getString(R.string.httperror)
            else -> context.getString(R.string.defaulterror)
        }
    }
}