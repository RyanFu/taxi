package com.qinyuan.model;

import android.app.AlertDialog;
import android.os.AsyncTask;

public class AsyncLoadDialog extends AsyncTask<Void, Void, Void>
{
	private AlertDialog.Builder builder;
	
	public AsyncLoadDialog(AlertDialog.Builder builder){
		this.builder = builder;
	}
	
	@Override
	protected Void doInBackground(Void... params)
	{
	     for(int i = 20;i > 0;i--){
	    	 builder.setMessage("\t\t"+i+"秒");
	    	 try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
	     }
	     return null;
	}

	
}
