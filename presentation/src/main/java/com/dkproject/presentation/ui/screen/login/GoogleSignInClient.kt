package com.dkproject.presentation.ui.screen.login

import android.content.Context
import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetCustomCredentialOption
import com.dkproject.presentation.BuildConfig
import com.dkproject.presentation.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class GoogleSignInClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth
) {

    private val crdentialManager = CredentialManager.create(context = context)

    suspend fun signIn(): SignInResult {
        try {
            val result = buildCredentialRequest()
            return handleSignIn(result)
        } catch (e: Exception) {
            return SignInResult.Failure(e.message.toString())
        }
    }

    private suspend fun buildCredentialRequest(): GetCredentialResponse {
        try {
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getGoogleIdOption())
                .build()
            return crdentialManager.getCredential(
                request = request, context = context
            )
        } catch (e: Exception) {
            throw NoCredentialsException(context.getString(R.string.googlenoaccount))
        }
    }

    private fun getGoogleIdOption(): GetCustomCredentialOption {
        val clientId = BuildConfig.GOOGLE_LOGIN
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(
                clientId
            )
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()
    }

    private fun createNonce(): String {
        val rawnonce = UUID.randomUUID().toString()
        val bytes = rawnonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): SignInResult {
        val credential = result.credential

        if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                return SignInResult.Success(authResult.user?.uid ?: "")
            } catch (e: GoogleIdTokenParsingException) {
                Log.e("googleSignIn2", "signIn: ${e.message}")
                return SignInResult.Failure(context.getString(R.string.googleCancel))
            }
        } else {
            throw LoginFailed(context.getString(R.string.googleCancel))
        }
    }

    suspend fun signOut() {
        crdentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        println("signout")
        firebaseAuth.signOut()
    }
}