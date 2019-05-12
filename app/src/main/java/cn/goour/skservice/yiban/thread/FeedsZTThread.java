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

public class FeedsZTThread extends Thread{
    private static Config config = Config.getDefaultInstance();
    private static SharedPreferences settings = ActionManage.getSettings();
    private YiBanRequest action;

    public FeedsZTThread() {
        action = ActionManage.getAction();
    }

    @Override
    public void run() {
        RunThread.shutDownNumUp();
        RunThread.showLog("开始为我的动态列表的动态点赞和同情！");
        int size = 10;
        try {
            size = Integer.parseInt(settings.getString("feedsGetNum",config.getFeedsGetNum()+""));
        }catch (Exception e){
            e.printStackTrace();
        }
        boolean isZan = settings.getBoolean("feedsZan",config.isFeedsZan());
        boolean isTong = settings.getBoolean("feedsTong",config.isFeedsTong());
        try {
            JSONObject feed;
            while (true){
                feed = action.getFeedsSelf(0, 1, size);
                if (feed.getIntValue("response") != 100){
                    RunThread.showLog("获取我的动态列表时遇到错误："+feed.getString("message"));
                    if (settings.getBoolean("autoLogin",config.isAutoLogin())){
                        RunThread.reLogin();
                        continue;
                    }
                    feed = null;
                    break;
                }
                break;
            }
            if (feed!=null){
                runZT(feed,isZan,isTong);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RunThread.showLog("给我的动态列表的好友动态点赞同情时遇到一个错误："+e.getMessage());
        }

        RunThread.showLog("点赞同情动态执行完毕，更多任务正在后台处理！");

        RunThread.shutDownNumDown();

    }
    public static void runZT(JSONObject json,boolean isZan,boolean isTong) throws Exception{
        JSONArray feeds = json.getJSONObject("data").getJSONArray("list");
        for (Object object : feeds) {
            if (!RunThread.isRun){
                break;
            }
            JSONObject item = (JSONObject)object;
            Thread run = new ZT(isZan,isTong,item);
            run.start();
        }
    }
}

class ZT extends Thread{
    private YiBanRequest action;
    private boolean isZan;
    private boolean isTong;
    private String  userid;
    private JSONObject  item;

    public ZT(boolean isZan, boolean isTong, JSONObject item) {
        this.action = ActionManage.getAction();
        this.isZan = isZan;
        this.isTong = isTong;
        this.userid = action.getUser().getUser_id()+"";
        this.item = item;
    }

    @Override
    public void run() {
        RunThread.shutDownNumUp();
        try {
            String upList = item.get("upList").toString();
            String downList = item.get("downList").toString();
            String text = item.getString("content");
            text = text.substring(0,text.length()>10?10:text.length());
            text = "姓名："+item.getString("name")+"，内容："+text;
            int type = 0;
            if (isZan && !upList.contains(userid)) {
                type+=1;
                action.getFeedsUps(item.get("id"));
            }
            if (isTong && !downList.contains(userid)) {
                type+=2;
                action.getFeedsDowns(item.get("id"));
            }
            switch (type){
                case 0:
                    break;
                case 1:
                    RunThread.showLog("+1 点赞成功！"+text);
                    break;
                case 2:
                    RunThread.showLog("+1 同情成功！"+text);
                    break;
                case 3:
                    RunThread.showLog("+1 点赞同情成功！"+text);
                    break;
            }
        }catch (Exception e){

        }
        RunThread.shutDownNumDown();
    }
}