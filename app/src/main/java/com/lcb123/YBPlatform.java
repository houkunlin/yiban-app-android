/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package com.lcb123;

/**
 * Created by HouKunLin on 2017/7/24.
 */

public class YBPlatform {
    static {
        System.loadLibrary("YBPlatform");
    }

    /**
     * 聊天链接服务器时必须的一个参数
     * @param host 用户id
     * @param access_token 令牌
     * @param timeStamp 一个链接次数 ，他的默认初始值=1464229527，但时链接时服务器会返回正确的值
     * @return
     */
    public static native String signature(String host, String access_token, String timeStamp);
}
