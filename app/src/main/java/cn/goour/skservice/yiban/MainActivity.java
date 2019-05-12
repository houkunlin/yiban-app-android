/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import cn.goour.skservice.yiban.thread.RunThread;
import cn.goour.utils.io.FileUtils;
import cn.goour.utils.io.IO;
import cn.goour.yiban.app.entity.User;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static Config config = new Config(true);
    public static Handler handler;
    private Button mBtnStart;
    private Button mBtnStop;
    private TextView mLogView;

    private SharedPreferences settings = null;
    private RunThread run;
    private ScrollView mLogBox;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e(TAG,"开启服务");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(TAG,"关闭服务");
        }
    };
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        settings = ActionManage.getSettings();

        initTextList();
        start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mLogView.append("" + msg.obj);
                        mLogBox.fullScroll(ScrollView.FOCUS_DOWN);
                        break;
                    case 2:
                        mBtnStart.setEnabled(true);
                        mBtnStop.setEnabled(false);
                        break;
                    default:
                }
            }
        };
    }

    private void initView() {
        mBtnStart = findViewById(R.id.btnStart);
        mBtnStop = findViewById(R.id.btnStop);
        mLogBox = findViewById(R.id.logBox);
        mLogView = findViewById(R.id.logView);
    }

    private void start() {
        intent = new Intent(this,RunService.class);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnStart.setEnabled(false);
                mBtnStop.setEnabled(true);
                mLogView.setText("");
                new RunGo().execute();
                startService(intent);
            }
        });
        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (run != null) {
                    run.shutDown();
                    mBtnStart.setEnabled(true);
                    mBtnStop.setEnabled(false);
                }
                stopService(intent);
            }
        });
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private void initTextList() {
        File chat ;
        File feeds ;
        boolean isChatPath = settings.getBoolean("msgSourceForSD",config.isMsgSourceForSD());
        boolean isFeedsPath = settings.getBoolean("feedsSourceForSD",config.isFeedsSourceForSD());
        String chatPath = settings.getString("msgSourcePath", config.getMsgSourcePath());
        String feedsPath = settings.getString("feedsSourcePath", config.getFeedsSourcePath());
        if ("".equals(chatPath.trim())){
            chatPath = config.getMsgSourcePath();
        }
        if ("".equals(feedsPath.trim())){
            feedsPath = config.getFeedsSourcePath();
        }
        try {
            if (!isChatPath){
                TextList.initChatXls(getResources().getAssets().open("chat.xls"));
                mLogView.append("没开启自定义聊天内容，使用默认文件\n");
            }else if (chatPath.equals(config.getMsgSourcePath())) {
                TextList.initChatXls(getResources().getAssets().open("chat.xls"));
                mLogView.append("自定义聊天内容使用默认文件\n");
            } else {
                if (chatPath.startsWith(Config.getRootPath())){
                    chat = new File(getSDPath(),chatPath);
                }else {
                    chat = new File(chatPath);
                }
                if (!chat.exists() || chat.isDirectory()) {
                    mLogView.append("自定义聊天内容文件不存在，使用默认文件\n");
                    TextList.initChatXls(getResources().getAssets().open("chat.xls"));
                }else {
                    mLogView.append("自定义聊天内容\n");
                    TextList.initChat(chat);
                }
            }
        } catch (Exception e) {
            mLogView.append(e.getMessage() + "\n");
        }

        try {
            if (!isFeedsPath){
                TextList.initFeedsXls(getResources().getAssets().open("feeds.xls"));
                mLogView.append("没开启自定义动态内容，使用默认文件\n");
            }else if (feedsPath.equals(config.getFeedsSourcePath())) {
                TextList.initFeedsXls(getResources().getAssets().open("feeds.xls"));
                mLogView.append("自定义动态内容使用默认文件\n");
            } else {
                if (feedsPath.startsWith(Config.getRootPath())){
                    feeds = new File(getSDPath(),feedsPath);
                }else {
                    feeds = new File(feedsPath);
                }

                if (!feeds.exists() || feeds.isDirectory()) {
                    mLogView.append("自定义动态内容文件不存在，使用默认文件\n");
                    TextList.initFeedsXls(getResources().getAssets().open("feeds.xls"));
                }else {
                    mLogView.append("自定义动态内容\n");
                    TextList.initFeeds(feeds);
                }
            }
        } catch (Exception e) {
            mLogView.append(e.getMessage() + "\n");
        }
        copyAssetsToSD();
    }
    private File getSDPath(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir;
        }
        return new File("/");
    }
    private void copyAssetsToSD(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            File path = new File(sdDir, Config.getRootPath());
            if (!path.exists()){
                path.mkdir();
            }
            File fileFeeds = new File(sdDir, config.getFeedsSourcePath());
            File fileMsg = new File(sdDir, config.getMsgSourcePath());

            if (!fileMsg.exists()) {
                try {
                    byte[] bytes = IO.read(getResources().getAssets().open("chat.xls"));
                    FileUtils.write(fileMsg, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!fileFeeds.exists()) {
                try {
                    byte[] bytes = IO.read(getResources().getAssets().open("feeds.xls"));
                    FileUtils.write(fileFeeds, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onDestroy() {

        stopService(intent);
        unbindService(conn);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent1,0);
                break;
            case R.id.about:
                Intent intent2 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent2);
                break;
            default:
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                setResult(RESULT_OK, intent1);
                finish();
                break;
            default:
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                boolean changeMsgPath = data.getBooleanExtra("changeMsgPath",false);
                boolean changeFeedPath = data.getBooleanExtra("changeFeedPath",false);
                if (changeMsgPath || changeFeedPath){
                    Message message = new Message();
                    message.what=1;
                    message.obj="自定义文件发生改变\n";
                    handler.sendMessage(message);
                    initTextList();
                }
                break;
            default:
        }
    }

    private class RunGo extends AsyncTask<Void, Void, Object> {

        RunGo() {
        }

        @Override
        protected Object doInBackground(Void... params) {
            run = new RunThread();
            run.start();
            return null;
        }

        @Override
        protected void onPostExecute(Object json) {
        }

        @Override
        protected void onCancelled() {
        }
    }
}
