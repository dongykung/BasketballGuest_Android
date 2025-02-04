package com.dkproject.presentation.ui.screen.GuestPost

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dkproject.presentation.R
import com.dkproject.presentation.model.GuestPostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GuestPostViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(GuestPostState())
    val uiState = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        if (title.length > 25) return
        if (title.length < 5) _uiState.update {
            it.copy(
                guestPost = it.guestPost.copy(title = title),
                titleErrorMessage = context.getString(R.string.leasttext, 5)
            )
        }
        else _uiState.update {
            it.copy(guestPost = it.guestPost.copy(title = title), titleErrorMessage = "")
        }
    }

    fun updateDescription(description: String) {
        if (description.length > 200) return
        if (description.length < 5) _uiState.update {
            it.copy(
                guestPost = it.guestPost.copy(description = description),
                descriptionErrorMessage = context.getString(R.string.leasttext, 5)
            )
        }
        else _uiState.update {
            it.copy(guestPost = it.guestPost.copy(description = description), descriptionErrorMessage = "")
        }
    }
}

sealed class GuestPostStep(val step: Int, val progress: Float) {
    data object Description : GuestPostStep(1, 0.25f)
    data object GuestDate : GuestPostStep(2, 0.5f)
    data object Position : GuestPostStep(3, 0.75f)
    data object Address : GuestPostStep(4, 1.0f)
}

data class GuestPostState(
    val currentStep: GuestPostStep = GuestPostStep.Description,
    val guestPost: GuestPostUiModel = GuestPostUiModel(),
    val titleErrorMessage: String = "",
    val descriptionErrorMessage: String = ""
)