package com.apporio.demotaxiappdriver;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;

import java.util.List;



import com.sampermissionutils.AfterPermissionGranted;
import com.sampermissionutils.AppSettingsDialog;
import com.sampermissionutils.EasyPermissions;


public class TrialSplashActivity extends BaseActivity  implements EasyPermissions.PermissionCallbacks {


    private static final String TAG = "SplashActivity";
    private static final int RC_LOCATION_CONTACTS_CAMERA_EXTERNALSTORAGE_PERM = 124;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_splash);




    }

    @Override
    protected void onResumeWithConnectionState(boolean connectivityStatus) {
        if(connectivityStatus){  // that is connected to the internet
            permissiontask();
        }else {
            // the work is already done in base activity class to reconnection Dialog
        }
    }


//  permissions tasks

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_CAMERA_EXTERNALSTORAGE_PERM)
    public void permissiontask() {
        String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE , Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.CAMERA };
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_permission_rationale_msg), RC_LOCATION_CONTACTS_CAMERA_EXTERNALSTORAGE_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


}
