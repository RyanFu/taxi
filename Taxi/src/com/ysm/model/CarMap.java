package com.ysm.model;

import java.util.HashMap;
import java.util.Map;

public class CarMap {
	private static CarMap carMap = new CarMap();
	//��������߼Ƴ̳�����Ϣ
	private Map<String,CarInfo> map = new HashMap<String, CarInfo>();
	
	public Map<String, CarInfo> getMap() {
		return map;
	}

	public void setMap(Map<String, CarInfo> map) {
		this.map = map;
	}

	public static CarMap getCarMap(){
		return carMap;
	}
	
}
