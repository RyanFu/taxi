package com.qinyuan.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.qinyuan.action.CallTaxi;
import com.qinyuan.model.ExitNotify;
import com.qinyuan.model.MessageNotify;
import com.qinyuan.model.MyPositionOverlay;
import com.qinyuan.model.TipHelper;

public class TaxiActivity extends MapActivity implements LocationListener,SynthesizerPlayerListener{
    private BMapManager mBMapMan = null;
    private MapView mMapView = null;
    private MapController mMapController = null;
    private MKLocationManager locationManager = null;
    private static MyPositionOverlay myOverlay;
    private GeoPoint pt;  //司机
    private GeoPoint pt1; //乘客
    private Button butt;
    
    private Button upMap;
    private Button downMap;
    
    private MyHandler mHandler = null;
    
    private String toVoiceText;  //用于合成语音的文字
    //缓存对象.
  	private SharedPreferences mSharedPreferences;
  	//合成对象.
  	private SynthesizerPlayer mSynthesizerPlayer;  		
  	//缓冲进度
  	private int mPercentForBuffering = 0;
  	//播放进度
  	private int mPercentForPlaying = 0;
  	
  	private static Activity taxiActivity;
  	public static Activity getTaxiActivity(){
  			return taxiActivity;
  	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        
        this.taxiActivity = this;
        
        //将此Activity添加到Mapplication中统一管理（退出程序时使用）
        Mapplication.getInstance().addActivity(this);
        
        //得到从LoginActivity传过来的初始GeoPoint pt
//        Bundle bundle = getIntent().getExtras();
//        String[] geoPoint = bundle.getStringArray("geoPoint");
//        pt = new GeoPoint(Integer.parseInt(geoPoint[0]),Integer.parseInt(geoPoint[1]));
        //主界面中地图的初始位置（天安门）
          pt = new GeoPoint((int) (39.915 * 1E6),(int) (116.404 * 1E6));
          
        mapExtendInit();    //初始化自定义地图缩放控件
        
        mBMapMan = ((MapManagerApplication) getApplication()).bMapManager;
        if (mBMapMan == null) {
            ((MapManagerApplication) getApplication()).bMapManager = new BMapManager(
                    getApplication());
            ((MapManagerApplication) getApplication()).bMapManager.init(
                    "C1652FDE7DFAB00ADDCABEA25284045B8C9DC974",
                    new MapManagerApplication.MyGeneralListener());
            mBMapMan = ((MapManagerApplication) getApplication()).bMapManager;
        }
        super.initMapActivity(mBMapMan);

        locationManager = mBMapMan.getLocationManager();
        locationManager.enableProvider((int) MKLocationManager.MK_GPS_PROVIDER);
        locationManager.requestLocationUpdates(this);

        mMapView = (MapView) findViewById(R.id.bmapsView);
        mMapView.setBuiltInZoomControls(false);// 设置启用默认的缩放控件
        mMapView.setDoubleClickZooming(false);//屏蔽双击放大功能

        mMapController = mMapView.getController();// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        mMapController.setZoom(12);// 设置地图zoom级别

//        MyLocationOverlay mylocTest = new MyLocationOverlay(this, mMapView);
//        mylocTest.enableMyLocation();
//        mylocTest.enableCompass();
//        mMapView.getOverlays().add(mylocTest);
        //设定屏幕中心位置
        mMapController.animateTo(pt);
        
        //设置“公开自己的位置”按钮
        butt = (Button)findViewById(R.id.but);
        butt.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				butt.setVisibility(View.INVISIBLE);
				Toast t = Toast.makeText(TaxiActivity.this, "位置已公开为客户可见", Toast.LENGTH_LONG);
	            t.show();
	            
	            new AsyncGeoTask().start();
			}
		});
        
        //语音缓冲
        mSharedPreferences = getSharedPreferences(getPackageName(),
				MODE_PRIVATE);
}
    
    //监听位置变化事件
    public void onLocationChanged(Location location) {

    	if (location != null) {
        	pt.setLatitudeE6((int)(location.getLatitude() * 1E6));
        	pt.setLongitudeE6((int) (location.getLongitude() * 1E6));
            mMapController.animateTo(pt);
            
          //自定义当前位置的覆盖物
            myOverlay = MyPositionOverlay.getInstance(pt, TaxiActivity.this, R.drawable.taxi);
            mMapView.getOverlays().add(myOverlay);
            
            
    		//taxiOverlay1 = TaxiPositionOverlay.getInstance(pt, TestActivity.this, R.drawable.taxi);
            //mMapView.getOverlays().add(taxiOverlay1);
        }
    }
    
    private void mapExtendInit(){
    	upMap = (Button)findViewById(R.id.upMap);
    	downMap = (Button)findViewById(R.id.downMap);
    	
    	upMap.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mMapController.zoomIn();
			}
		});
    	
    	downMap.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mMapController.zoomOut();
			}
		});
    }
    
   @Override
   protected void onStart()
   {
	   super.onStart();
	   //删除通知栏的提示信息
	   NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	   nm.cancel(R.id.but);
   }
    @Override
    protected void onDestroy() {
        if (mBMapMan != null) {
            mBMapMan.destroy();
            ((MapManagerApplication) getApplication()).bMapManager = null;
            mBMapMan = null;
        }
        
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mBMapMan != null) {
            mBMapMan.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBMapMan != null) {
            mBMapMan.start();
        }
        super.onResume();
    }

    @Override
    protected void onStop()
    {
    	if (null != mSynthesizerPlayer) {
			mSynthesizerPlayer.cancel();
		}
    	super.onStop();
    }
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    //设置菜单项
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	menu.add(0, 1, 1, "路况");
    	menu.add(0, 2, 2, "路线图");
        menu.add(0, 3, 3, "卫星图");
        menu.add(0, 4, 4, "退出");
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == 1){
    		mMapView.setTraffic(true);
        }
    	else if(item.getItemId() == 2){
    		mMapView.setSatellite(false);
        }
        else if(item.getItemId() == 3){
        	mMapView.setSatellite(true);
        }
        else if(item.getItemId() == 4){
        	new ExitNotify(this).dialog();
        }
        return true;
    }
    
    //后退按钮监听事件，点击时隐藏当前Activity，并在通知栏显示此应用程序图标
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	PackageManager pm = getPackageManager();  
        ResolveInfo homeInfo = 
            pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0); 
        if (keyCode == KeyEvent.KEYCODE_BACK) {
              //new ExitNotify(this).dialog();
	            ActivityInfo ai = homeInfo.activityInfo;  
	            Intent startIntent = new Intent(Intent.ACTION_MAIN);  
	            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
	            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));  
	            startActivitySafely(startIntent);
        	
        		Intent intent = new Intent(this,StatusService.class);
        		startService(intent);
        		Log.v("-=-=-=-=-", "kkkgggggk");
            	//return false;
          }
        		return super.onKeyDown(keyCode, event);
      }

    private void startActivitySafely(Intent intent) {  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        try {  
            startActivity(intent);  
        } catch (ActivityNotFoundException e) {  
            Toast.makeText(this, "null",  
                    Toast.LENGTH_SHORT).show();  
        } catch (SecurityException e) {  
            Toast.makeText(this, "null",  
                    Toast.LENGTH_SHORT).show();   
        }  
    }

