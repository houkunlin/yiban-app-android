/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban.thread;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.java_websocket.handshake.ServerHandshake;

import java.util.concurrent.atomic.AtomicInteger;

import cn.goour.skservice.yiban.ActionManage;
import cn.goour.skservice.yiban.Config;
import cn.goour.skservice.yiban.MainActivity;
import cn.goour.yiban.app.action.IChat;
import cn.goour.yiban.app.action.YiBanRequest;
import cn.goour.yiban.app.entity.User;

/**
 * Created by HouKunLin on 2017/7/25.
 */

public class RunThread extends Thread{
    private static final String TAG = "RunThread";
    public static boolean isRun = false;
    public static Handler handler;
    public static Config config = Config.getDefaultInstance();
//    private static AtomicInteger number = new AtomicInteger(0);
    private static volatile int shutDownNum = 0;
    private static SharedPreferences settings = ActionManage.getSettings();
    private YiBanRequest action;
    private CheckInThread checkInThread;
    private FeedsAddThread feedsAddThread;
    private FeedsZTThread feedsZTThread;
    private ChatThread chatThread;
    private VisitOrgHomeThread visitOrgHomeThread;
    private VisitGroupHomeThread visitGroupHomeThread;
    private VisitFriendHomeThread visitFriendHomeThread;

    public RunThread() {
        action = ActionManage.getAction();
        handler = MainActivity.handler;
    }
    @Override
    public void run() {
        RunThread.isRun = true;

        try {
            action.getChatClient(new IChat() {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {

                }

                @Override
                public void onMessage(String s) {

                }

                @Override
                public void send(String s) {

                }

                @Override
                public void send5(String s) {

                }

                @Override
                public void onClose(int i, String s, boolean b) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (settings.getBoolean("checkIn",config.isCheckIn()) && RunThread.isRun){
            checkInThread = new CheckInThread();
            checkInThread.start();
        }
        if (settings.getBoolean("feedsAdd",config.isFeedsAdd()) && RunThread.isRun){
            feedsAddThread = new FeedsAddThread();
            feedsAddThread.start();
        }
        boolean isZan = settings.getBoolean("feedsZan",config.isFeedsZan());
        boolean isTong = settings.getBoolean("feedsTong",config.isFeedsTong());
        if ((isZan || isTong) && RunThread.isRun){
            feedsZTThread = new FeedsZTThread();
            feedsZTThread.start();
        }
        if (settings.getBoolean("msgSend",config.isMsgSend()) && RunThread.isRun){
            chatThread = new ChatThread();
            chatThread.start();
        }
        if (settings.getBoolean("visitOrgHome",config.isVisitOrgHome()) && RunThread.isRun){
            visitOrgHomeThread = new VisitOrgHomeThread();
            visitOrgHomeThread.start();
        }
        if (settings.getBoolean("visitGroupHome",config.isVisitGroupHome()) && RunThread.isRun){
            visitGroupHomeThread = new VisitGroupHomeThread();
            visitGroupHomeThread.start();
        }
        if (settings.getBoolean("visitFriendHome",config.isVisitFriendHome()) && RunThread.isRun){
            visitFriendHomeThread = new VisitFriendHomeThread();
            visitFriendHomeThread.start();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (shutDownNum > 0){
            Log.e(TAG,"num1="+shutDownNum+";");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message message = new Message();
        message.what=2;
        handler.sendMessage(message);

    }

    public void shutDown(){
        RunThread.isRun = false;
    }
    public static void showLog(String text){
        if (handler == null){
            handler = MainActivity.handler;
        }
        if (handler == null){
            return;
        }
        Message message = new Message();
        message.what=1;
        message.obj=text+"\n";
        handler.sendMessage(message);
    }
    public static void reLogin(){
        try {
            JSONObject json = YiBanRequest.getLogin(ActionManage.getUser());
            User user = new User(json);
            ActionManage.setUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static synchronized void shutDownNumUp(){
//        number.incrementAndGet();
        shutDownNum+=1;
    }
    public static synchronized void shutDownNumDown(){
//        number.decrementAndGet();
        shutDownNum-=1;
    }
}
