package cn.goour.skservice.yiban;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.List;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
    private static final String TAG = "SettingsActivity";
    private static SharedPreferences settings;
    private static Config config1 = Config.getDefaultInstance();
    private static boolean changeMsgPath = false;
    private static boolean changeFeedPath = false;
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            Log.e(TAG,preference.getKey()+"="+stringValue);
            Log.e(TAG,"getSummary="+preference.getSummary());
            String key1 = preference.getKey();
            switch (key1){
                case "autoLogin":
                    break;
                case "checkIn":
                    break;
                case "checkInToFeeds":
                    break;
                case "feedsAdd":
                    break;
                case "feedsToWeiBo":
                    break;
                case "feedsGps":
                    break;
                case "feedsSourceForSD":
                    break;
                case "feedsZan":
                    break;
                case "feedsTong":
                    break;
                case "feedsZTInit":
                    break;
                case "feedsComments":
                    break;
                case "feedsCommentsNoAgent":
                    break;
                case "feedsComForSD":
                    break;
                case "feedsComSDPath":
                    break;
                case "msgSend":
                    break;
                case "msgSourceForSD":
                    break;
                case "visitOrgHome":
                    break;
                case "visitGroupHome":
                    break;
                case "visitFriendHome":
                    break;
                case "msgSourcePath":
                    changeMsgPath = true;
                    preference.setSummary(stringValue);
                    break;
                case "feedsSourcePath":
                    changeFeedPath = true;
                    preference.setSummary(stringValue);
                    break;
                case "feedsTimes":
                case "feedsNum":
                case "feedsGetNum":
                case "msgTimes":
                case "msgNum":
                case "gpsAddress":
                case "gpsLat":
                case "gpsLng":
                    preference.setSummary(stringValue);
                    break;
                default:
                    preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        /*sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));*/

        String key1 = preference.getKey();
        Object value1 = config1.getFieldValue(key1);
        switch (key1){
            case "autoLogin":
                break;
            case "checkIn":
                break;
            case "checkInToFeeds":
                break;
            case "feedsAdd":
                break;
            case "feedsToWeiBo":
                break;
            case "feedsGps":
                break;
            case "feedsSourceForSD":
                break;
            case "feedsZan":
                break;
            case "feedsTong":
                break;
            case "feedsZTInit":
                break;
            case "feedsComments":
                break;
            case "feedsCommentsNoAgent":
                break;
            case "feedsComForSD":
                break;
            case "feedsComSDPath":
                break;
            case "msgSend":
                break;
            case "msgSourceForSD":
                break;
            case "visitOrgHome":
                break;
            case "visitGroupHome":
                break;
            case "visitFriendHome":
                break;
            case "feedsTimes":
            case "feedsNum":
            case "feedsSourcePath":
            case "msgSourcePath":
            case "feedsGetNum":
            case "msgTimes":
            case "msgNum":
            case "gpsAddress":
            case "gpsLat":
            case "gpsLng":
                setSummary(preference,key1,value1);
//                value = sharedPreferences.getString(key1, value1 == null ? "" : String.valueOf(value1));
//                preference.setSummary(value);
                break;
            default:
                setSummary(preference,key1,value1);
//                value = sharedPreferences.getString(key1, value1 == null ? "" : String.valueOf(value1));
//                preference.setSummary(value);
        }
    }
    private static void setSummary(Preference preference,String key,Object value1){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(preference.getContext());
        String value = sharedPreferences.getString(key, value1 == null ? "" : String.valueOf(value1));
        preference.setSummary(value);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setTitle("设置");
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        changeMsgPath = false;
        changeFeedPath = false;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("changeFeedPath",changeFeedPath);
                intent.putExtra("changeMsgPath",changeMsgPath);
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || LoginSet.class.getName().equals(fragmentName)
                || CheckInSet.class.getName().equals(fragmentName)
                || FeedsAddSet.class.getName().equals(fragmentName)
                || FeedsZTSet.class.getName().equals(fragmentName)
                || ChatSet.class.getName().equals(fragmentName)
                || ElseSet.class.getName().equals(fragmentName)
                || GPSSet.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.

            bindPreferenceSummaryToValue(findPreference("autoLogin"));
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class LoginSet extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_view_login);
            setHasOptionsMenu(true);
            getActivity().setTitle("登录设置");
            bindPreferenceSummaryToValue(findPreference("autoLogin"));

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class CheckInSet extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_view_checkin);
            setHasOptionsMenu(true);
            getActivity().setTitle("签到设置");
            bindPreferenceSummaryToValue(findPreference("checkIn"));
            bindPreferenceSummaryToValue(findPreference("checkInToFeeds"));

        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class FeedsAddSet extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_view_feeds_add);
            setHasOptionsMenu(true);
            getActivity().setTitle("发布动态设置");
            bindPreferenceSummaryToValue(findPreference("feedsAdd"));
            bindPreferenceSummaryToValue(findPreference("feedsTimes"));
            bindPreferenceSummaryToValue(findPreference("feedsNum"));
            bindPreferenceSummaryToValue(findPreference("feedsToWeiBo"));
            bindPreferenceSummaryToValue(findPreference("feedsSourceForSD"));
            bindPreferenceSummaryToValue(findPreference("feedsSourcePath"));
            bindPreferenceSummaryToValue(findPreference("feedsGps"));
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class FeedsZTSet extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_view_feeds_zt);
            setHasOptionsMenu(true);
            bindPreferenceSummaryToValue(findPreference("feedsZan"));
            bindPreferenceSummaryToValue(findPreference("feedsTong"));
            bindPreferenceSummaryToValue(findPreference("feedsGetNum"));
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ChatSet extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_view_chat);
            setHasOptionsMenu(true);
            getActivity().setTitle("聊天设置");
            bindPreferenceSummaryToValue(findPreference("msgSend"));
            bindPreferenceSummaryToValue(findPreference("msgTimes"));
            bindPreferenceSummaryToValue(findPreference("msgNum"));
            bindPreferenceSummaryToValue(findPreference("msgSourceForSD"));
            bindPreferenceSummaryToValue(findPreference("msgSourcePath"));
            bindPreferenceSummaryToValue(findPreference("group"));
            findPreference("group").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(),ChooseGroupActivity.class);
                    startActivityForResult(intent,0);
                    return false;
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (resultCode){
                case RESULT_OK:
                    String group = data.getStringExtra("group");
                    SharedPreferences.Editor edit = settings.edit();
                    edit.putString("group",group);
                    edit.apply();
                    bindPreferenceSummaryToValue(findPreference("group"));
                    break;
                default:
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ElseSet extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_view_else);
            setHasOptionsMenu(true);
            getActivity().setTitle("其他设置");
            bindPreferenceSummaryToValue(findPreference("visitFriendHome"));
            bindPreferenceSummaryToValue(findPreference("visitGroupHome"));
            bindPreferenceSummaryToValue(findPreference("visitFriendHome"));
            bindPreferenceSummaryToValue(findPreference("feedsZTInit"));
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GPSSet extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_view_gps);
            setHasOptionsMenu(true);
            getActivity().setTitle("地理位置设置");
            bindPreferenceSummaryToValue(findPreference("gpsAddress"));
            bindPreferenceSummaryToValue(findPreference("gpsLat"));
            bindPreferenceSummaryToValue(findPreference("gpsLng"));
            findPreference("goMap").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(),MapChoose.class);
                    startActivityForResult(intent,0);
                    return false;
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.e(TAG,"获取得到返回结果");
            super.onActivityResult(requestCode,resultCode,data);
            switch (resultCode){
                case RESULT_OK:
                    String address = data.getStringExtra("address");
                    String lat = data.getStringExtra("lat");
                    String lng = data.getStringExtra("lng");
                    Log.e(TAG,"address="+address+";lat="+lat+";lng="+lng);
                    SharedPreferences.Editor edit = settings.edit();
                    edit.putString("gpsAddress",address);
                    edit.putString("gpsLat",lat);
                    edit.putString("gpsLng",lng);
                    edit.apply();
                    bindPreferenceSummaryToValue(findPreference("gpsAddress"));
                    bindPreferenceSummaryToValue(findPreference("gpsLat"));
                    bindPreferenceSummaryToValue(findPreference("gpsLng"));
                    break;
                default:
            }
        }
    }
}
