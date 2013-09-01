package com.qinyuan.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapView;

//附加了 屏幕点击事件处理 的自定义MapView
public class MapViewClick extends MapView
{
	private DestinationOverlay desOverlay;

	private BMapManager mBMapMan = null;

	private EditText et;

	public static boolean flag = true;

	public MapViewClick(Context context)
	{
		super(context);
	}

	public MapViewClick(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public MapViewClick(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// 获得屏幕点击的位置
		int x = (int) event.getX();
		int y = (int) event.getY();
		// 将像素坐标转为地址坐标
		GeoPoint pt = this.getProjection().fromPixels(x, y);

		showDestination(pt);

		return super.onTouchEvent(event);
	}

	private void showDestination(GeoPoint pt1)
	{
		if (flag)
		{
			desOverlay = DestinationOverlay.getInstance(pt1, this.getContext(),
					R.drawable.zhong);
			this.getOverlays().add(desOverlay);

			et = UserActivity.getEditText();
			mBMapMan = UserActivity.getBMapManager();

			MKSearch mySearch = new MKSearch();
			mySearch.init(mBMapMan, new MKSearchListener()
			{
				public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,int arg1){}
				public void onGetTransitRouteResult(MKTransitRouteResult arg0,int arg1){}
				public void onGetSuggestionResult(MKSuggestionResult arg0,int arg1){}
				public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2){}
				public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,int arg1){}
				public void onGetBusDetailResult(MKBusLineResult arg0, int arg1){}
				public void onGetAddrResult(MKAddrInfo res, int error)
				{
					if (error != 0)
					{
						String str = String.format("错误号: %d", error);
						et.setText(str);
						return;
					}

					String street = "";
					if (null != res.addressComponents.street)
					{
						street = res.addressComponents.street;
					}

					String strInfo = res.addressComponents.city
							+ res.addressComponents.district + street;

					Log.v("-------->", strInfo + "=========");
					if (null != et)
					{
						et.setText(strInfo);
						Log.v("-------->", et + "");
					}
				}
			});
			mySearch.reverseGeocode(pt1);
		}
	}

}
