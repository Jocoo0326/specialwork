package com.jocoo.swork.data

import com.gdmm.core.network.Php
import com.jocoo.swork.bean.LoginItem
import com.jocoo.swork.bean.MMVoid
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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

}