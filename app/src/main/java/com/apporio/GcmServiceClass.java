package com.apporio;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.apporio.demotaxiappdriver.manager.SessionManager;
import com.apporio.demotaxiappdriver.samwork.ApiManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by lenovo on 3/11/2018.
 */

public class GcmServiceClass extends GcmTaskService implements ApiManager.APIFETCHER {

    private Handler mHandler = new Handler();

    @Override
    public void onInitializeTasks() {
        // When your package is removed or updated, all of its network tasks are cleared by
        // the GcmNetworkManager. You can override this method to reschedule them in the case of
        // an updated package. This is not called when your application is first installed.
        //
        // This is called on your application's main thread.

        // TODO(developer): In a real app, this should be implemented to re-schedule important tasks.
        Log.e("onInitialize","Vishal");
        Toast.makeText(getApplicationContext(),"Vishal",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onRunTask(TaskParams taskParams) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                Log.e("onRunTask","NRUNTASK");
                Toast.makeText(getApplicationContext(),"VishalonRunTask",Toast.LENGTH_SHORT).show();
            }

        });

        return 0;
    }

    @Override
    public void onAPIRunningState(int a, String APINAME) {

    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {

    }

    @Override
    public void onFetchResultZero(String script) {

    }
}
