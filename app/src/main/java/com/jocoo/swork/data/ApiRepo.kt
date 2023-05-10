package com.jocoo.swork.data

import com.gdmm.core.di.IoDispatcher
import com.gdmm.core.extensions.safeApiCall
import com.gdmm.core.network.ApiResponse
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ApiRepo @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineContext,
    private val apiService: ApiService
) {

    fun login(username: String, passwd: String) = flow {
        emit(ApiResponse.success(apiService.login(username, passwd)))
    }.safeApiCall(ioDispatcher)

    fun changePassword(oldPwd: String, newPwd: String) = flow {
        emit(ApiResponse.success(apiService.changePassword(oldPwd, newPwd)))
    }.safeApiCall(ioDispatcher)
}