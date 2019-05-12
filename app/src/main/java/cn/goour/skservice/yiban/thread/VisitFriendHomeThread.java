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
import cn.goour.utils.tools.NullValid;
import cn.goour.yiban.app.action.YiBanRequest;

/**
 * Created by HouKunLin on 2017/7/25.
 */

public class VisitFriendHomeThread extends Thread{
    private static Config config = Config.getDefaultInstance();
    private static SharedPreferences settings = ActionManage.getSettings();
    private YiBanRequest action;

    public VisitFriendHomeThread() {
        action = ActionManage.getAction();
    }

    @Override
    public void run() {

        RunThread.shutDownNumUp();
        RunThread.showLog("开始执行访问好友主页程序...");
        try {
            JSONObject json;
            while (true){
                json= action.getUsersFriends();
                if (json.getIntValue("response") != 100){
                    RunThread.showLog("获取我的好友列表遇到错误："+json.getString("message"));
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
                JSONArray arr = json.getJSONObject("data").getJSONArray("friends");
                for (Object o:arr) {
                    if (!RunThread.isRun){
                        break;
                    }
                    JSONObject friend = (JSONObject) o;
                    new GetHomeFriend(friend).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            RunThread.showLog("访问好友主页时遇到一个错误："+e.getMessage());
        }
        RunThread.showLog("访问好友主页完毕，更多任务正在后台处理！");
        RunThread.shutDownNumDown();
    }
}
class GetHomeFriend extends Thread{
    private static Config config = Config.getDefaultInstance();
    private static SharedPreferences settings = ActionManage.getSettings();
    private YiBanRequest action;
    private JSONObject friend;
    private String user_id;

    public GetHomeFriend(JSONObject friend) {
        this.action = ActionManage.getAction();
        this.friend = friend;
        this.user_id = action.getUser().getUser_id()+"";
    }

    @Override
    public void run() {
        RunThread.shutDownNumUp();
        try {
            String theUserId = friend.getString("user_id");
            action.getUsersHome(theUserId);
            String name = friend.getString("user_name");
            if (NullValid.isNull(name)){
                name=friend.getString("name");
            }
            RunThread.showLog("获取我的好友["+name+"]主页成功...");
            if (settings.getBoolean("feedsZTInit",config.isFeedsZTInit())){
                JSONObject fees = action.getFeedsOther(theUserId, 0, 1, 50);
                FeedsZTThread.runZT(fees,true,true);
            }
        }catch (Exception e){

        }
        RunThread.shutDownNumDown();
    }
}