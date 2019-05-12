/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.goour.skservice.yiban.thread.RunThread;

public class ChooseGroupActivity extends Activity {
    private static final String TAG = "ChooseGroupActivity";
    private ListView mGroupList;
    private List<JSONObject> groups = new ArrayList<>();
    private ArrayAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);
        setTitle("选择聊天群组对象");
        initView();
        ad = new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,groups){
            public View getView(int position, View convertView, ViewGroup parent){
                TextView tv =new TextView(getContext());
                String text = groups.get(position).getString("name");
                tv.setText(text);
                tv.setTextSize(24);
                Log.e(TAG,"在适配器里面："+text);
                return tv;
            }
        };
        mGroupList.setAdapter(ad);
        mGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.ChatSet.class);
                intent.putExtra("group",groups.get(i).toJSONString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        ad.notifyDataSetChanged();
        new GetGroups().execute();
    }

    private void initView() {
        mGroupList = findViewById(R.id.groupList);
    }
    class GetGroups extends AsyncTask<Void,Void,JSONObject>{

        @Override
        protected void onPostExecute(JSONObject json) {
            groups.clear();
            if (json != null){
                if (json.getIntValue("response") == 100){
                    JSONArray arr = json.getJSONObject("data").getJSONArray("groups");
                    for (Object o:arr) {
                        JSONObject item = (JSONObject) o;
                        groups.add(item);
                    }
                    ad.notifyDataSetChanged();
                    Log.e(TAG,"这是一个有效的数据，群组有："+groups.size());
                }else {
                    Toast.makeText(getApplicationContext(),"获取群组列表错误："+json.getString("message"),Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"获取群组列表错误",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject json = null;
            try {
                json = ActionManage.getAction().getUsersGroupsAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e(TAG,"获取群组列表完成："+json);
            return json;
        }
    }
}
