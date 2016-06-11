package com.nerszaks.goeuro.utils;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.nerszaks.goeuro.MainActivity;
import com.nerszaks.goeuro.R;

public class PermissionUtils {

    public static final int PERMISSIONS_REQUESTED = 1;

    public static boolean checkPermission(MainActivity mActivity, String permission, boolean requestPermission) {
        int permissionCheck = ContextCompat.checkSelfPermission(mActivity, permission);
        if (PackageManager.PERMISSION_GRANTED == permissionCheck) {
            return true;
        } else if (PackageManager.PERMISSION_DENIED == permissionCheck && requestPermission) {
            Toast.makeText(mActivity, R.string.RequestedPermission, Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{permission},
                    PERMISSIONS_REQUESTED);
            return false;
        }
        return false;
    }


}
