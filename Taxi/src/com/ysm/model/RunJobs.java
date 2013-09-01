package com.ysm.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunJobs {
	public RunJobs()
    {
    }

    public String toInvoke(String command[])
    {
        String result = "";
        try
        {
            Process p = Runtime.getRuntime().exec(command);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line ="";
            while(null != (line=br.readLine())){
            	result += line; 
            }
            p.waitFor();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
