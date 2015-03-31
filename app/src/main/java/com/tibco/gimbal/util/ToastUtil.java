package com.tibco.gimbal.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tangjian on 5/3/15.
 * email:tangjian19900607@gmail.com
 * QQ:562980080
 * WeChat:ITnan562980080
 */
public final class ToastUtil {

    public static void show(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
