package com.qinyuan.activity;

import android.app.Application;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;

public class MapManagerApplication extends Application {
    BMapManager bMapManager = null;
    
    static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetNetworkState(int iError) {

        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                
            }
        }
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        bMapManager = new BMapManager(this);
        bMapManager.init("C1652FDE7DFAB00ADDCABEA25284045B8C9DC974", new MyGeneralListener());
    }    
}
