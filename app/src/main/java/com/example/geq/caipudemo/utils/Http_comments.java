package com.example.geq.caipudemo.utils;

import com.example.geq.caipudemo.vo.comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *5.取得评论
 */



public class Http_comments {
    private static List<comment> commentList=new ArrayList<comment>();
    private static HttpURLConnection connection;
    private static InputStream is;
    private static ByteArrayOutputStream baos;

    public static List<comment> getcomments(int menuid){
        URL url;
        try {
            url = new URL(values.Http_comments);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            StringBuffer stringBuffer=new StringBuffer();
            stringBuffer.append("menuid=").append(menuid);
            byte[] bytes = stringBuffer.toString().getBytes();
            connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(bytes);
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                String str = baos.toString();
                System.out.println(str);
                JSONObject jsonObject = new JSONObject(str);
                JSONArray menus = jsonObject.getJSONArray("comments");
                for(int i=0;i<menus.length();i++){
                    JSONObject menu = menus.getJSONObject(i);
                    String mid = menu.getString("menuid");
                    String region = menu.getString("region");
                    String ptime = menu.getString("ptime");
                    String date = menu.getString("date");
                    String hours = menu.getString("hours");
                    String seconds = menu.getString("seconds");
                    String month = menu.getString("month");
                    String nanos = menu.getString("nanos");
                    String timezoneOffset = menu.getString("timezoneOffset");
                    String year = menu.getString("year");
                    String minutes = menu.getString("minutes");
                    String time = menu.getString("time");
                    String day = menu.getString("day");
                   // menuinfo menuinfo=new menuinfo(spic,assistmaterial,notlikes,menuname,abstracts,mainmaterial,menuid,typeid,likes);
                    comment comment=new comment(mid,region,ptime,date,hours,seconds,month,nanos,timezoneOffset,year,minutes,time,day);
                    commentList.add(comment);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null && baos != null & connection != null) {
                try {
                    url = null;
                    is.close();
                    baos.close();
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           return commentList;
        }
    }
    }
