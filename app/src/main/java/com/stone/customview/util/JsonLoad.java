package com.stone.customview.util;

import android.content.Context;

import com.google.gson.Gson;
import com.stone.customview.model.PuzzleBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/5/28 17 10
 */
public class JsonLoad {

    public static PuzzleBean readLocalJson(Context context,  String fileName){
        PuzzleBean bean = null;
        try {
            String jsonString="";
            String resultString="";
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                    context.getResources().getAssets().open(fileName)));
            while ((jsonString=bufferedReader.readLine())!=null) {
                resultString+=jsonString;
            }
            bean = new Gson().fromJson(resultString, PuzzleBean.class);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bean;
    }

//    public static String readLocalJson(Context context,  String fileName){
//        String jsonString="";
//        String resultString="";
//        try {
//            InputStream inputStream=context.getResources().getAssets().open(fileName);
//            byte[] buffer=new byte[inputStream.available()];
//            inputStream.read(buffer);
//            resultString=new String(buffer,"GB2312");
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//        return resultString;
//    }
}
