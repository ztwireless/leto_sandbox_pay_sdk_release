package com.leto.sandbox.sdk.demo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public class CheckPermissionUtils {
    private CheckPermissionUtils() {
    }

    //检测权限
    public static String[] checkPermission(Context context, String[] perms) {
        List<String> data = new ArrayList<>();//存储未申请的权限
        for (String permission : perms) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(context, permission);
            Log.d("test", "permission " + permission + " check result: " + checkSelfPermission);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {//未申请
                data.add(permission);
            }
        }
        return data.toArray(new String[data.size()]);
    }
}
