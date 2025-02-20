package com.dkproject.presentation.di

import android.content.Context
import androidx.annotation.StringRes
import com.dkproject.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject


interface ResourceProvider {
    fun getString(@StringRes resId: Int): String
}

class DefaultResourceProvider @Inject constructor(
@ApplicationContext private val context: Context
) : ResourceProvider {
    override fun getString(@StringRes resId: Int): String = context.getString(resId)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {
    @Binds
    abstract fun bindResourceProvider(resourceProvider: DefaultResourceProvider): ResourceProvider
}

