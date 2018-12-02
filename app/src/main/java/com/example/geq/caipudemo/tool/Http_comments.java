package com.example.geq.caipudemo.tool;

import android.util.Log;

import com.example.geq.caipudemo.vo.Comment;

import org.json.JSONArray;
import org.json.JSONException;
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
    private static List<Comment> commentList=new ArrayList<Comment>();
    private static HttpURLConnection connection;
    private static InputStream is;
    private static ByteArrayOutputStream baos;
    private static String param;

    public static List<Comment> getcomments(int menuid){
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
            JSONObject object = new JSONObject();
            try {
                object.put("menuid",+ menuid);
                param = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(param);
            byte[] bytes = param.getBytes();
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
                System.out.println("menus_length"+menus.length());
                for(int i=0;i<menus.length();i++){
                    JSONObject menu = menus.getJSONObject(i);
                    String mid = menu.getString("menuid");
                    String region = menu.getString("region");
                    JSONObject ptime = menu.getJSONObject("ptime");
                    String date = ptime.getString("date");
                    String hours = ptime.getString("hours");
                    String seconds = ptime.getString("seconds");
                    String month = ptime.getString("month");
                    String nanos = ptime.getString("nanos");
                    String timezoneOffset = ptime.getString("timezoneOffset");
                    String year = ptime.getString("year");
                    String minutes = ptime.getString("minutes");
                    String time = ptime.getString("time");
                    String day = ptime.getString("day");
                    String content=menu.getString("content");
                    int cid=menu.getInt("cid");
                    //Menuinfo Menuinfo=new Menuinfo(spic,assistmaterial,notlikes,menuname,abstracts,mainmaterial,menuid,typeid,likes);
                    Comment Comment =new Comment(mid,region,date,hours,seconds,month,nanos,timezoneOffset,year,minutes,time,day,content,cid);
                    commentList.add(Comment);
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

            Log.e("------------", "commentList: "+commentList.size());
           return commentList;
        }
    }
    }
