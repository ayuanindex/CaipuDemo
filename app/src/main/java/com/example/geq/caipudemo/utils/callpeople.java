package com.example.geq.caipudemo.utils;

import android.content.Intent;
import android.net.Uri;

//拨打电话
public class callpeople {
    public static void call(String phonenumber){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phonenumber));
    }
}
