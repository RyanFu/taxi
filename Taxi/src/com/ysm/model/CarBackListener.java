package com.ysm.model;

import java.util.HashMap;
import java.util.Map;

public class CarBackListener {
	private static CarBackListener cbl = new CarBackListener();
	//����û�����򳵺�˾���Ļ�ִ��Ϣ
	private Map<String,String> map = new HashMap<String, String>();
	
	public Map<String,String> getMap(){
		return map;
	}
	
	public static CarBackListener getCarBackListener(){
		return cbl;
	}
}
