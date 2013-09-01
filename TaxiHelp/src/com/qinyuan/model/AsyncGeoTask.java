package com.qinyuan.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;
import com.qinyuan.activity.R;

public class AsyncGeoTask extends AsyncTask<GeoPoint, Void, String>
{
	private MapView mMapView;
	private Context activity;
	private String strInfo;
	private String destination;
	
	public AsyncGeoTask(MapView mMapView,Context activity,String strInfo,String destination){
		this.mMapView = mMapView;
		this.activity = activity;
		this.strInfo = strInfo;
		this.destination = destination;
	}
	
	protected String doInBackground(GeoPoint... params)
	{
		double lon = ((GeoPoint)params[0]).getLongitudeE6()/1e6;
		double lat = ((GeoPoint)params[0]).getLatitudeE6()/1e6;
		String backJson = "";
		
		HttpParams httpParameters = new BasicHttpParams();
    	HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
    	HttpConnectionParams.setSoTimeout(httpParameters, 10000);
    	HttpConnectionParams.setTcpNoDelay(httpParameters, true);
    	
    	HttpGet httpGet = new HttpGet("http://cloud.dlmu.edu.cn:9090/Taxi/GeoPointServlet?lon="+lon+"&lat="+lat);
    	HttpClient httpClient = new DefaultHttpClient();
    	InputStream is = null;
    	try{
    		HttpResponse httpResp = httpClient.execute(httpGet);
    		HttpEntity httpEntity = httpResp.getEntity();
    		is = httpEntity.getContent();
    		BufferedReader br = new BufferedReader(new InputStreamReader(is));
    		
    		
    		String line = "";
    		while(null != (line = br.readLine())){
    			backJson += line;
    		}
    		Log.v("---------->", backJson);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
		return backJson;
	}

	@Override
	protected void onPostExecute(String result)
	{
		Drawable marker = activity.getResources().getDrawable(R.drawable.taxi);
    	mMapView.getOverlays().add(new OverItemT(marker, activity,result,strInfo,mMapView,destination)); 
    	mMapView.invalidate();
    	Log.v(">>>>>>>>>>>>>", result);
    	Toast t = Toast.makeText(activity, "点击出租车图标查看对应的信息", Toast.LENGTH_LONG);
        t.show();
	}
}
