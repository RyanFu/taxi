package com.qinyuan.model;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;
import com.qinyuan.activity.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ConfirmDialog
{
	private Context activity;
	private String carBackInfo;
	private MapView mMapView;
	private OverItemT overItems;
	private GeoPoint pt;
	
	public ConfirmDialog(Context activity,String carBackInfo,MapView mMapView,OverItemT overItems,GeoPoint pt){
		this.activity = activity;
		this.carBackInfo = carBackInfo;
		this.mMapView = mMapView;
		this.overItems = overItems;
		this.pt = pt;
		
	}
	
	protected void showDialog() {
	 	  AlertDialog.Builder builder = new Builder(activity);
	 	  if("yes".equals(carBackInfo)){
	 		  builder.setTitle("恭喜");
	 		  builder.setMessage("恭喜，您刚才点击的出租车同意了您的请求，马上过来接您，请稍等。");
	 	  }else if("no".equals(carBackInfo)){
	 		  builder.setTitle("对不起");
	 		  builder.setMessage("对不起，您刚才点击的出租车拒绝了您的请求，您可以换一辆车试试。");
	 	  }else{
	 		 builder.setTitle("抱歉");
	 		 builder.setMessage("抱歉，您刚才点击的出租车暂时没有响应，您可以重新点击或换一辆车试试。");
	 	  }
	 	  
	 	  builder.setPositiveButton("确认", new OnClickListener() {
	 	   @Override
	 	   public void onClick(DialogInterface dialog, int which) {
	 	    dialog.dismiss();
	 	    if("yes".equals(carBackInfo)){
	 	    	mMapView.getOverlays().remove(overItems);
	 	    	
	 	    	mMapView.getOverlays().add(TaxiPoiOverlay.getInstance(pt, activity, R.drawable.taxi));
	 	    	mMapView.invalidate();
	 	    }
	 	   }
	 	  });
	 	 
	 	  builder.create().show();
	 	 }
}
