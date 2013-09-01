package com.qinyuan.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.qinyuan.model.AsyncGeoTask;
import com.qinyuan.model.ExitNotify;

public class UserActivity extends MapActivity implements LocationListener,RecognizerDialogListener{
    private static BMapManager mBMapMan = null;
    private static MapViewClick mMapView = null;
    private MapController mMapController = null;
    private MKLocationManager locationManager = null;
    private MKSearch mkSearch = null;
    private static MyPositionOverlay myOverlay;
    private GeoPoint pt;
    //TextView tv;                   //显示当前位置显示
    static EditText et;			//显示打车目的地
    
    private Button speadButton; //语音按钮
    //缓存，保存当前的引擎参数到下一次启动应用程序使用.
  	private SharedPreferences mSharedPreferences;
  	//识别Dialog
  	private RecognizerDialog iatDialog;
  	
  	private AlertDialog.Builder builder; //目的地确认对话框
    
    private RadioButton radio0;
    private RadioButton radio1;
    private RadioButton radio2;
    private RadioButton radio3;
    
    private Button upMap;
    private Button downMap;
    
    String strInfo;
    String strInfo1;
    String strInfo2 = "点击屏幕确定目的地";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        
        //将此Activity添加到Mapplication中统一管理（退出程序时使用）
        Mapplication.getInstance().addActivity(this);
        
        //得到从LoginActivity传过来的初始GeoPoint pt
//        Bundle bundle = getIntent().getExtras();
//        String[] geoPoint = bundle.getStringArray("geoPoint");
//        pt = new GeoPoint(Integer.parseInt(geoPoint[0]),Integer.parseInt(geoPoint[1]));
        //主界面中地图的初始位置（天安门）
        pt = new GeoPoint((int) (39.915 * 1E6),(int) (116.404 * 1E6));
        
        //tv = (TextView)findViewById(R.id.addr);
        et = (EditText)this.findViewById(R.id.des);
        
        voiceButtonInit();    //初始化语音按钮
        bottomInit();        //初始化底部菜单
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

        mMapView = (MapViewClick) findViewById(R.id.bmapsView);
        mMapView.setDoubleClickZooming(false);//屏蔽双击放大功能
        mMapView.setBuiltInZoomControls(false);// 关闭默认的缩放控件

        mMapController = mMapView.getController();// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        mMapController.setZoom(12);// 设置地图zoom级别

        //设定屏幕中心位置
        mMapController.animateTo(pt);
        
      //初始化语音转写Dialog
      	iatDialog = new RecognizerDialog(this, "appid=509a5311");
      	iatDialog.setListener(this);

