package com.qinyuan.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncWaitTask extends AsyncTask<String, Void, String>
{
	private Context activity;
	private LoadingDialog ld;
	private GeoPoint pt;
	private MapView mMapView;
	private int times = 0;   //控制请求的次数
	private OverItemT overItems;

	public AsyncWaitTask(Context activity,LoadingDialog ld,GeoPoint pt,MapView mMapView,OverItemT overItems)
	{
		this.activity = activity;
		this.ld = ld;
		this.pt = pt;
		this.mMapView = mMapView;
		this.overItems = overItems;
	}

	@Override
	protected String doInBackground(String... params)
	{
		//上传打车请求
		HttpGet httpGet = new HttpGet(params[0]);
		HttpClient httpClient = new DefaultHttpClient();
		try
		{
			httpClient.execute(httpGet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		//每隔两秒请求一次，等待司机的响应
		while (true)
		{
			HttpGet httpGet1 = new HttpGet(params[1]);
			HttpClient httpClient1 = new DefaultHttpClient();
			InputStream is = null;
			String backMess = "";
			try
			{
				HttpResponse httpResp = httpClient1.execute(httpGet1);
				HttpEntity httpEntity = httpResp.getEntity();
				is = httpEntity.getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));

				String line = "";
				while (null != (line = br.readLine()))
				{
					backMess += line;
				}
				Log.v("---------->", backMess);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			if ("wait".equals(backMess))
			{
				if(times > 9){
					return "nothing";
				}
				try
				{
					Thread.sleep(2000);
					times++;
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				Log.v("+++++++++++++", "++++++++++");
			}
			else if ("yes".equals(backMess))
			{
				return "yes";
			}
			else
			{
				return "no";
			}
		}
	}

	@Override
	protected void onPostExecute(String backMess)
	{
		//显示司机响应的对话框
		new ConfirmDialog(activity, backMess,mMapView,overItems,pt).showDialog();
		//关掉用户等待的对话框
		ld.CloseDialog();
	}
}
