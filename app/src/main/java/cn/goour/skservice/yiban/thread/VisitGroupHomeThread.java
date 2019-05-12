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

public class VisitGroupHomeThread extends Thread{
    private static Config config = Config.getDefaultInstance();
    private static SharedPreferences settings = ActionManage.getSettings();
    private YiBanRequest action;

    public VisitGroupHomeThread() {
        this.action = ActionManage.getAction();
    }

    @Override
    public void run() {

        RunThread.shutDownNumUp();
        RunThread.showLog("开始获取群组列表...");
        try {
            JSONObject json;
            while (true){
                json= action.getUsersGroupsAll();
                if (json.getIntValue("response") != 100){
                    RunThread.showLog("获取我的群组列表遇到错误："+json.getString("message"));
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
                JSONArray arr = json.getJSONObject("data").getJSONArray("groups");
                for (Object o:arr) {
                    if (!RunThread.isRun){
                        break;
                    }
                    JSONObject item = (JSONObject) o;
                    new GetHomeGroup(item).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            RunThread.showLog("获取群组主页时遇到一个错误："+e.getMessage());
        }
        RunThread.showLog("获取群组主页完毕，更多数据正在后台处理！");
        RunThread.shutDownNumDown();

    }
}
class GetHomeGroup extends Thread{
    private YiBanRequest action;
    private JSONObject item;

    public GetHomeGroup(JSONObject item) {
        this.action = ActionManage.getAction();
        this.item = item;
    }

    @Override
    public void run() {
        RunThread.shutDownNumUp();
        try {
            String theId = item.getString("group_id");
            String theUserId = item.getString("user_id");
            action.getGroupsIndex(theId,theUserId);
            RunThread.showLog("获取群组["+item.getString("name")+"]主页成功...");
        }catch (Exception e){

        }
        RunThread.shutDownNumDown();
    }
}