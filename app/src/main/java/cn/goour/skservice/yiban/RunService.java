/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.io.FileDescriptor;

public class RunService extends Service {
    private static final String TAG = "RunService";
    public RunService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"绑定服务");
        return new MyIBinder();
    }
    class MyIBinder extends Binder {
        public Service getService(){
            return RunService.this;
        }
    }
    @Override
    public void onCreate() {
        Log.e(TAG,"创建服务");
        createNotification();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"服务OnStart");
        return super.onStartCommand(intent, flags, startId);
    }
    private void createNotification(){
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class);

        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.yiban_icon)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("小K服务") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.yiban_icon) // 设置状态栏内的小图标
                .setContentText("系统正在为您运行...") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        Notification notification = builder.build(); // 获取构建好的Notification

        notification.flags = Notification.FLAG_SHOW_LIGHTS;

        notification.defaults = Notification.DEFAULT_SOUND;
        startForeground(11002, notification);
    }
    @Override
    public void onDestroy() {
        Log.e(TAG,"注销服务");
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG,"解绑服务");
        stopForeground(true);
        return super.onUnbind(intent);
    }
}
