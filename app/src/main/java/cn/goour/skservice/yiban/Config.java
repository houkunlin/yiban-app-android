/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

/**
 * Created by HouKunLin on 2017/7/25.
 */

public class Config implements Serializable{
    private static Config defaultConfig;

    private boolean autoLogin;//自动登录

    private boolean checkIn;//自动签到
    private boolean checkInToFeeds;//签到是否同步到动态

    private boolean feedsAdd;//自动发布动态
    private long feedsTimes;//发布动态间隔时间
    private int feedsNum;//发布动态条数
    private boolean feedsToWeiBo;//动态同步到微博
    private boolean feedsGps;//动态同步到微博

    private boolean feedsSourceForSD;//是否自定义动态内容
    private String feedsSourcePath;//自定义动态内容文件

    private boolean feedsZan;//是否点赞
    private boolean feedsTong;//是否同情
    private int feedsGetNum;//获取动态条数
    private boolean feedsZTInit;//点赞同情动态初始化

    private boolean feedsComments;//是否自动评论
    private boolean feedsCommentsNoAgent;//是否重复评论
    private boolean feedsComForSD;//是否自定义评论内容
    private String feedsComSDPath;//自定义评论内容文件

    private boolean msgSend;//是否自动发送消息
    private long msgTimes;//发送消息间隔
    private int msgNum;//发送消息条数

    private boolean msgSourceForSD;//是否自定义消息内容
    private String msgSourcePath;//自定义消息内容文件

    private boolean visitOrgHome;
    private boolean visitGroupHome;
    private boolean visitFriendHome;
    public static String rootPath = "/sKService/";

    private String gpsAddress;//自定义地址名称
    private String gpsLat;//自定义纬度
    private String gpsLng;//自定义经度
    public static String getTails(){
        return "\n------[小K服务]漂亮的小尾巴";
    }
    public Config() {
        this(false);
    }

    public Config(boolean isDefault) {
        if (isDefault){
            this.autoLogin = true;

            this.checkIn = true;
            this.checkInToFeeds = true;

            this.feedsAdd = true;
            this.feedsTimes = 5000;
            this.feedsNum = 20;
            this.feedsToWeiBo = false;
            this.feedsSourceForSD = false;
            this.feedsSourcePath = rootPath+"feeds.xls";
            this.feedsGps = false;

            this.feedsZan = true;
            this.feedsTong = true;
            this.feedsGetNum = 50;
            this.feedsZTInit = false;

            this.feedsComments = false;
            this.feedsCommentsNoAgent = true;
            this.feedsComForSD = false;
            this.feedsComSDPath = rootPath+"feeds.xls";

            this.msgSend = false;
            this.msgTimes = 5000;
            this.msgNum = 50;
            this.msgSourceForSD = false;
            this.msgSourcePath = rootPath+"chat.xls";

            this.visitOrgHome = false;
            this.visitGroupHome = false;
            this.visitFriendHome = false;

            this.gpsAddress = "天安门城楼";
            this.gpsLat = "39.908710";//39.908710
            this.gpsLng = "116.397500";//116.397500
        }
    }
    public static synchronized Config getDefaultInstance(){
        if (defaultConfig == null){
            synchronized (Config.class){
                if (defaultConfig == null){
                    defaultConfig = new Config(true);
                }
            }
        }
        return defaultConfig;
    }

