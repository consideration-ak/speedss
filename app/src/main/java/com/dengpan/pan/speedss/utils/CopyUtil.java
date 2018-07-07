package com.dengpan.pan.speedss.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import static android.content.Context.CLIPBOARD_SERVICE;

public class CopyUtil {
    public static void copy(Context context,String content){
        ClipboardManager clip = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", content);
        clip.setPrimaryClip(myClip);
    }
}
