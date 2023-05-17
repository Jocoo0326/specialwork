package com.jocoo.swork.data

import com.gdmm.core.di.IoDispatcher
import com.gdmm.core.extensions.safeApiCall
import com.gdmm.core.network.ApiResponse
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    fun getDepartmentList(
        pageNum: Int,
        pageSize: Int,
        parentId: String
    ) = flow {
        emit(ApiResponse.success(apiService.getDepartmentList(pageNum, pageSize, parentId)))
    }.safeApiCall(ioDispatcher)

    fun getContractorList(
        pageNum: Int,
        pageSize: Int
    ) = flow {
        emit(ApiResponse.success(apiService.getContractorList(pageNum, pageSize)))
    }.safeApiCall(ioDispatcher)

    fun getOperatorList(
        params: Map<String, String>
    ) = flow {
        emit(ApiResponse.success(apiService.getOperatorList(params)))
    }.safeApiCall(ioDispatcher)

    fun getStatistic() = flow {
        emit(ApiResponse.success(apiService.getStatistic()))
    }.safeApiCall(ioDispatcher)

    fun getTicketList(
        params: Map<String, String>
    ) = flow {
        emit(ApiResponse.success(apiService.getTicketList(params)))
    }.safeApiCall(ioDispatcher)

    fun getTicketInfo(
        id: String
    ) = flow {
        emit(ApiResponse.success(apiService.getTicketInfo(id)))
    }.safeApiCall(ioDispatcher)

    fun uploadImage(
        imageByteArray: ByteArray
    ) = flow {
        val body = imageByteArray.toRequestBody("multipart/form-data".toMediaType())
        val part = MultipartBody.Part.createFormData("image", "image", body)
        emit(ApiResponse.success(apiService.uploadImage(part)))
    }.safeApiCall(ioDispatcher)

    fun checkSafety(
        map: Map<String, String>
    ) = flow {
        emit(ApiResponse.success(apiService.checkSafety(map)))
    }.safeApiCall(ioDispatcher)

    fun deleteGas(id: String) = flow {
        emit(ApiResponse.success(apiService.deleteGas(id)))
    }.safeApiCall(ioDispatcher)

    fun addGas(params: Map<String, String>) = flow {
        emit(ApiResponse.success(apiService.addGas(params)))
    }.safeApiCall(ioDispatcher)

    fun getGasList(id: String) = flow {
        emit(ApiResponse.success(apiService.getGasList(id)))
    }.safeApiCall(ioDispatcher)

    fun getGasTableOptions(id: String) = flow {
        emit(ApiResponse.success(apiService.getGasTableOptions(id)))
    }.safeApiCall(ioDispatcher)
}