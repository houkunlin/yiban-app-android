/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban;

import android.inputmethodservice.Keyboard;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


/**
 * Created by HouKunLin on 2017/7/25.
 */

public class TextList {
    private static DecimalFormat df;
    private static List<String> chatText = new ArrayList<String>();
    private static List<String> feedsText = new ArrayList<String>();
    private static int chatLen = 0;
    private static int feedsLen = 0;
    private static String[] fileExt = new String[] { "xls" };
    static {
        df = (DecimalFormat) NumberFormat.getInstance();
        df.setMaximumFractionDigits(8);
    }

    public TextList() {
    }

    public static void initChatXls(InputStream inputStream) throws Exception {

        parseXLS(inputStream,chatText);
        chatLen = chatText.size();
    }
    /*public static void initChatXlsx(InputStream inputStream) throws Exception {

        parseXLSX(inputStream,chatText);
        chatLen = chatText.size();
    }*/
    public static void initFeedsXls(InputStream inputStream) throws Exception {

        parseXLS(inputStream,feedsText);
        feedsLen = feedsText.size();
    }
    /*public static void initFeedsXlsx(InputStream inputStream) throws Exception {

        parseXLSX(inputStream,feedsText);
        feedsLen = feedsText.size();
    }*/
    public static void initChat(File file) throws Exception {
        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())
                .toLowerCase();

        if (ArrayUtils.contains(fileExt, ext)){
            try {
                switch (ext) {
                    case "xls":
                        parseXLS(file,chatText);
                        break;
                    /*case "xlsx":
                        parseXLSX(file,chatText);
                        break;*/
                    default:
                        throw new Exception("聊天消息内容文件格式不支持");
                }
                chatLen = chatText.size();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("解析聊天消息内容文件错误",e);
            }
        }

    }
    public static void initFeeds(File file) throws Exception {
        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())
                .toLowerCase();

        if (ArrayUtils.contains(fileExt, ext)){
            try {
                switch (ext) {
                    case "xls":
                        parseXLS(file,feedsText);
                        break;
                    /*case "xlsx":
                        parseXLSX(file,feedsText);
                        break;*/
                    default:
                        throw new Exception("布动态内容文件格式不支持");
                }
                feedsLen = feedsText.size();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("解析发布动态内容文件错误",e);
            }
        }
    }
    public static String getChatText(){
        if (chatLen == 0){
            return "小K服务，我的易班小助手！";
        }
        int index = random(0,chatLen);
        return chatText.get(index);
    }
    public static String getFeedsText(){
        if (feedsLen == 0){
            return "小K服务，我的易班小助手！";
        }
        int index = random(0,feedsLen);
        return feedsText.get(index);
    }
    public static int random(int min,int max) {
        Random random = new Random();
        int re = random.nextInt(max);
        return re;
    }
    private static void parseXLS(File file,List<String> list) throws Exception{
        try {
            Workbook workbook = Workbook.getWorkbook(file);
            parseExcel(workbook,list);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*private static void parseXLSX(File file,List list) throws Exception{
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            parseExcel(workbook,list);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }*/
    private static void parseXLS(InputStream inputStream, List<String> list) throws Exception{
        try {
            Workbook workbook = Workbook.getWorkbook(inputStream);
            parseExcel(workbook,list);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /*private static void parseXLSX(InputStream inputStream,List list) throws Exception{
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            parseExcel(workbook,list);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }*/
    private static void parseExcel(Workbook workbook, List<String> list) throws Exception{
        Sheet sh = workbook.getSheet(0);
        list.clear();
//        int sheetNum = workbook.getNumberOfSheets();
        int sheetRows = sh.getRows();
//        int sheetColumns = sh.getColumns();
        for (int i = 0; i < sheetRows; i++) {
            list.add(sh.getCell(0,i).getContents());
        }
        workbook.close();
    }
}
