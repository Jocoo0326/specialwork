package com.gdmm.core.network.interceptor;

import android.os.SystemClock;
import android.text.TextUtils;
import com.gdmm.core.util.TimeUtilKt;
import com.gdmm.core.util.DateFormatPattern;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间拦截器
 *
 * @author: Created by hatch on 2019/2/22.
 */
public class DateTimeInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        Headers headers = response.headers();

        String strServerDate = headers.get("Date");
        if (!TextUtils.isEmpty(strServerDate)) {
            try {
                Date serverDate = TimeUtilKt.stringConvertToDate(strServerDate
                        , DateFormatPattern.GMT, Locale.ENGLISH, TimeUtilKt::dateFormat);
                if (serverDate != null) {
                    ServerTimeManager.syncServerTime(serverDate.getTime(), SystemClock.elapsedRealtime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
