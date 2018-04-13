package com.holly.hobby;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

//处理日志数据
public class HandleText {
	
	public static void main(String[] args){
		BufferedReader br = null;
		FileReader r = null;
		try {
			Set<String> s = new HashSet();
		    r = new FileReader(new File("E:\\项目工作文档\\总部工单\\icdssoc_obj.sql"));
			br= new BufferedReader(r);
			while(br.readLine()!=null){
				String txt = br.readLine();
				System.out.println(txt);
				if(txt!=null){
					int start = txt.indexOf("comment on column  ");
					if(start>0){
						String tablespacename = txt.substring(start+"comment on column ".length()+1, txt.length());
						s.add(tablespacename);
					}
				}
			}
			
			System.out.println(s.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(br!=null){try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}}
			if(r!=null){try {
				r.close();
			} catch (IOException e) {
				e.printStackTrace();
			}}
			
		}
		
	}
}
