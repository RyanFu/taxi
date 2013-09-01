package com.qinyuan.model;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
//构造一个对话框，查看出租车信息，确认打车
public class TaxiInfo
{
	private Context activity;
	private double lon;
	private double lat;
	private String userAddr;
	private GeoPoint pt;
	private MapView mMapView;
	private OverItemT overItems;

	TaxiInfo(Context activity, double lon, double lat, String userAddr,MapView mMapView,OverItemT overItems)
	{
		this.activity = activity;
		this.lon = lon;
		this.lat = lat;
		this.userAddr = userAddr;
		this.overItems = overItems;
		this.mMapView = mMapView;
		pt = new GeoPoint((int)(lat*1e6), (int)(lon*1e6));
	}

	protected void dialog(String carID,final String destination)
	{
		AlertDialog.Builder builder = new Builder(activity);
//		builder.setMessage("车  ID："
//				+ carID
//				+ "\n车牌号:辽B 119110\n司  机:xxx\n车  型:北京现代\n司机电话："+num+"\n车身颜色：灰黄\n所属企业：大金");
		builder.setTitle("确认要坐这辆车？");
		
		TextView tv = new TextView(activity);
		tv.setSingleLine(false);
		tv.setText(Html.fromHtml("车  ID："+ carID + "<br>车牌号:辽B 119110<br>司  机:xxx<br>车  型:北京现代<br>司机电话：<a href='tel:18801234567'>18801234567</a><br>车身颜色：灰黄<br>所属企业：大金"));
		tv.setTextSize(18);
		
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		builder.setView(tv);
		
		builder.setPositiveButton("确认", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				String url1 = "http://cloud.dlmu.edu.cn:9090/Taxi/UserHandler?lon="
						+ lon
						+ "&lat="
						+ lat
						+ "&userAddr="
						+ userAddr
						+ "&carID=116622" + "&userID=119" +"&destination=" + destination;
				String url2 = "http://cloud.dlmu.edu.cn:9090/Taxi/UserResp?"
						+ "userID=119";
				//关闭此对话框
				dialog.dismiss();
				
				//打开显示 等待响应... 的对话框
				LoadingDialog ld = new LoadingDialog(activity);
				
				new AsyncLoadDialog(ld.showDialog()).execute();
				
				//异步上传打车请求，并等待司机响应
				AsyncWaitTask awt = new AsyncWaitTask(activity,ld,pt,mMapView,overItems);
				awt.execute(url1, url2);

			}

		});
		builder.setNegativeButton("取消", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
}