    public Object getFieldValue(String fieldName){
        Object object = null;
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            if (field != null){
                field.setAccessible(true);
                object = field.get(this);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }
    public void setFieldValue(String fieldName,Object fieldValue){
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            if (field != null){
                field.setAccessible(true);
                field.set(this,fieldValue);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public boolean isAutoLogin() {
        return autoLogin;
    }

    public boolean isCheckIn() {
        return checkIn;
    }

    public boolean isCheckInToFeeds() {
        return checkInToFeeds;
    }

    public boolean isFeedsAdd() {
        return feedsAdd;
    }

    public long getFeedsTimes() {
        return feedsTimes;
    }

    public int getFeedsNum() {
        return feedsNum;
    }

    public boolean isFeedsToWeiBo() {
        return feedsToWeiBo;
    }

    public boolean isFeedsSourceForSD() {
        return feedsSourceForSD;
    }

    public String getFeedsSourcePath() {
        return feedsSourcePath;
    }

    public boolean isFeedsZan() {
        return feedsZan;
    }

    public boolean isFeedsTong() {
        return feedsTong;
    }

    public boolean isFeedsComments() {
        return feedsComments;
    }

    public boolean isFeedsCommentsNoAgent() {
        return feedsCommentsNoAgent;
    }

    public boolean isFeedsComForSD() {
        return feedsComForSD;
    }

    public String getFeedsComSDPath() {
        return feedsComSDPath;
    }

    public boolean isMsgSend() {
        return msgSend;
    }

    public long getMsgTimes() {
        return msgTimes;
    }

    public int getMsgNum() {
        return msgNum;
    }

    public boolean isMsgSourceForSD() {
        return msgSourceForSD;
    }

    public String getMsgSourcePath() {
        return msgSourcePath;
    }

    public boolean isVisitFriendHome() {
        return visitFriendHome;
    }

    public boolean isVisitGroupHome() {
        return visitGroupHome;
    }

    public boolean isVisitOrgHome() {
        return visitOrgHome;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckInToFeeds(boolean checkInToFeeds) {
        this.checkInToFeeds = checkInToFeeds;
    }

    public void setFeedsAdd(boolean feedsAdd) {
        this.feedsAdd = feedsAdd;
    }

    public void setFeedsTimes(long feedsTimes) {
        this.feedsTimes = feedsTimes;
    }

    public void setFeedsNum(int feedsNum) {
        this.feedsNum = feedsNum;
    }

    public void setFeedsToWeiBo(boolean feedsToWeiBo) {
        this.feedsToWeiBo = feedsToWeiBo;
    }

    public void setFeedsSourceForSD(boolean feedsSourceForSD) {
        this.feedsSourceForSD = feedsSourceForSD;
    }

    public void setFeedsSourcePath(String feedsSourcePath) {
        this.feedsSourcePath = feedsSourcePath;
    }

    public void setFeedsZan(boolean feedsZan) {
        this.feedsZan = feedsZan;
    }

    public void setFeedsTong(boolean feedsTong) {
        this.feedsTong = feedsTong;
    }

    public void setFeedsComments(boolean feedsComments) {
        this.feedsComments = feedsComments;
    }

    public void setFeedsCommentsNoAgent(boolean feedsCommentsNoAgent) {
        this.feedsCommentsNoAgent = feedsCommentsNoAgent;
    }

    public void setFeedsComForSD(boolean feedsComForSD) {
        this.feedsComForSD = feedsComForSD;
    }

    public void setFeedsComSDPath(String feedsComSDPath) {
        this.feedsComSDPath = feedsComSDPath;
    }

    public void setMsgSend(boolean msgSend) {
        this.msgSend = msgSend;
    }

    public void setMsgTimes(long msgTimes) {
        this.msgTimes = msgTimes;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }

    public void setMsgSourceForSD(boolean msgSourceForSD) {
        this.msgSourceForSD = msgSourceForSD;
    }

    public void setMsgSourcePath(String msgSourcePath) {
        this.msgSourcePath = msgSourcePath;
    }

    public void setVisitFriendHome(boolean visitFriendHome) {
        this.visitFriendHome = visitFriendHome;
    }

    public void setVisitGroupHome(boolean visitGroupHome) {
        this.visitGroupHome = visitGroupHome;
    }

    public void setVisitOrgHome(boolean visitOrgHome) {
        this.visitOrgHome = visitOrgHome;
    }

    public boolean isFeedsGps() {
        return feedsGps;
    }

    public void setFeedsGps(boolean feedsGps) {
        this.feedsGps = feedsGps;
    }

    public static String getRootPath() {
        return rootPath;
    }

    public static void setRootPath(String rootPath) {
        Config.rootPath = rootPath;
    }

    public String getGpsAddress() {
        return gpsAddress;
    }

    public void setGpsAddress(String gpsAddress) {
        this.gpsAddress = gpsAddress;
    }

    public String getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(String gpsLat) {
        this.gpsLat = gpsLat;
    }

    public String getGpsLng() {
        return gpsLng;
    }

    public void setGpsLng(String gpsLng) {
        this.gpsLng = gpsLng;
    }

    public int getFeedsGetNum() {
        return feedsGetNum;
    }

    public void setFeedsGetNum(int feedsGetNum) {
        this.feedsGetNum = feedsGetNum;
    }

    public boolean isFeedsZTInit() {
        return feedsZTInit;
    }

    public void setFeedsZTInit(boolean feedsZTInit) {
        this.feedsZTInit = feedsZTInit;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Config{");
        sb.append("autoLogin=").append(autoLogin);
        sb.append(", checkIn=").append(checkIn);
        sb.append(", checkInToFeeds=").append(checkInToFeeds);
        sb.append(", feedsAdd=").append(feedsAdd);
        sb.append(", feedsTimes=").append(feedsTimes);
        sb.append(", feedsNum=").append(feedsNum);
        sb.append(", feedsToWeiBo=").append(feedsToWeiBo);
        sb.append(", feedsGps=").append(feedsGps);
        sb.append(", feedsSourceForSD=").append(feedsSourceForSD);
        sb.append(", feedsSourcePath='").append(feedsSourcePath).append('\'');
        sb.append(", feedsZan=").append(feedsZan);
        sb.append(", feedsTong=").append(feedsTong);
        sb.append(", feedsComments=").append(feedsComments);
        sb.append(", feedsCommentsNoAgent=").append(feedsCommentsNoAgent);
        sb.append(", feedsComForSD=").append(feedsComForSD);
        sb.append(", feedsComSDPath='").append(feedsComSDPath).append('\'');
        sb.append(", msgSend=").append(msgSend);
        sb.append(", msgTimes=").append(msgTimes);
        sb.append(", msgNum=").append(msgNum);
        sb.append(", msgSourceForSD=").append(msgSourceForSD);
        sb.append(", msgSourcePath='").append(msgSourcePath).append('\'');
        sb.append(", visitFriendHome=").append(visitFriendHome);
        sb.append(", visitGroupHome=").append(visitGroupHome);
        sb.append(", visitOrgHome=").append(visitOrgHome);
        sb.append(", gpsAddress='").append(gpsAddress).append('\'');
        sb.append(", gpsLat='").append(gpsLat).append('\'');
        sb.append(", gpsLng='").append(gpsLng).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
