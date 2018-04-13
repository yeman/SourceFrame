package com.holly.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OutPutFileName {
	public static final String fileDir = "C:\\JMS&ActiveMQ实战(JMS+ActiveMQ+Drools)";
	public static final String outPutFile = "C:\\JMS&ActiveMQ实战(JMS+ActiveMQ+Drools)\\fileNames.txt";
	public static final int fileNamePreLen = 3;

	public static void main(String[] args) throws IOException {
		File f = new File(outPutFile);
		if (f.exists()) {
			f.delete();
		}
		List<File> files = Arrays.asList(new File(fileDir).listFiles());

		FileWriter fw = new FileWriter(f);
		try {
			System.out.println("begin write file into " + outPutFile);
			for (File ff : files) {
				String fname = ff.getName();
				fw.write(fname);
				fw.write("\r\n");
			}
			fw.flush();
			System.out.println("write file into " + outPutFile + " done !!!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				fw.close();
			}
		}

	}

}
