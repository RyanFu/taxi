package com.qinyuan.action;

import com.qinyuan.model.MessageNotify;
import android.os.AsyncTask;


public class CallTaxi extends AsyncTask<MessageNotify, Void, String>{
	
	@Override
	protected String doInBackground(MessageNotify... mn)
	{
		try
		{
			Thread.sleep(20000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		mn[0].dialogDisappear();
		return null;
	}

	@Override
	protected void onPostExecute(String result)
	{
		
	}
}
