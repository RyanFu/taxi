package com.qinyuan.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.Projection;

public class MyPositionOverlay extends Overlay
{
	private static GeoPoint geoPoint;
	private static Context context;
	private static int drawable;
	private static MyPositionOverlay myOverlay = new MyPositionOverlay();
	
	public MyPositionOverlay(){super();}
//	public MyPositionOverlay(GeoPoint geoPoint, Context context, int drawable)
//	{
//		super();
//		this.geoPoint = geoPoint;
//		this.context = context;
//		this.drawable = drawable;
//
//	}
	
	public static MyPositionOverlay getInstance(GeoPoint pt, Context c, int d){
		geoPoint = pt;
		context = c;
		drawable = d;
		return myOverlay;
	}
	
	
	
	private Projection projection;
	private Point point;
	private Bitmap bitmap;

	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{

		projection = mapView.getProjection();
		point = new Point();
		projection.toPixels(geoPoint, point);

		bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
		// canvas.drawBitmap(bitmap, point.x - bitmap.getWidth() , point.y
		// -bitmap.getHeight() , null);
		canvas.drawBitmap(bitmap, point.x, point.y, null);
		super.draw(canvas, mapView, shadow);
	}

	
}
