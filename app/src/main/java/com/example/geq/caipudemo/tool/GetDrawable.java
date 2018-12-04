package com.example.geq.caipudemo.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//获取网络图片对象
public class GetDrawable {
	static Drawable drawable;
	private static URL url;
	private static String imgurl;
	private static Bitmap bitmap;

	public static Drawable getdrawable(final String path, final Context context) {
		imgurl = values.Http + path;
		url = null;
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					url = new URL(imgurl);
					InputStream stream = url.openStream();
					drawable = Drawable.createFromResourceStream(context.getResources(), null, stream, "src", null);
					Drawable.createFromStream(url.openStream(), "");
					drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return drawable;
	}

	//使用缓存储存图片(bitmap)
	public static Bitmap getBitmap(final String path, final Context context) {
		bitmap = null;
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					File file = new File(context.getCacheDir(), Base64.encodeToString(path.getBytes(), Base64.DEFAULT));
					if (file.exists() && file.length() > 0) {
						bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
					} else {
						System.out.println("第一次加载时自动保存在缓存");
						URL url = new URL(values.Http + path);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						conn.setConnectTimeout(5000);
						int code = conn.getResponseCode();
						System.out.println(conn);
						if (code == 200) {
							InputStream in = conn.getInputStream();
							FileOutputStream fos = new FileOutputStream(file);
							int len = -1;
							byte[] buffer = new byte[1024];
							while ((len = in.read(buffer)) != -1) {
								fos.write(buffer, 0, len);
							}
							fos.close();
							in.close();
							bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
