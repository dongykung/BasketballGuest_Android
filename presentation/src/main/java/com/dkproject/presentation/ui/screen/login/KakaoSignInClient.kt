package com.dkproject.presentation.ui.screen.login

import android.content.Context
import android.util.Log
import com.dkproject.presentation.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class KakaoSignInClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun signInWithKakao(): SignInResult {
        try {
            val oauthToken: OAuthToken =
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    loginWithKakaoTalkAsync()
                } else {
                    loginWithKakaoAccountAsync()
                }
            loginWithEmailPermissionAsync()
            val idToken = oauthToken.idToken
            val accessToken = oauthToken.accessToken
            val credential = OAuthProvider.newCredentialBuilder("oidc.kakao")
                .setIdToken(idToken)
                .setAccessToken(accessToken)
                .build()
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            return SignInResult.Success(authResult.user?.uid ?: "")
        } catch (e: Exception) {
            return SignInResult.Failure(e.message.toString())
        }
    }

    private suspend fun loginWithKakaoTalkAsync(): OAuthToken =
        suspendCancellableCoroutine { cont ->
            UserApiClient.instance.loginWithKakaoTalk(context) { oauthToken, error ->
                if (error != null) {
                    cont.resumeWithException(error)
                } else if (oauthToken != null) {
                    cont.resume(oauthToken)
                } else {
                    cont.resumeWithException(Exception(context.getString(R.string.kakaoCancel)))
                }
            }
        }

    private suspend fun loginWithKakaoAccountAsync(): OAuthToken =
        suspendCancellableCoroutine { cont ->
            val scopes = mutableListOf<String>("openid", "account_email")
            UserApiClient.instance.loginWithKakaoAccount(context) { oauthToken, error ->
                if (error != null) {
                    cont.resumeWithException(error)
                } else if (oauthToken != null) {
                    cont.resume(oauthToken)
                } else {
                    cont.resumeWithException(Exception(context.getString(R.string.kakaoCancel)))
                }
            }
        }

    private suspend fun loginWithEmailPermissionAsync(): OAuthToken =
        suspendCancellableCoroutine { cont ->
            val scopes = listOf("openid", "account_email")
            UserApiClient.instance.loginWithNewScopes(context = context, scopes = scopes) { oauthToken, error ->
                if (error != null) {
                    cont.resumeWithException(error)
                } else if (oauthToken != null) {
                    cont.resume(oauthToken)
                } else {
                    cont.resumeWithException(Exception("Unknown error in loginWithEmailPermissionAsync"))
                }
            }
        }
}

