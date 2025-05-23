package com.dkproject.presentation.ui.screen.GuestPost

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.model.Poi
import com.dkproject.domain.usecase.Guest.UpdateGuestPostUseCase
import com.dkproject.domain.usecase.Guest.UploadGuestPostUseCase
import com.dkproject.presentation.R
import com.dkproject.presentation.extension.combineWithTimeFrom
import com.dkproject.presentation.extension.withTime
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.model.Position
import com.dkproject.presentation.model.toDomainModel
import com.dkproject.presentation.util.isNetworkAvailable
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class GuestPostViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val uploadGuestPostUseCase: UploadGuestPostUseCase,
    private val updateGuestPostUseCase: UpdateGuestPostUseCase,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _uiEvent = Channel<PostUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(GuestPostState())
    val uiState = _uiState.asStateFlow()

    fun updateEditMode(guestPost: GuestPostUiModel) {
        _uiState.update { it.copy(guestPost = guestPost, isEditMode = true) }
    }

    fun updateCurrentStep(step: GuestPostStep) {
        _uiState.update { it.copy(currentStep = step) }
    }

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
            it.copy(
                guestPost = it.guestPost.copy(description = description),
                descriptionErrorMessage = ""
            )
        }
    }

    fun updateDate(date: Date) {
        val baseTime = date.withTime(0, 0, 0, 0)

        val previousStartTime = uiState.value.guestPost.startDate
        val previousEndTime = uiState.value.guestPost.endDate
        val newStartTime = previousStartTime.combineWithTimeFrom(baseTime)
        val newEndTime = previousEndTime.combineWithTimeFrom(baseTime)
        _uiState.update {
            it.copy(
                guestPost = it.guestPost.copy(
                    date = baseTime,
                    startDate = newStartTime,
                    endDate = newEndTime
                )
            )
        }
    }

    fun updateStartTime(date: Date) {
        _uiState.update { it.copy(guestPost = it.guestPost.copy(startDate = date)) }
    }

    fun updateEndTime(date: Date) {
        val msg = if (date < uiState.value.guestPost.startDate) R.string.timeerror else null
        _uiState.update { it.copy(guestPost = it.guestPost.copy(endDate = date)) }
    }

    fun updatePosition(position: Position) {
        val posStr = position.toFirestoreValue()
        val currentPositions = uiState.value.guestPost.positions
        val newPositions = if (posStr == "무관") {
            listOf("무관")
        } else {
            val toggledPositions = if (currentPositions.contains(posStr)) {
                currentPositions - posStr
            } else {
                currentPositions + posStr
            }
            toggledPositions - "무관"
        }
        _uiState.update { it.copy(guestPost = it.guestPost.copy(positions = newPositions)) }
    }

    fun updateMemberCount(count: Int) {
        _uiState.update { it.copy(guestPost = it.guestPost.copy(memberCount = count)) }
    }

    fun updateAddress(poi: Poi) {
        _uiState.update {
            it.copy(
                guestPost = it.guestPost.copy(
                    placeName = poi.name,
                    placeAddress = poi.detailAddress,
                    parkFlag = poi.parkFlag ?: 0,
                    lat = poi.noorLat,
                    lng = poi.noorLon
                )
            )
        }
    }

    fun uploadPost() {
        val myUid = auth.currentUser?.uid
        viewModelScope.launch {
            if (myUid == null) {
                _uiEvent.send(PostUiEvent.ShowSnackbar(context.getString(R.string.loselogin)))
                return@launch
            }
            if (!isNetworkAvailable(context)) {
                _uiEvent.send(PostUiEvent.ShowSnackbar(context.getString(R.string.networkerror)))
                return@launch
            }
            _uiState.update {
                it.copy(
                    isLoading = true,
                    guestPost = it.guestPost.copy(writerUid = myUid)
                )
            }
            val result = withContext(context = Dispatchers.IO) {
                if (uiState.value.isEditMode) updateGuestPostUseCase(uiState.value.guestPost.id ?: "", uiState.value.guestPost.toDomainModel())
                else uploadGuestPostUseCase(uiState.value.guestPost.toDomainModel())
            }
            result.fold(
                onSuccess = {
                    if(uiState.value.isEditMode) _uiEvent.send(PostUiEvent.UpdateComplete)
                    else _uiEvent.send(PostUiEvent.UploadComplete)
                },
                onFailure = { e ->
                    _uiEvent.send(PostUiEvent.ShowSnackbar(getErrorMessage(e)))
                    _uiState.update { it.copy(isLoading = false) }
                }
            )
        }
    }


    private fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is IOException -> context.getString(R.string.networkerror)
            else -> context.getString(R.string.uploadfail)
        }
    }
}

sealed class GuestPostStep(@StringRes val step: Int, val progress: Float) {
    data object Description : GuestPostStep(R.string.postdescription, 0.25f)
    data object GuestDate : GuestPostStep(R.string.date, 0.5f)
    data object Position : GuestPostStep(R.string.postposition, 0.75f)
    data object Address : GuestPostStep(R.string.postaddress, 1.0f)
}

data class GuestPostState(
    val currentStep: GuestPostStep = GuestPostStep.Description,
    val guestPost: GuestPostUiModel = GuestPostUiModel(date = Date().withTime(0, 0, 0, 0)),
    val isLoading: Boolean = false,
    val titleErrorMessage: String = "",
    val descriptionErrorMessage: String = "",
    val isEditMode: Boolean = false
)

sealed class PostUiEvent {
    data class ShowSnackbar(val message: String) : PostUiEvent()
    data object UploadComplete : PostUiEvent()
    data object UpdateComplete : PostUiEvent()
}