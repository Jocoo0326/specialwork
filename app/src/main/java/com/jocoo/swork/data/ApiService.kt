package com.jocoo.swork.data

import com.gdmm.core.network.Php
import com.jocoo.swork.bean.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @Php
    @POST("apps/user/login.html")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") passwd: String
    ): LoginItem

    @Php
    @POST("apps/user/change_pwd.html")
    @FormUrlEncoded
    suspend fun changePassword(
        @Field("old_pwd") oldPwd: String,
        @Field("new_pwd") newPwd: String
    ): MMVoid

    @Php
    @GET("apps/user/get_department_list.html")
    suspend fun getDepartmentList(
        @Query("page") pageNum: Int,
        @Query("limit") pageSize: Int,
        @Query("parent_id") parentId: String
    ): PageItem<WorkUnitItem>

    @Php
    @GET("apps/user/get_contractor_list.html")
    suspend fun getContractorList(
        @Query("page") pageNum: Int,
        @Query("limit") pageSize: Int
    ): PageItem<WorkUnitItem>

    @Php
    @GET("apps/user/get_operator_list.html")
    suspend fun getOperatorList(
        @QueryMap params: Map<String, String>
    ): PageItem<OperatorInfo>

    @Php
    @GET("apps/statistic/index.html")
    suspend fun getStatistic(): HomeRes

    @Php
    @GET("apps/ticket/get_list.html")
    suspend fun getTicketList(
        @QueryMap params: Map<String, String>
    ): PageItem<WorkInfo>

    @Php
    @GET("apps/ticket/get_info.html")
    suspend fun getTicketInfo(
        @Query("ticket_id") id: String
    ): TicketInfoRes

    @Php
    @Multipart
    @POST("apps/ticket/uploadImages.html")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): UploadInfo

    @Php
    @POST("apps/ticket/set_checkres.html")
    @FormUrlEncoded
    suspend fun checkSafety(
        @FieldMap map: Map<String, String>
    ): MMVoid
}