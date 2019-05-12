/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import cn.goour.yiban.app.action.YiBanRequest;
import cn.goour.yiban.app.entity.User;

/**
 * Created by HouKunLin on 2017/7/28.
 */

public class ActionManage {
    private static User user;
    private static YiBanRequest action;
    private static SharedPreferences settings;
    private static SharedPreferences sKSYSettings;

    /*static {
        sKSYSettings = new Activity().getSharedPreferences("sKServiceYiban", Context.MODE_PRIVATE);
    }*/

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        ActionManage.user = user;
        if (ActionManage.action == null){
            ActionManage.action = new YiBanRequest(user);
        }else {
            getAction().setUser(user);
        }

    }

    public static YiBanRequest getAction() {
        return action;
    }

    public static void setAction(YiBanRequest action) {
        ActionManage.action = action;
    }

    public static SharedPreferences getSettings() {
        return settings;
    }

    public static void setSettings(SharedPreferences settings) {
        ActionManage.settings = settings;
    }

    public static SharedPreferences getsKSYSettings() {
        return sKSYSettings;
    }

    public static void setsKSYSettings(SharedPreferences sKSYSettings) {
        ActionManage.sKSYSettings = sKSYSettings;
    }
}
