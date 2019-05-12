/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban.thread;

import android.content.SharedPreferences;

import cn.goour.skservice.yiban.ActionManage;
import cn.goour.skservice.yiban.Config;
import cn.goour.skservice.yiban.TextList;
import cn.goour.yiban.app.action.YiBanRequest;

/**
 * Created by HouKunLin on 2017/7/25.
 */

public class FeedsAddThread extends Thread{
    private static Config config = Config.getDefaultInstance();
    private static SharedPreferences settings = ActionManage.getSettings();
    private YiBanRequest action;

    public FeedsAddThread() {
        action = ActionManage.getAction();
    }

    @Override
    public void run() {
        RunThread.shutDownNumUp();
        RunThread.showLog("开始执行发布动态程序...");
        int runNum = 0;
        while (true && RunThread.isRun){
            try {
                String text = getFeedsText();
                String address = "";
                String lat = "";
                String lng = "";
                if (settings.getBoolean("feedsGps",config.isFeedsGps())){
                    address = settings.getString("gpsAddress",config.getGpsAddress());
                    lat = settings.getString("gpsLat",config.getGpsLat());
                    lng = settings.getString("gpsLng",config.getGpsLng());
                }

                action.getFeedsADD(text, address, lat, lng);
                RunThread.showLog("发布一条动态成功！动态内容："+text);
                if (settings.getBoolean("feedsToWeiBo",config.isFeedsToWeiBo())){
                    action.getWeiBo(text);
                    RunThread.showLog("动态同步到中职社区微博！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                RunThread.showLog("发布动态遇到一个错误："+e.getMessage());
            }
            int runNum2 = 1;
            try {
                runNum2 = Integer.parseInt(settings.getString("feedsNum",config.getFeedsNum()+""));
            }catch (Exception e){

            }
            runNum++;
            if (runNum2 > 0 && runNum >= runNum2){
                RunThread.showLog("指定发布动态条数完成！");
                break;
            }

            try {
                long sleep = 1000;
                try {
                    sleep = Long.parseLong(settings.getString("feedsTimes",config.getFeedsTimes()+""));
                }catch (Exception e){
                    e.printStackTrace();
                }
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        RunThread.showLog("发布动态程序执行完毕！");

        RunThread.shutDownNumDown();
    }
    private String getFeedsText(){
        String text = TextList.getFeedsText();
        return text;
    }
}
