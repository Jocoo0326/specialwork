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

    @Php
    @GET("apps/ticket/getGasTableOptions.html")
    suspend fun getGasTableOptions(
        @Query("ticket_id") id: String
    ): GasTableOptionsInfo

    @Php
    @GET("apps/ticket/get_gas_data_list.html")
    suspend fun getGasList(
        @Query("ticket_id") id: String
    ): GasListResp

    @Php
    @POST("apps/ticket/del_gas_data.html")
    @FormUrlEncoded
    suspend fun deleteGas(
        @Field("gas_data_id") id: String
    ): MMVoid

    @Php
    @POST("apps/ticket/add_gas_data.html")
    @FormUrlEncoded
    suspend fun addGas(@FieldMap params: Map<String, String>): MMVoid

    @Php
    @POST("apps/ticket/edit_gas_data.html")
    @FormUrlEncoded
    suspend fun editGas(@FieldMap params: Map<String, String>): MMVoid

    @Php
    @GET("apps/ticket/getTicketOpinions.html")
    suspend fun getTicketOpinions(
        @Query("ticket_id") id: String
    ): TicketOptionsResp

    @Php
    @POST("apps/ticket/set_opinions.html")
    @FormUrlEncoded
    suspend fun setOpinions(
        @FieldMap params: Map<String, String>
    ): MMVoid

    @Php
    @POST("apps/ticket/set_accept.html")
    @FormUrlEncoded
    suspend fun setAccept(
        @FieldMap params: Map<String, String>
    ): MMVoid

    @Php
    @POST("apps/ticket/set_stop.html")
    @FormUrlEncoded
    suspend fun setStop(
        @Field("ticket_id") id: String
    ): MMVoid

    @Php
    @POST("apps/ticket/set_continue.html")
    @FormUrlEncoded
    suspend fun setContinue(
        @Field("ticket_id") id: String
    ): MMVoid
}