package com.holly.desginpattern.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Receiver {
	public void action(String cmd) throws IOException {
		InputStream is = null;
		InputStreamReader isr = null;
		Process p =  Runtime.getRuntime().exec(cmd);
		is = p.getInputStream();
		isr = new InputStreamReader(is,"GBK");
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while((line = br.readLine())!=null){
			System.out.println(line);
		}
		System.out.println("执行!!!" + cmd+"完毕");
	}

}
