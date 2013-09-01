package com.qinyuan.model;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.qinyuan.activity.Mapplication;

public class ExitNotify
{
private Context activity;
	
	public ExitNotify(Context activity){
		this.activity = activity;
	}
	
	public void dialog() {
	 	  AlertDialog.Builder builder = new Builder(activity);
	 	  
	 	  builder.setTitle("您确实要退出吗？");
	 	  builder.setPositiveButton("确定", new OnClickListener() {
	 	   @Override
	 	   public void onClick(DialogInterface dialog, int which) {
	 		  Mapplication.getInstance().exit();
	 	   }
	 	  });
	 	  builder.setNegativeButton("取消", new OnClickListener() {
	 	   @Override
	 	   public void onClick(DialogInterface dialog, int which) {
	 	    dialog.dismiss();
	 	   }
	 	  });
	 	  builder.create().show();
	 	 }
}
