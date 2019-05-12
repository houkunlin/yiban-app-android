/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import cn.goour.utils.io.IO;

public class AboutActivity extends Activity {

    private TextView mAboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        try {
            byte[] re = IO.read(getResources().getAssets().open("about.txt"));
            mAboutText.setText(new String(re));
        }catch (Exception e){

        }
    }

    private void initView() {
        mAboutText = findViewById(R.id.aboutText);
    }
}
