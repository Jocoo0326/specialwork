package com.gdmm.core.network

import android.content.Context
import android.content.SharedPreferences
import com.gdmm.core.BaseApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        SessionManager.getInstance(BaseApplication.applicationContext()).authToken?.let {
            requestBuilder.addHeader("Authorization", it)
        }
        return chain.proceed(requestBuilder.build())
    }
}

class SessionManager private constructor(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_LOGIN_TYPE = "login_type"
        const val USER_PHONE = "user_phone"
        const val USER_ISSETPWDS = "user_is_setpwds"
        const val STATION_ID = "station_id"

        @Volatile
        private var instance: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return instance ?: synchronized(this) {
                instance ?: SessionManager(context).also { instance = it }
            }
        }
    }

    var authToken: String?
        get() = prefs.getString(USER_TOKEN, null)
        set(value) {
            prefs.edit().putString(USER_TOKEN, value).apply()
        }


    var loginType: String?
        get() {
            return prefs.getString(USER_LOGIN_TYPE, "")
        }
        set(value) {
            prefs.edit().putString(USER_LOGIN_TYPE, value).apply()
        }

    fun clearAll() {
        loginType = ""
        authToken = null
    }

    /**
     * 是否已登陆
     *
     * @return
     */
    fun isLogin(): Boolean {
        return !authToken.isNullOrEmpty()
    }
}