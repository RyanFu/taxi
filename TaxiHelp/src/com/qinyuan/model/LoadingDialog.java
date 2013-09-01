package com.qinyuan.model;

import com.qinyuan.activity.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

//构造一个用户等待的对话框
public class LoadingDialog
{
	private Context activity;
	private AlertDialog.Builder builder;
	private AlertDialog ld;
	
	public LoadingDialog(Context activity){
		this.activity = activity;
	}
	
	
	public AlertDialog.Builder showDialog()
	{
		 builder = new Builder(activity); 
		 builder.setTitle("正在等待响应...").setIcon(R.drawable.load).setCancelable(false); 
		 ld = builder.create();
	     ld.show();
	     
	     return builder;
	}
	
	public void CloseDialog(){
		ld.dismiss();
	}
}
