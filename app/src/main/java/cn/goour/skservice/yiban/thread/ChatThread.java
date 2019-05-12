/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban.thread;

import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.List;

import cn.goour.skservice.yiban.ActionManage;
import cn.goour.skservice.yiban.Config;
import cn.goour.skservice.yiban.TextList;
import cn.goour.utils.tools.NullValid;
import cn.goour.yiban.app.action.IChat;
import cn.goour.yiban.app.action.WebSocketChat;
import cn.goour.yiban.app.action.YiBanRequest;

/**
 * Created by HouKunLin on 2017/7/25.
 */

public class ChatThread extends Thread{
    private static final String TAG = "ChatThread";
    private static Config config = Config.getDefaultInstance();
    private static SharedPreferences settings = ActionManage.getSettings();
    private YiBanRequest action;
    private WebSocketChat chat;

    public ChatThread() {
        action = ActionManage.getAction();
    }

    @Override
    public void run() {
        RunThread.shutDownNumUp();
        RunThread.showLog("开始执行聊天程序...");
        try {
            this.chat = action.getChatClient();
            String group = settings.getString("group","");
            if (!NullValid.isNull(group)){
                JSONObject item = JSONObject.parseObject(group);
                RunThread.showLog("聊天程序正在发送群组消息...");
                try {
                    Thread.sleep(2000);
                }catch (Exception e){

                }
                int runNum = 0;
                while (true && RunThread.isRun){
                    String text = getText();

                    Log.e(TAG,"For Send Msg :"+item);
                    chat.sendGroupMsg(item,text);
                    RunThread.showLog("聊天程序给群组["+item.getString("name")+"]发送聊天内容："+text);
                    /*for (Object o :arr) {
                        if (!RunThread.isRun){
                            break;
                        }
                        JSONObject json = (JSONObject) o;
                        try {
                            Thread.sleep(1000);
                        }catch (Exception e){}
                    }*/
                    try {
                        Thread.sleep(500);
                    }catch (Exception e){}
                    int runNum2 = 1;
                    try {
                        runNum2 = Integer.parseInt(settings.getString("msgNum",config.getMsgNum()+""));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    runNum++;
                    if (runNum2 > 0 && runNum >= runNum2){
                        RunThread.showLog("发送指定聊天消息次数完毕！");
                        break;
                    }

                    try {
                        long sleep = 1000;
                        try {
                            sleep = Long.parseLong(settings.getString("msgTimes",config.getMsgTimes()+""));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                RunThread.showLog("没有选择聊天群组对象！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            RunThread.showLog("聊天程序遇到一个错误："+e.getMessage());
        }

        RunThread.showLog("聊天程序执行完毕！");
        RunThread.shutDownNumDown();
    }
    private String getText(){
        return TextList.getChatText();
    }
}
