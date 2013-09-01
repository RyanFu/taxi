package com.qinyuan.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
//此类用于退出程序时使用，保存每一个Activity的实例，在退出时一个一个关掉
public class Mapplication extends Application
{
	private List<Activity> activityList = new LinkedList<Activity>();
	private static Mapplication instance;

	private Mapplication() {
	}

	// 单例模式中获取唯一的MyApplication实例
	public static Mapplication getInstance() {
	if (null == instance) {
	instance = new Mapplication();
	}
	return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
	activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
	for (Activity activity : activityList) {
	activity.finish();
	}
	System.exit(0);
	} 
}
