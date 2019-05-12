/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.ArrayUtils;

import cn.goour.utils.Regex.RegexUtils;
import cn.goour.utils.io.IO;
import cn.goour.yiban.app.action.YiBanRequest;
import cn.goour.yiban.app.entity.User;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private ProgressBar mLoginProgress;
    private AutoCompleteTextView mAccount;
    private EditText mPassword;
    private Button mBtnLogin;
    private ScrollView mLoginForm;
    private SharedPreferences sp;
    private Handler handler;
    private SharedPreferences settings;
    private TextView mUpdateLog;
    private static String[] userIds = {"","",""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);
        initView();
        sp = getSharedPreferences("sKServiceYiban", Context.MODE_PRIVATE);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        ActionManage.setSettings(settings);
        ActionManage.setsKSYSettings(sp);
        String ac = sp.getString("account", "");
        String pa = sp.getString("password", "");
        mAccount.setText(ac);
        mPassword.setText(pa);
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mBtnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        /*User user = ActionManage.getUser();
                        if (ArrayUtils.contains(userIds,user.getUser_id())){

                        }*/
                        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                        startActivityForResult(intent1, 0);
                        break;
                    default:
                }
            }
        };
        try {
            byte[] re = IO.read(getResources().getAssets().open("log.txt"));
            mUpdateLog.setText(new String(re));
        }catch (Exception e){

        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mAccount.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccount.getText().toString();
        String password = mPassword.getText().toString();

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("account", account);
        editor.putString("password", password);
        editor.apply();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            mAccount.setError(getString(R.string.error_field_required));
            focusView = mAccount;
            cancel = true;
        } else if (!RegexUtils.isValidPhoneNum(account)) {
            mAccount.setError(getString(R.string.error_invalid_account));
            focusView = mAccount;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(account, password);
            mAuthTask.execute((Void) null);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mLoginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void initView() {
        mLoginProgress = findViewById(R.id.login_progress);
        mAccount = findViewById(R.id.account);
        mPassword = findViewById(R.id.password);
        mBtnLogin = findViewById(R.id.btnLogin);
        mLoginForm = findViewById(R.id.login_form);
        mUpdateLog = (TextView) findViewById(R.id.updateLog);
    }

    private class UserLoginTask extends AsyncTask<Void, Void, JSONObject> {

        private final String account;
        private final String password;

        UserLoginTask(String account, String password) {
            this.account = account;
            this.password = password;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject json;
            try {
                User user = new User(account, password);
                json = YiBanRequest.getLogin(user);
            } catch (Exception e) {
                e.printStackTrace();
                json = new JSONObject();
                json.put("message", "" + e.getMessage());
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            mAuthTask = null;
            boolean ok = false;
            if (json != null) {
                if (json.containsKey("response") && json.getIntValue("response") == 100) {
                    User user = new User(json);
                    ActionManage.setUser(user);
                    ok = true;
                } else {
                    mPassword.setError(json.getString("message"));
                    mPassword.requestFocus();
                }
            } else {
                mPassword.setError("未知错误");
                mPassword.requestFocus();
            }
            showProgress(false);
            if (ok) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                finish();
                break;
            default:
        }
    }
}

