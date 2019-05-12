/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban.thread;

import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.goour.skservice.yiban.ActionManage;
import cn.goour.skservice.yiban.Config;
import cn.goour.yiban.app.action.YiBanRequest;

/**
 * Created by HouKunLin on 2017/7/25.
 */

public class CheckInThread extends Thread{
    private static Config config = Config.getDefaultInstance();
    private static SharedPreferences settings = ActionManage.getSettings();
    private YiBanRequest action;
    public CheckInThread() {
        action = ActionManage.getAction();
    }

    @Override
    public void run() {
        RunThread.shutDownNumUp();
        RunThread.showLog("开始执行签到程序...");
        int errorNum = 0;
        while (true && RunThread.isRun){
            try {
                JSONObject json = action.getCheckin_Question();
                if (json.getIntValue("response") != 100){
                    RunThread.showLog("检查签到遇到错误："+json.getString("message"));
                    if (settings.getBoolean("autoLogin",config.isAutoLogin())){
                        RunThread.reLogin();
                        continue;
                    }
                    break;
                }
                boolean isChecked = json.getJSONObject("data").getBooleanValue("isChecked");
                if (!isChecked){
                    JSONArray json2 = json.getJSONObject("data").getJSONObject("survey").getJSONObject("question").getJSONArray("option");
                    JSONObject json3 = (JSONObject)json2.get(0);
                    action.getCheckin_Answer(json3.get("Question_id"), 1);
                    if (settings.getBoolean("checkInToFeeds",config.isCheckInToFeeds())){
                        String address = "";
                        String lat = "";
                        String lng = "";
                        if (settings.getBoolean("feedsGps",config.isFeedsGps())){
                            address = settings.getString("gpsAddress",config.getGpsAddress());
                            lat = settings.getString("gpsLat",config.getGpsLat());
                            lng = settings.getString("gpsLng",config.getGpsLng());
                        }
                        action.getFeedsADD("我在易班签到，你也快来吧~~~~",address,lat,lng);

                        RunThread.showLog("签到内容已经同步到个人动态！");
                    }
                    RunThread.showLog("签到成功！");
                }else {
                    RunThread.showLog("今天已经签到！");
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
                RunThread.showLog("签到遇到一个错误："+e.getMessage());
                errorNum++;
            }
            if (errorNum >= 10){
                RunThread.showLog("签到程序遇到错误超过10次！");
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        RunThread.showLog("签到程序执行完毕！");

        RunThread.shutDownNumDown();
    }
}
