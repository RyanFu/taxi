package com.ysm.model;

import java.util.HashMap;
import java.util.Map;

public class UserMap {
	private static UserMap userMap = new UserMap();
	//����û��ԼƳ̳���������Ϣ
	public Map<String,FromUserInfo> map = new HashMap<String,FromUserInfo>();
	
	public Map<String, FromUserInfo> getMap() {
		return map;
	}

	public void setMap(Map<String, FromUserInfo> map) {
		this.map = map;
	}

	public static UserMap getUserMap(){
		return userMap;
	}
}
