package com.lehoo.resource.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @Description: excel 工具类  poi 3.10.1
 * @date 2016年7月1日 下午1:19:40
 * @author chuer
 */
public class ExcelUtils {

	
	/**
	 * 创建工作薄
	 * @param file
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Workbook load(File file) throws InvalidFormatException, IOException{
		return WorkbookFactory.create(file);
	}
	/**
	 * 创建工作薄
	 * @param file
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Workbook load(InputStream ins) throws InvalidFormatException, IOException{
		return WorkbookFactory.create(ins);
	}
	
	
	/**
	 * 根据sheet名称获取sheet
	 * @param wb
	 * @param sheetName
	 * @return
	 */
	public static Sheet getSheet(Workbook wb,String sheetName){
		return wb.getSheet(sheetName);
	}
	
	
	/**
	 * 根据sheet下表获取sheet
	 * @param wb
	 * @param sheetIndex
	 * @return
	 */
	public static Sheet getSheet(Workbook wb,int sheetIndex){
		return wb.getSheetAt(sheetIndex);
	}
	
	
	/**
	 * 获取sheet所有的行
	 * @param sheet
	 * @return
	 */
	public static Row [] getRows(Workbook wb){
		Sheet sheet = getSheet(wb,0);
		return getRows(sheet);
	}
	
	
	
	/**
	 * 获取sheet所有的行
	 * @param sheet
	 * @return
	 */
	public static Row [] getRows(Sheet sheet){
		Row [] rows = new Row[sheet.getLastRowNum()];
		int index = 0;
		for(Row row : sheet){
			rows[index] = row;
			index++;
		}
		return rows;
	}
	
	/**
	 * 获取sheet的行,排除了head行
	 * @param sheet
	 * @param headNum  
	 * @return
	 */
	public static Row [] getRows(Sheet sheet,int headNum){
		Row [] rows = new Row[sheet.getPhysicalNumberOfRows() - headNum];
		int index = 0;
		for(Row row : sheet){
			if(index < headNum){
				index++;
				continue;
			}
			rows[index - headNum] = row;
			index++;
		}
		return rows;
	}
	
	/**
	 * 获得某行的列数
	 * @param row
	 * @return
	 */
	public static int getCols(Row row){
		return row.getPhysicalNumberOfCells();
	}
	
	
	/**
	 * 获取sheet的行,排除了head行
	 * @param wb
	 * @param headNum  
	 * @return
	 */
	public static Row [] getRows(Workbook wb,int offset){
		Sheet sheet = getSheet(wb,0);
		Row [] rows = new Row[sheet.getPhysicalNumberOfRows() - offset];
		int index = 0;
		for(Row row : sheet){
			if(index++ < offset){
				continue;
			}
			rows[index - offset - 1] = row;
		}
		return rows;
	}
	
	/**
	 * 获得行
	 * @param wb
	 * @param headIndex
	 * @return
	 */
	public static Row getHead(Workbook wb,int headIndex){
		Sheet sheet = getSheet(wb,0);
		return sheet.getRow(headIndex);
	}
	
	/**
	 * 获得行
	 * @param wb
	 * @param headIndex
	 * @return
	 */
	public static Row getHead(Sheet sheet,int headIndex){
		return sheet.getRow(headIndex);
	}
	
	/**
	 * 获得行
	 * @param sheet
	 * @param index
	 * @return
	 */
	public static Row getRow(Sheet sheet,int index){
		return sheet.getRow(index);
	}
	
	
	/**
	 * 获得单元格float类型的值
	 * @param row
	 * @param index
	 * @return
	 */
	public static Float getFloValue(Row row,int index){
		Cell cell = row.getCell(index);
		return getFloValue(cell);
	}
	
	/**
	 * 获得单元格float类型的值
	 * @param cell
	 * @return
	 */
	public static Float getFloValue(Cell cell){
		return Float.parseFloat(getStrValue(cell).equals("")?"0":getStrValue(cell));
	}
	
	
	/**
	 * 获得double类型值
	 * @param row
	 * @param index
	 * @return
	 */
	public static Double getDouValue(Row row,int index){
		Cell cell = row.getCell(index);
		return getDouValue(cell);
	}
	
	/**
	 * 获得double类型值
	 * @param cell
	 * @return
	 */
	public static Double getDouValue(Cell cell){
		return Double.parseDouble(getStrValue(cell).equals("")?"0":getStrValue(cell));
	}
	/**
	 * 获得单元格int类型的值
	 * @param cell
	 * @return
	 */
	public static Integer getIntValue(Cell cell){
		return getDouValue(cell).intValue();
	}
	
	/**
	 * 获得单元格int类型的值
	 * @param row
	 * @param index
	 * @return
	 */
	public static Integer getIntValue(Row row,int index){
		Cell cell = row.getCell(index);
		return getIntValue(cell);
	}
	
	
	/**
	 * 获得单元格int类型的值
	 * @param row
	 * @param index
	 * @return
	 */
	public static Short getShortValue(Row row,int index){
		return getIntValue(row,index).shortValue();
	}
	
	
	
	
	/**
	 * 获得单元格byte类型的值
	 * @param row
	 * @param index
	 * @return
	 */
	public static Byte getByteValue(Row row,int index){
		return getIntValue(row,index).byteValue();
	}
	
	/**
	 * 获得单元格long类型的值
	 * @param row
	 * @param index
	 * @return
	 */
	public static Long getLongValue(Row row,int index){
		return getDouValue(row, index).longValue();
//		return Double.valueOf(getStrValue(row,index)).longValue();
	}
	
	/**
	 * 获得Boolean类型值
	 * @param row
	 * @param index
	 * @return
	 */
	public static Boolean getBoolValue(Row row,int index){
		Cell cell = row.getCell(index);
		return getBoolValue(cell);
	}
	
	/**
	 * 获得Boolean类型值
	 * @param cell
	 * @return
	 */
	public static Boolean getBoolValue(Cell cell){
		return Boolean.valueOf(getStrValue(cell));
	}
	
	
	/**
	 * 获得单元格字符串格式内容
	 * @param row
	 * @param index
	 * @return
	 */
	public static String getStrValue(Row row,int index){
		return getStrValue(row.getCell(index));
	}
	
	
	/**
	 * 获得单元格字符串格式内容
	 * @param cell
	 * @return
	 */
	public static String getStrValue(Cell cell){
		if(cell == null){
			return "";
		}
		String cellValue = null;
		switch(cell.getCellType()){
		case Cell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				cellValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
			}else{
				cellValue = String.valueOf(cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue()).toLowerCase();
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellValue = String.valueOf(cell.getCellFormula());
			break;
		case Cell.CELL_TYPE_BLANK:
			cellValue = "";
			break;
		default:
			cellValue = "";
		}
		return cellValue.trim();
	}
	
	/** 是否是科学计数方式 */
	public static String convertScientific(String string) {
		String regx = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$"; // 科学计数法正则表达式
		Pattern pattern = Pattern.compile(regx);
		if (pattern.matcher(string).matches()) {
			return new BigDecimal(string).toPlainString();
		}
		return string;
	}
	
}
