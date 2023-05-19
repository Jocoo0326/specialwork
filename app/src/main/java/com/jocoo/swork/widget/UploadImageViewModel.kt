package com.jocoo.swork.widget

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import com.jocoo.swork.data.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

data class UploadImageState(
    private val name: String? = null
) : State

@HiltViewModel
class UploadImageViewModel @Inject constructor(
    private val repo: ApiRepo
) : BaseViewModel<UploadImageState>(UploadImageState()) {

    fun uploadImage(toByteArray: ByteArray) {
        launchAndCollectIn(repo.uploadImage(toByteArray)) {
            onSuccess = {
                _uploadImageFlow.tryEmit(it.imageUrl ?: "")
            }
        }
    }

    private val _uploadImageFlow = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uploadImageFlow = _uploadImageFlow.asSharedFlow()
}