package com.dkproject.presentation.di

import android.content.Context
import com.dkproject.presentation.ui.screen.login.GoogleSignInClient
import com.dkproject.presentation.ui.screen.login.KakaoSignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object SocialAuthModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun provideGoogleAuthClient(@ApplicationContext context: Context, auth: FirebaseAuth): GoogleSignInClient {
        return GoogleSignInClient(context, auth)
    }

    @Provides
    fun provideKakaoAuthClient(@ApplicationContext context: Context, auth: FirebaseAuth): KakaoSignInClient {
        return KakaoSignInClient(context, auth)
    }
}