package com.jocoo.swork.data

import com.gdmm.core.network.Php
import com.jocoo.swork.bean.LoginItem
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

}