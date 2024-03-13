package com.lehoo.resource;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.BasicBSONObject;

import com.lehoo.resource.excel.ExcelParser;
import com.lehoo.resource.json.GenerateToJson;
import com.lehoo.resource.util.ExcelUtils;

import static com.lehoo.resource.excel.ExcelParser.NAME_ROW_NUM;

/**
 * 把excel转化成json，存入文件
 * 并且生成对应的java类
 * @author chuer
 * @date 2016年6月23日 下午4:28:38
 */
public class ExcelToJson {

	static Map<String,String> sheetNames = new HashMap<>();

	static Set<String> generatedSheets = new HashSet<>();

	/**
	 * 读取所有的excel文件
	 * @param file
	 * @return
	 */
	private static void excelFiles(File file,Set<File> excelFiles){
		if(file.isDirectory()){
			File[] listFiles = file.listFiles();
			for(File f : listFiles){
				excelFiles(f,excelFiles);
			}
		}else{
			if(file.getName().endsWith(".xlsx") && !file.getName().startsWith("~$")){
				excelFiles.add(file);
			}
		}
	}

	/**
	 * 解析excel文件
	 */
	private static void generate(String resourcePath,File excelFile){
		try {
			FileInputStream fis = new FileInputStream(excelFile);
			Workbook workBook = ExcelUtils.load(fis);
			int numberOfSheets = workBook.getNumberOfSheets();

			for(int sheetIndex = 0 ; sheetIndex < numberOfSheets; sheetIndex++){
				Sheet sheet = workBook.getSheetAt(sheetIndex);

				//判断sheet的名字是否正确定义
				String sheetName = sheet.getSheetName();
				if(sheetName.toLowerCase().startsWith("sheet") || sheetName.contains("策划") || sheetName.contains("coder")){
					System.out.println("warn:excel:"+excelFile.getName()+" sheetName error. name:"+sheet.getSheetName());
					continue;
				}
				System.out.println("##>"+sheetName);
				Row rowName = ExcelUtils.getHead(sheet, NAME_ROW_NUM);
				int cols = rowName.getPhysicalNumberOfCells();
				//列名
				String[] names = ExcelParser.getNames(sheet, cols);
				//数据主键名字必须是sn
				if(!names[0].equals("sn")){
					System.out.println("warn:excel:"+excelFile.getName()+" sn is not primary");
					continue;
				}

				//列类型
				String[] types = ExcelParser.getTypes(sheet, cols);
				if(names.length != types.length){
					throw new Exception("excel error:"+excelFile.getName());
				}

				if(sheetNames.containsKey(sheetName)){
					throw new Exception("same sheet name :"+sheetName+", newFile:"+excelFile.getAbsolutePath()+", oldFile:"+sheetNames.get(sheetName));
				}

				sheetNames.put(sheetName,excelFile.getAbsolutePath());

				//解析内容
				Set<BasicBSONObject> datas = ExcelParser.parseData(names,types,sheet);

				String suffix = "";
				if(sheetName.indexOf("|") != -1){
					suffix = sheetName.substring(sheetName.indexOf("|"), sheetName.length());
				}

				//是否生成java和json文件，java文件不在重新生成，json数据需要继续往下写。
				boolean isGenerate = false;
				if(sheetName.contains("-")){
					sheetName = sheetName.substring(0, sheetName.indexOf("-"));
					if(generatedSheets.contains(sheetName)){
						isGenerate = true;
					}else{
						generatedSheets.add(sheetName);
					}

					sheetName += suffix;
				}

				if(isGenerate){
					appendToJson(resourcePath,sheetName,datas);
				}else{
					//生成json文件
					appendToJson(resourcePath,sheetName,datas);
				}
			}

			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void generateOneSheet(String resourcePath,File excelFile){
		try {
			FileInputStream fis = new FileInputStream(excelFile);
			Workbook workBook = ExcelUtils.load(fis);
			int numberOfSheets = workBook.getNumberOfSheets();

			Sheet sheet = workBook.getSheetAt(0);

			//判断sheet的名字是否正确定义
			String sheetName = sheet.getSheetName();
			if(sheetName.toLowerCase().startsWith("sheet") || sheetName.contains("策划") || sheetName.contains("coder")){
				throw new Exception("warn:excel:"+excelFile.getName()+" sheetName error. name:"+sheet.getSheetName());
			}
			Row rowName = ExcelUtils.getHead(sheet, NAME_ROW_NUM);
			int cols = rowName.getPhysicalNumberOfCells();
			//列名
			String[] names = ExcelParser.getNames(sheet, cols);
			//数据主键名字必须是sn
/*			if(!names[0].equals("sn")){
				throw new Exception("warn:excel:"+excelFile.getName()+" sn is not primary");
			}*/

			//列类型
			String[] types = ExcelParser.getTypes(sheet, cols);
			if(names.length != types.length){
				throw new Exception("excel error:"+excelFile.getName());
			}

			if(sheetNames.containsKey(sheetName)){
				throw new Exception("same sheet name :"+sheetName+", newFile:"+excelFile.getAbsolutePath()+", oldFile:"+sheetNames.get(sheetName));
			}

			sheetNames.put(sheetName,excelFile.getAbsolutePath());

			//解析内容
			Set<BasicBSONObject> datas = ExcelParser.parseDataOneSheet(names,types,sheet);

			String suffix = "";
			if(sheetName.indexOf("|") != -1){
				suffix = sheetName.substring(sheetName.indexOf("|"), sheetName.length());
			}

			//是否生成java和json文件，java文件不在重新生成，json数据需要继续往下写。
			boolean isGenerate = false;
			if(sheetName.contains("-")){
				sheetName = sheetName.substring(0, sheetName.indexOf("-"));
				if(generatedSheets.contains(sheetName)){
					isGenerate = true;
				}else{
					generatedSheets.add(sheetName);
				}

				sheetName += suffix;
			}

			if(isGenerate){
				appendToJson(resourcePath,sheetName,datas);
			}else{
				//生成json文件
				appendToJson(resourcePath,sheetName,datas);
			}

			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 追加json数据
	 * @param resourcePath
	 * @param sheetName
	 * @param datas
	 * @throws Exception
	 */
	private static void appendToJson(String resourcePath,String sheetName,Set<BasicBSONObject> datas) throws Exception{
		GenerateToJson.append(resourcePath, sheetName, datas);
	}

	/**
	 * 查找目录下所有的excel文件，并把excel文件生成json文件和java文件
	 * @param resourcePath  ../resource/
	 *
	 */
	public static void loopAndGenerate(String resourcePath){
		File resouceFile = new File(resourcePath);

		//获得所有的excel文件
		Set<File> excelFiles = new HashSet<>();
		excelFiles(resouceFile,excelFiles);


		//生成json文件 和java文件
		for(File excelFile : excelFiles){
			generateOneSheet(resourcePath,excelFile);
		}
	}


	public static void main(String[] args) {
		if(args.length == 1){
			String resourcePath = args[0];
			loopAndGenerate(resourcePath);
			System.out.println(".................................................");
			System.out.println("...............genarate success..................");
			System.out.println(".................................................");
		}else{
			throw new RuntimeException("args error:");
		}
	}

}
