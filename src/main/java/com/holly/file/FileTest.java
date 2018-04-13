package com.holly.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileTest {
	public static final String fileDir = "C:/OutPut";
	public static final String outPutFile = "C:/OutPut/fileNames.txt";
	public static final int fileNamePreLen = 3;

	public static String getDiglefromStr(String str) {
		String str2 = "";
		if (str != null && !"".equals(str)) {
			for (int i = 0; i < fileNamePreLen; i++) {
				if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					str2 += str.charAt(i);
				}
			}
		}
		return str2;

	}

	public static void main(String[] args) throws IOException {
		File f = new File(outPutFile);
		if(f.exists()){
			f.delete();
		}
		List<File> files = Arrays.asList(new File(fileDir).listFiles());
		Collections.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				int f1PrefixNum = Integer.parseInt(getDiglefromStr(f1.getName()));
				int f2PrefixNum = Integer.parseInt(getDiglefromStr(f2.getName()));
				return f1PrefixNum - f2PrefixNum;
			}
		});
		
		FileWriter fw = new FileWriter(f);
		try {
			System.out.println("begin write file into "+outPutFile);
			for (File ff : files) {
				String fname = ff.getName();
				fw.write(fname);
				fw.write("\r\n");
			}
			fw.flush();
			System.out.println("write file into "+outPutFile+" done !!!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				fw.close();
			}
		}

	}

}
