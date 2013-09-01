package com.qinyuan.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText et1;
	private EditText et2;
	//private String result = "";
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
      //将此Activity添加到Mapplication中统一管理（退出程序时使用）
        Mapplication.getInstance().addActivity(this);

        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        
        Button butt = (Button)findViewById(R.id.but1);
        
        butt.setOnClickListener(new OnClickListener()
		{
        	
			public void onClick(View v)
			{	
				String user = et1.getText().toString();
		        String passwd = et2.getText().toString();
		        String url = "http://cloud.dlmu.edu.cn:9090/Taxi/LoginCheck?user="+user+"&passwd="+passwd;
		        
		        String result = "";
		        
				HttpGet httpGet = new HttpGet(url);
	        	HttpClient httpClient = new DefaultHttpClient();
	        	InputStream is = null;
	        	try{
	        		HttpResponse httpResp = httpClient.execute(httpGet);
	        		HttpEntity httpEntity = httpResp.getEntity();
	        		is = httpEntity.getContent();
	        		BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        		
	        		String line = "";
	        		while(null != (line = br.readLine())){
	        			result += line;
	        		}
	        		Log.v("---------->", result);
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        	
	        	if("success".equals(result)){
	        		Intent intent = new Intent();
					intent.setClass(LoginActivity.this,TaxiActivity.class);
					Bundle bundle = new Bundle();
					bundle.putStringArray("geoPoint", new String[]{"39107864","121826082"});
					intent.putExtras(bundle);
					startActivity(intent);
	        	} else {
	        		Toast t = Toast.makeText(LoginActivity.this, "您输入的账号或密码有误！", Toast.LENGTH_SHORT);
		            t.show();
	        	}
//	        	
			}
		});
   }
}