package com.ysm.model;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class CarInfoWriter {
	private static CarInfoWriter carInfoWriter = new CarInfoWriter();
	private RandomAccessFile raf;
	
	public RandomAccessFile getRaf() throws FileNotFoundException {
		if(raf == null){
			return raf = new RandomAccessFile("J:/as.txt","rw");
		}else{
			return raf;
		}
	}

	public static CarInfoWriter getInstance(){
		return carInfoWriter;
	}
}