      //初始化语音缓存对象
      	mSharedPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
    }

    //监听位置变化事件
    public void onLocationChanged(Location location) {

    	if (location != null) {
        	pt.setLatitudeE6((int)(location.getLatitude() * 1E6));
        	pt.setLongitudeE6((int) (location.getLongitude() * 1E6));
            //mMapController.animateTo(pt);
            
            //自定义当前位置的覆盖物
            myOverlay = MyPositionOverlay.getInstance(pt, UserActivity.this, R.drawable.user);
            mMapView.getOverlays().add(myOverlay);
            
            //搜索当前地址信息
            mkSearch = new MKSearch();
            mkSearch.init(mBMapMan, new MKSearchListener()
    		{
    			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1){}
    			
    			public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1){}
    			
    			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1){}
    			
    			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2){}
    			
    			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1){}
    			
    			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1){}

    			public void onGetAddrResult(MKAddrInfo res, int error)
    			{
    				if (error != 0)
    				{
    					//String str = String.format("错误号: %d", error);
    					//tv.setText(str);
    					return;
    				}
    				
    				String street = "";
    				if(null != res.addressComponents.street){
    					street = res.addressComponents.street;
    				}
    				
//    				String streetNumber = "";
//    				if(null != res.addressComponents.street){
//    					streetNumber = res.addressComponents.street;
//    				}
    				
    				strInfo = res.addressComponents.district + street;
    				
    				//strInfo1 = res.addressComponents.district + street + streetNumber;
    				Log.v("-------->", strInfo);
    				//tv.setText(strInfo1);
    			}
    		});
            mkSearch.reverseGeocode(pt);
    		
        }
    }

    private void voiceButtonInit(){
    	speadButton = (Button)findViewById(R.id.speak);
    	
    	speadButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				showIatDialog();
			}
		});
    }
    
    private void bottomInit(){
    	radio0 = (RadioButton)findViewById(R.id.radio0);
    	radio1 = (RadioButton)findViewById(R.id.radio1);
    	radio2 = (RadioButton)findViewById(R.id.radio2);
    	radio3 = (RadioButton)findViewById(R.id.radio3);
    	
    	radio0.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mMapController.animateTo(pt);
			}
		});
    	
    	radio1.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mMapView.setTraffic(true);
			}
		});
    	
    	radio2.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//对打车按钮进行点击事件监听
		        MapViewClick.flag = true; //如果为false，则点击地图时不再改变目的地
				if("点击屏幕确定目的地".equals(et.getText().toString()) || et.getText().toString().equals("")){
						Toast t = Toast.makeText(UserActivity.this, "请先确定目的地", Toast.LENGTH_SHORT);
						t.show();
		        	}
					else{
						builder = new Builder(UserActivity.this);
						
						builder.setTitle(" 目的地确认");
						builder.setMessage("\t您确定现在要打车去往" + et.getText().toString().replaceAll("。", "") + "？");
						
						builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
						 	   public void onClick(DialogInterface dialog, int which) {
						 		   	Log.v("-------->", et.getText().toString());
						            AsyncGeoTask agt = new AsyncGeoTask(mMapView, UserActivity.this,strInfo,et.getText().toString());
						        	agt.execute(pt);
						        	MapViewClick.flag = false;
						        	mMapController.animateTo(pt);
						 	   }
						 	  });
						builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
						 	   public void onClick(DialogInterface dialog, int which) {
						 	    dialog.dismiss();
						 	   }
						 	  });
						builder.create().show();
					}
			}
		});
    	
    	radio3.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				UserActivity.this.openOptionsMenu();
			}
		});
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
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	menu.add(0, 1, 1, "卫星图");
        menu.add(0, 2, 2, "路线图");
        menu.add(0, 3, 3, "退出");
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == 1){
    		mMapView.setSatellite(true);
        }
    	else if(item.getItemId() == 2){
    		mMapView.setSatellite(false);
        }
        else if(item.getItemId() == 3){
        	new ExitNotify(this).dialog();
        } 
        return true;
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
   	 
        if (keyCode == KeyEvent.KEYCODE_BACK
                  && event.getRepeatCount() == 0) {
              new ExitNotify(this).dialog();
              return true;
          }
          return super.onKeyDown(keyCode, event);
      }
    
    //给MapViewClick提供当前BMapManager对象
    public static BMapManager getBMapManager(){
    	if(mBMapMan != null){
    		return mBMapMan;
    	}
    	return null;
    }
    
    //给MapViewClick提供EditText对象tv1
    public static EditText getEditText(){
    	if(null != et){
    		return et;
    	}
    	return null;
    }
  
    public void showIatDialog()
	{
		//获取引擎参数
		String engine = mSharedPreferences.getString(getString(R.string.preference_key_iat_engine),getString(R.string.preference_default_iat_engine));

		//获取area参数，POI搜索时需要传入.
		String area = null;
		if ("poi".equals(engine)) {
			final String defaultProvince = getString(R.string.preference_default_poi_province);
			String province = mSharedPreferences.getString(
					getString(R.string.preference_key_poi_province),
					defaultProvince);
			final String defaultCity = getString(R.string.preference_default_poi_city);
			String city = mSharedPreferences.getString(
					getString(R.string.preference_key_poi_city),
					defaultCity);

			if (!defaultProvince.equals(province)) {
				area = "search_area=" + province;
				if (!defaultCity.equals(city)) {
						area += city;
						}
					}
			}
		
		if(TextUtils.isEmpty(area))
			 area = "";
		else 
			area += ",";
		//设置转写Dialog的引擎和poi参数.
		iatDialog.setEngine(engine, area, null);

		//设置采样率参数，由于绝大部分手机只支持8K和16K，所以设置11K和22K采样率将无法启动录音. 
		String rate = mSharedPreferences.getString(
				getString(R.string.preference_key_iat_rate),
				getString(R.string.preference_default_iat_rate));
		if(rate.equals("rate8k"))
			iatDialog.setSampleRate(RATE.rate8k);
		else if(rate.equals("rate11k"))
			iatDialog.setSampleRate(RATE.rate11k);
		else if(rate.equals("rate16k"))
			iatDialog.setSampleRate(RATE.rate16k);
		else if(rate.equals("rate22k"))
			iatDialog.setSampleRate(RATE.rate22k);
		et.setText(null);
		//弹出转写Dialog.
		iatDialog.show();
		
		}
    
    @Override
	public void onEnd(SpeechError arg0)
	{
		
	}

	@Override
	public void onResults(ArrayList<RecognizerResult> results, boolean arg1)
	{
		StringBuilder builder = new StringBuilder();
		for (RecognizerResult recognizerResult : results) {
			builder.append(recognizerResult.text);
		}
		//builder.deleteCharAt(builder.length()-1);
		String text = builder.toString().replaceAll("。", "");
		et.append(text);
 		et.setSelection(et.length());
 		
 		if(mMapView.getOverlays().contains(DestinationOverlay.getInstance())){
 			mMapView.getOverlays().remove(DestinationOverlay.getInstance());
 			mMapView.invalidate();
 			Log.v("mmmmmmmmmmmmmmmm", "======================");
 		}
 		MapViewClick.flag = false;
	}
    
//class CustomGifView extends View {
//            public CustomGifView(Context context) { 
//                super(context); 
//                mMovie = Movie.decodeStream(getResources().openRawResource( 
//                        R.drawable.load));
//            } 
//            
//            public void onDraw(Canvas canvas) {
//                long now = android.os.SystemClock.uptimeMillis(); 
//                
//                if (mMovieStart == 0) { // first time 
//                    mMovieStart = now; 
//                } 
//                if (mMovie != null) { 
//                    
//                    int dur = mMovie.duration(); 
//                    if (dur == 0) { 
//                        dur = 1000; 
//                    } 
//                    int relTime = (int) ((now - mMovieStart) % dur);                
//                    mMovie.setTime(relTime); 
//                    mMovie.draw(canvas, 0, 0); 
//                    invalidate(); 
//                } 
//            }
//        }
}


