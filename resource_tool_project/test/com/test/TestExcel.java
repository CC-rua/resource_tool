package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.BasicBSONObject;

import com.lehoo.resource.excel.ExcelParser;
import com.lehoo.resource.util.ExcelUtils;
import com.mongodb.BasicDBObject;

public class TestExcel {

	public static void main(String[] args) throws Exception{
		File file = new File("E:/gamesrc/Server/resource/formation/formation.xlsx");
		File toFile = new File("C:\\Users\\Administrator\\Desktop\\ConFunctionDesc.json");
		
		Workbook wb = ExcelUtils.load(file);
		Sheet sheet = wb.getSheetAt(1);

		String[] names = ExcelParser.getNames(sheet);
		String[] index = ExcelParser.getIndexs(sheet);
		
		String[] types = ExcelParser.getTypes(sheet);

		Set<BasicBSONObject> parseData = ExcelParser.parseData(names, types, sheet);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toFile), "UTF-8"));
		
		for(BasicBSONObject obj : parseData){
			bw.write(obj.toString());
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
		
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(toFile),"UTF-8"));
		String line = null;
		while((line = reader.readLine()) != null){
			BasicDBObject jsonObj = BasicDBObject.parse(line);
			System.out.println(jsonObj.getString("sn")+"  "+jsonObj.getString("isFirstOpenAward"));;
			
		}
		reader.close();
		
		
		
		load(toFile);
		
	}
	
	
	public static void load(File file)throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				BasicDBObject jsonObj = BasicDBObject.parse(line);
				System.out.println(jsonObj.getString("sn")+" "+jsonObj.getBoolean("isFirstOpenAward"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			reader.close();
		}
	}


}
