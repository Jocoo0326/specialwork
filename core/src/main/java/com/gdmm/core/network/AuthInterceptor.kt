package com.gdmm.core.network

import android.content.Context
import android.content.SharedPreferences
import com.gdmm.core.BaseApplication
import com.squareup.moshi.JsonClass
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        SessionManager.getInstance(BaseApplication.applicationContext()).authToken?.let {
            requestBuilder.addHeader("X-Token", it)
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

    var userInfo: UserInfoItem? = null

    fun clearAll() {
        loginType = ""
        authToken = null
        userInfo = null
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

@JsonClass(generateAdapter = true)
data class UserInfoItem(
    var user_id: String? = null,
    var phone: String? = null,
    var org_id: String? = null,
    var name: String? = null,
    var org_type: String? = null,
    var org_name: String? = null,
    var bigscreen_name: String? = null,
    var area_id: String? = null,
    var dep_name: String? = null,
    var group_name: String? = null,
    var area_name: String? = null,
)