//处理AsyncGeoTask中后台线程与当前Activity的通信，当有用户请求打车时就在当前Activity中显示对应对话框   
class MyHandler extends Handler{
	
		public MyHandler(Looper looper){
			super(looper);
		}
       
    	@Override
    	public void handleMessage(Message msg)
    	{
    		String mess = (String)msg.obj;
        	if(!mess.equals("")){
        		String[] res = mess.split("&");
        		double lon = Double.parseDouble(res[0]);
        		double lat = Double.parseDouble(res[1]);
        		String userID = res[2];
        		String userAddr = res[3];
        		String destination = res[4];
        		Log.v("[[[[[[[[[[[[[[[[[[", lon+"\n"+lat+"\n"+userAddr);
        		
        		pt1 = new GeoPoint((int)(lat*1e6),(int)(lon*1e6));
        		
        		//播放语音
        		toVoiceText = "有人正在"+userAddr+"请求打车,去往"+destination;
        		synthetizeInSilence();
        		
        		//响铃提醒（手机默认铃声）
        		TipHelper.PlaySound(TaxiActivity.this);
        		//显示提示信息
        		MessageNotify mn = new MessageNotify(TaxiActivity.this, mMapView, pt, userAddr,userID,pt1,mBMapMan);
        		mn.dialog();
        		
        		new CallTaxi().execute(mn);
    		}
        	Log.v(">>>>>>>>>>>>>", mess);
    	}
} 

