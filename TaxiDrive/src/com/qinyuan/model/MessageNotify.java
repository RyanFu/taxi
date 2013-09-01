package com.qinyuan.model;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.RouteOverlay;
import com.qinyuan.activity.R;
import com.qinyuan.activity.TaxiActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.Toast;

public class MessageNotify
{
	private Context activity;
	private MapView mMapView;
	private GeoPoint pt; //司机
	private String userAddr; 
	private String userID;
	private GeoPoint pt1; //乘客
	private BMapManager mBMapMan;
	
	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;
	
	public MessageNotify(Context activity,MapView mMapView,GeoPoint pt,String userAddr,String userID,GeoPoint pt1,BMapManager mBMapMan){
		this.activity = activity;
		this.mMapView = mMapView;
		this.pt = pt;
		this.pt1 = pt1;
		this.userAddr = userAddr;
		this.userID = userID;
		this.mBMapMan = mBMapMan;
	}
	
	public void dialog() {
	 	  builder = new Builder(activity);
	 	  builder.setMessage("在"+userAddr+"(具体以地图上显示的图标为准)有位乘客请求打车，您想接受吗？");
	 	  builder.setTitle("是否同意此客户的请求？").setCancelable(false);
	 	  
	 	  builder.setPositiveButton("接受", new OnClickListener() {
	 	   @Override
	 	   public void onClick(DialogInterface dialog, int which) {
	 	    
	 		  MKSearch mMKSearch = new MKSearch();
	 		  mMKSearch.init(mBMapMan, new MySearchListener()); 
	 		  
	 		  MKPlanNode start = new MKPlanNode();
	 		  start.pt = pt;
	 		  MKPlanNode end = new MKPlanNode();
	 		  end.pt = pt1;
	 		  // 设置驾车路线搜索策略，时间优先、费用最少或距离最短
	 		  mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
	 		  mMKSearch.drivingSearch(null, start, null, end);
	 		   
	 		 //在地图上显示打车用户的位置
	 		 UserPositionOverlay userOverlay = UserPositionOverlay.getInstance(pt1, activity, R.drawable.user);
	         mMapView.getOverlays().add(userOverlay);
	         mMapView.invalidate();
	         
	         HttpGet httpGet = new HttpGet("http://cloud.dlmu.edu.cn:9090/Taxi/AcceptResp?userID="+userID);
	 	    	HttpClient httpClient = new DefaultHttpClient();
	 	    	try
				{
					httpClient.execute(httpGet);
				}catch (Exception e)
				{
					Toast.makeText(activity, "无法获取数据，请检查网络连接", Toast.LENGTH_LONG).show();
				}
	 		 
	 	    dialog.dismiss();
	 	   }
	 	  });
	 	  builder.setNegativeButton("拒绝", new OnClickListener() {
	 	   @Override
	 	   public void onClick(DialogInterface dialog, int which) {
	 		  HttpGet httpGet = new HttpGet("http://cloud.dlmu.edu.cn:9090/Taxi/RefuseResp?userID="+userID);
	 	    	HttpClient httpClient = new DefaultHttpClient();
	 	    	try
				{
					httpClient.execute(httpGet);
				}catch (Exception e)
				{
					Toast.makeText(activity, "无法获取数据，请检查网络连接", Toast.LENGTH_LONG).show();
				}
	 	    	
	 	    dialog.dismiss();
	 	   }
	 	  });
	 	 alertDialog = builder.create();
	 	 alertDialog.show();
	 	 }
	
	public void dialogDisappear(){
		if(alertDialog.isShowing()){
			Log.v("showing----------", "showingshowing");
			alertDialog.dismiss();
		}
	}
	
	class MySearchListener implements MKSearchListener {    
		public void onGetAddrResult(MKAddrInfo result, int iError) {    }    
	    public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
	    	if (result == null) {
	    			return;    
	    		}    
	    	RouteOverlay routeOverlay = new RouteOverlay(TaxiActivity.getTaxiActivity(), mMapView);    // 此处仅展示一个方案作为示例    
	    	routeOverlay.setData(result.getPlan(0).getRoute(0));    
	    	mMapView.getOverlays().add(routeOverlay);	
			
		}     
	    public void onGetPoiResult(MKPoiResult result, int type, int iError) {    }     
	    public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {    }     
	    public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {    }    
	    public void onGetBusDetailResult(MKBusLineResult result, int iError) {    }    
	    public void onGetSuggestionResult(MKSuggestionResult result, int iError) {    }
	}
	
}


