package com.ysm.model;

import java.io.File;

public class FileHandlerThread extends Thread {
	String path;
	File file = null;
	
	public FileHandlerThread(String path,File file){
		this.path = path;
		this.file = file;
	}
	
	public FileHandlerThread(){}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(120000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(file.exists()){
				file.delete();
			}
		}
	}
}
