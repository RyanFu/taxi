package com.qinyuan.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;

public class OverItemT extends ItemizedOverlay<OverlayItem> {
    public static List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
    @SuppressWarnings("unused")
	private String jsonData;
    private Context mContext;
    private String strInfo;
    private List<String> carID = new ArrayList<String>();
    private MapView mMapView;
    private String destination;
 
    public OverItemT(Drawable marker, Context context,String jsonData,String strInfo,MapView mMapView,String destination) {
        super(boundCenterBottom(marker));
 
        this.mContext = context;
        this.jsonData = jsonData;
        this.strInfo = strInfo;
        this.mMapView = mMapView;
        this.destination = destination;
        
        try
		{
			JSONArray jsonArray = new JSONArray(jsonData);
			
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				double lon = jsonObject.getDouble("lon");
				double lat = jsonObject.getDouble("lat");
				carID.add(jsonObject.getString("carID"));
				
				GeoList.add(new OverlayItem(new GeoPoint((int)(lat*1e6),(int)(lon*1e6)), "p"+i, "pp"+i));
				Log.v("--------->", mContext.toString());
			}
			populate();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
 
        //用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
//        GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
//        GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
// 
//        GeoList.add(new OverlayItem(p1, "P1", "point1"));
//        GeoList.add(new OverlayItem(p2, "P2", "point2"));
//        populate();  //createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
    }
 
    @Override
    protected OverlayItem createItem(int i) {
        return GeoList.get(i);
    }
 
    @Override
    public int size() {
        return GeoList.size();
    }
 
    @Override
    // 处理当点击事件
    protected boolean onTap(int i) {
//        Toast.makeText(this.mContext, GeoList.get(i).getSnippet(),
//                Toast.LENGTH_SHORT).show();
    	//显示对话框
    	double lon1 = GeoList.get(i).getPoint().getLongitudeE6()/1e6;
    	double lat1 = GeoList.get(i).getPoint().getLatitudeE6()/1e6;
    	new TaxiInfo(mContext,lon1,lat1,strInfo,mMapView,this).dialog(carID.get(i),destination);
        return true;
    }
}

