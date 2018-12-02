package com.example.geq.caipudemo.tool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 4.菜谱的评价按钮
 */
public class Http_support{
    private static String result="err";
    private static HttpURLConnection connection;
    private static InputStream is;
    private static ByteArrayOutputStream baos;
    private static String param;

    public static String support(int menuid, String yes){
        URL url;
        try {
            url = new URL(values.Http_support);
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
                object.put("menuid",menuid);
                object.put("like",yes);
                param = object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  System.out.println(param);
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
                //  System.out.println(str);
                JSONObject jsonObject = new JSONObject(str);
                result = jsonObject.getString("result");
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
            return result;
        }
    }

}
