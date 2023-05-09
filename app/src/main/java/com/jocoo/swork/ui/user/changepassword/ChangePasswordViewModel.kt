package com.jocoo.swork.ui.user.changepassword

import com.gdmm.core.BaseViewModel
import com.gdmm.core.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class ChangePasswordState(
    val name: String? = null
) : State

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(

) : BaseViewModel<ChangePasswordState>(ChangePasswordState()) {

}
