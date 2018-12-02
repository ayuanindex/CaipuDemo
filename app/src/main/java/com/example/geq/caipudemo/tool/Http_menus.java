package com.example.geq.caipudemo.tool;

import com.example.geq.caipudemo.vo.Menuinfo;
import com.example.geq.caipudemo.vo.Request_menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * 2.根据分类的 ID 检索菜谱列表
 */

public class Http_menus {
    private static List<Menuinfo> menuinfoList=null;
    private static HttpURLConnection connection;
    private static InputStream is;
    private static ByteArrayOutputStream baos;
    private static String param;
    private static PrintWriter out;

    public static List<Menuinfo> getmenus(Request_menu request){
        URL url;
        try {
            url = new URL(values.Http_mmenus);
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
               // object.put("pagesize",""+request.getPagesize());
                //object.put("stratid", ""+request.getStartid());
                object.put("typeid",request.getTypeid());
                param = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(param);
            byte[] bytes = param.getBytes();
            connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(bytes);
            menuinfoList=new ArrayList<Menuinfo>();
            System.out.println("code:   "+connection.getResponseCode());
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
                JSONArray menus = jsonObject.getJSONArray("menus");
                for(int i=0;i<menus.length();i++){
                    JSONObject menu = menus.getJSONObject(i);
                    String spic = menu.getString("spic");
                    String assistmaterial = menu.getString("assistmaterial");
                    String notlikes = menu.getString("notlikes");
                    String menuname = menu.getString("menuname");
                    String abstracts = menu.getString("abstracts");
                    String mainmaterial = menu.getString("mainmaterial");
                    String menuid = menu.getString("menuid");
                    String typeid = menu.getString("typeid");
                    String likes = menu.getString("likes");
                    Menuinfo Menuinfo =new Menuinfo(spic,assistmaterial,notlikes,menuname,abstracts,mainmaterial,menuid,typeid,likes);
                    menuinfoList.add(Menuinfo);
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
           return menuinfoList;
        }
    }
    private String JsonGet(String name, String pass) {
        JSONObject object = new JSONObject();
        try {
            object.put("name", name);
            object.put("pass", pass);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    }
