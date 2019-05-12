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

public class VisitOrgHomeThread extends Thread{
    private static Config config = Config.getDefaultInstance();
    private static SharedPreferences settings = ActionManage.getSettings();
    private YiBanRequest action;

    public VisitOrgHomeThread() {
        this.action = ActionManage.getAction();
    }

    @Override
    public void run() {
        RunThread.shutDownNumUp();
        RunThread.showLog("开始获取机构号主页...");
        try {
            JSONObject json;
            while (true){
                json= action.getUsersFollows(2);
                if (json.getIntValue("response") != 100){
                    RunThread.showLog("获取我的机构号列表遇到错误："+json.getString("message"));
                    if (settings.getBoolean("autoLogin",config.isAutoLogin())){
                        RunThread.reLogin();
                        continue;
                    }
                    json = null;
                    break;
                }
                break;
            }
            if (json!=null){
                JSONArray orgs = json.getJSONObject("data").getJSONArray("users");
                for (Object object : orgs) {
                    if (!RunThread.isRun){
                        break;
                    }
                    JSONObject item = (JSONObject)object;
                    new GetHomeOrg(item).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            RunThread.showLog("获取机构号主页时遇到一个错误："+e.getMessage());
        }
        RunThread.showLog("获取机构号主页完毕，更多数据正在后台处理！");
        RunThread.shutDownNumDown();
    }
}
class GetHomeOrg extends Thread{
    private YiBanRequest action;
    private JSONObject item;

    public GetHomeOrg(JSONObject item) {
        this.action = ActionManage.getAction();
        this.item = item;
    }
    @Override
    public void run() {
        RunThread.shutDownNumUp();
        try {
            action.getOrgIndex(item.get("user_id"));
            RunThread.showLog("获取机构号["+item.get("name")+"]主页成功...");
        }catch (Exception e){

        }
        RunThread.shutDownNumDown();
    }
}