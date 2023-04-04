package com.gdmm.core.network.interceptor;

import android.os.SystemClock;

/**
 * 当前服务器时间 = 上次获取的服务器时间 + 手机开机时间 - 上次解析服务器时间时的手机开机时间
 *
 * @author: Created by hatch on 2018/9/12.
 */
public class ServerTimeManager {

    //上次拿到的服务器时间
    private static long lastServerTime;

    //与服务器手机开机时间
    private static long lastElapsedRealtime;

    /***
     * 同步服务器时间
     *
     * @param _lastServerTime
     * @param _lastElapsedRealtime
     */
    public static void syncServerTime(long _lastServerTime, long _lastElapsedRealtime) {
        lastServerTime = _lastServerTime;
        lastElapsedRealtime = _lastElapsedRealtime;
    }

    /***
     * 当前服务器时间
     *
     * @return 返回毫秒
     */
    public static long getServerTime() {
        return lastServerTime + SystemClock.elapsedRealtime() - lastElapsedRealtime;
    }

    /***
     * 当前服务器时间
     *
     * @return 返回秒
     */
    public static long getServerTime2Seconds() {
        return getServerTime() / 1000L;
    }

}
