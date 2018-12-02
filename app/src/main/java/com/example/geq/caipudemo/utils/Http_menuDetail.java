package com.example.geq.caipudemo.utils;

import com.example.geq.caipudemo.vo.MenuDetail;
import com.example.geq.caipudemo.vo.Step;

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
 * 3.获取菜谱详情
 */
public class Http_menuDetail {
    private static List<Step> stepList=new ArrayList<Step>();
    private static HttpURLConnection connection;
    private static InputStream is;
    private static ByteArrayOutputStream baos;
    private static MenuDetail menuDetail=null;

    public static MenuDetail getmenus(int mid){
        URL url;
        try {
            url = new URL(values.Http_menuDetail);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            StringBuffer stringBuffer=new StringBuffer();
            stringBuffer.append("menuid=").append(mid);
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
                JSONObject menu = jsonObject.getJSONObject("menu");
                String spic = menu.getString("spic");
                String assistmaterial = menu.getString("assistmaterial");
                String notlikes = menu.getString("notlikes");
                String menuname = menu.getString("menuname");
                String abstracts = menu.getString("abstracts");
                String mainmaterial = menu.getString("mainmaterial");
                String menuid = menu.getString("menuid");
                String typeid = menu.getString("typeid");
                String likes = menu.getString("likes");
                JSONArray steps = jsonObject.getJSONArray("steps");
                for(int i=0;i<steps.length();i++){
                    JSONObject step = steps.getJSONObject(i);
                    String stepid = step.getString("stepid");
                    String description = step.getString("description");
                    String menuid1 = step.getString("menuid");
                    String pic = step.getString("pic");
                    //menuinfo menuinfo=new menuinfo(spic,assistmaterial,notlikes,menuname,abstracts,mainmaterial,menuid,typeid,likes);
                    Step step1=new Step(stepid,description,menuid1,pic);
                    stepList.add(step1);
                }
                menuDetail = new MenuDetail(spic,assistmaterial,notlikes,menuname,abstracts,mainmaterial,menuid,typeid,likes,stepList);
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
           return menuDetail;
        }
    }
    }