class AsyncGeoTask extends Thread
    {  	
    	@Override
    	public void run()
    	{
    		double lon = pt.getLongitudeE6()/1e6;
    		double lat = pt.getLatitudeE6()/1e6;
    		
    		while(true){
//    			HttpParams httpParameters = new BasicHttpParams();
//    	    	HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
//    	    	HttpConnectionParams.setSoTimeout(httpParameters, 10000);
//    	    	HttpConnectionParams.setTcpNoDelay(httpParameters, true);
    	    	
    	    	HttpGet httpGet = new HttpGet("http://cloud.dlmu.edu.cn:9090/Taxi/CarHandler?lon="+lon+"&lat="+lat+"&carID=116622");
    	    	HttpClient httpClient = new DefaultHttpClient();
    	    	InputStream is = null;
    	    	String backMess = "";
    	    	try{
    	    		HttpResponse httpResp = httpClient.execute(httpGet);
    	    		HttpEntity httpEntity = httpResp.getEntity();
    	    		is = httpEntity.getContent();
    	    		BufferedReader br = new BufferedReader(new InputStreamReader(is));
    	    		
    	    		String line = "";
    	    		while(null != (line = br.readLine())){
    	    			backMess += line;
    	    		}
    	    		
    	    	}catch(Exception e){
    	    		//Toast.makeText(TaxiActivity.this, "无法获取数据，请检查网络连接", Toast.LENGTH_LONG).show();
    	    	}
    	    	
    	    	if(!"".equals(backMess)){
    	    		Log.v("---------->", backMess);
    				driverNotify(backMess);
    			}
    	    	try
    			{
    				Thread.sleep(3000);
    			}
    			catch (InterruptedException e)
    			{
    				//Toast.makeText(TaxiActivity.this, "程序暂时无法响应，请稍候", Toast.LENGTH_SHORT).show();
    			}
    		}
    	}

    	private void driverNotify(String result)
    	{
    		//Looper curLooper = Looper.myLooper();
            Looper mainLooper = Looper.getMainLooper();
            String msg;
//            if (curLooper == null ){
            	   mHandler = new MyHandler(mainLooper);
                   msg = result;
//            } else {
//                   mHandler = new MyHandler(curLooper);
//                   msg = result ;
//            }
            mHandler.removeMessages(0);
            Message m = mHandler.obtainMessage(1, 1, 1, msg);
            mHandler .sendMessage(m);
    	}
    }
 
/**
 * 使用SynthesizerPlayer合成语音，不弹出合成Dialog.
 * @param
 */
private void synthetizeInSilence() {
	if (null == mSynthesizerPlayer) {
		//创建合成对象.
		mSynthesizerPlayer = SynthesizerPlayer.createSynthesizerPlayer(
				this, "appid=509a5311");
	}

	//设置合成发音人.
	String role = mSharedPreferences.getString(
			getString(R.string.preference_key_tts_role),
			getString(R.string.preference_default_tts_role));
	mSynthesizerPlayer.setVoiceName(role);

	//设置发音人语速
	int speed = mSharedPreferences.getInt(
			getString(R.string.preference_key_tts_speed),
			50);
	mSynthesizerPlayer.setSpeed(speed);

	//设置音量.
	int volume = mSharedPreferences.getInt(
			getString(R.string.preference_key_tts_volume),
			50);
	mSynthesizerPlayer.setVolume(volume);

	//设置背景音.
	String music = mSharedPreferences.getString(
			getString(R.string.preference_key_tts_music),
			getString(R.string.preference_default_tts_music));
	mSynthesizerPlayer.setBackgroundSound(music);

	//获取合成文本.
	String source = null;
	if (!("").equals(toVoiceText)) {
		source = toVoiceText;
	}

	//进行语音合成.
	mSynthesizerPlayer.playText(source, null,this);
	//mToast.setText(String.format(getString(R.string.tts_toast_format), 0, 0));
	//mToast.show();
}

/**
 * SynthesizerPlayerListener的"播放进度"回调接口.
 * @param percent,beginPos,endPos
 */
@Override
public void onBufferPercent(int percent,int beginPos,int endPos) {
	mPercentForBuffering = percent;
}

/**
 * SynthesizerPlayerListener的"开始播放"回调接口.
 * @param 
 */
@Override
public void onPlayBegin() {
}

/**
 * SynthesizerPlayerListener的"暂停播放"回调接口.
 * @param 
 */
@Override
public void onPlayPaused() {
}

/**
 * SynthesizerPlayerListener的"播放进度"回调接口.
 * @param percent,beginPos,endPos
 */
@Override
public void onPlayPercent(int percent,int beginPos,int endPos) {
	mPercentForPlaying = percent;
}


/**
 * SynthesizerPlayerListener的"恢复播放"回调接口，对应onPlayPaused
 * @param 
 */
@Override
public void onPlayResumed() {
}

/**
 * SynthesizerPlayerListener的"结束会话"回调接口.
 * @param error
 */
@Override
public void onEnd(SpeechError error) {
}

}





