package com.dkproject.domain.Error

sealed class DomainError : Throwable() {
    // normal error
    data object UnknownError : DomainError() {
        private fun readResolve(): Any = UnknownError
    }

    data object NetworkError : DomainError() {
        private fun readResolve(): Any = NetworkError
    }

    // firebase error
    data object DocumentNotFound : DomainError() {
        private fun readResolve(): Any = DocumentNotFound
    }

    data object PermissionDenied : DomainError() {
        private fun readResolve(): Any = PermissionDenied
    }

    // server error
    data class HttpError(val code: Int) : DomainError()
}