package com.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

public class TestAppend {

	public static void main(String[] args)throws Exception {
//		bbb();
//		bbb();
//		bbb();
//		bbb();
		
		
		String sheetName = "Stuff-a|Action";
		String suffix = "";
		if(sheetName.indexOf("|") != -1){
			suffix = sheetName.substring(sheetName.indexOf("|"), sheetName.length());
		}
		sheetName = sheetName.substring(0, sheetName.indexOf("-"));
		sheetName += suffix;
				
		System.out.println(sheetName);
	}
	
	
	private static void bbb()throws Exception{
		 try {
			 String lineSeperator = System.getProperty("line.separator");
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile("C:\\Users\\Administrator\\Desktop\\aa.txt", "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            
            randomFile.write(new String("你好你好"+lineSeperator).getBytes());//(new String("aaaaa".getBytes(),"UTF-8"));
            
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	
	

}
