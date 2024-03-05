package com.lehoo.resource;

import com.lehoo.resource.excel.ExcelParser;
import com.lehoo.resource.java.GenerateToJava;
import com.lehoo.resource.json.GenerateToJson;
import com.lehoo.resource.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.BasicBSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * 把excel转化成json，存入文件
 * 并且生成对应的java类
 *
 * @author chuer
 * @date 2016年6月23日 下午4:28:38
 */
public class ExcelToJsonAndJava {

    static Set<String> sheetNames = new HashSet<>();

    static Set<String> generatedSheets = new HashSet<>();

    /**
     * 读取所有的excel文件
     *
     * @param file
     * @return
     */
    private static void excelFiles(File file, Set<File> excelFiles) {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File f : listFiles) {
                excelFiles(f, excelFiles);
            }
        } else {
            if (file.getName().endsWith(".xlsx") && !file.getName().startsWith("~$")) {
                excelFiles.add(file);
            }
        }
    }

    /**
     * 解析excel文件
     */
    private static void generate(String resourcePath, String projectPath, File excelFile, String packageName) {
        try {
            FileInputStream fis = new FileInputStream(excelFile);
            Workbook workBook = ExcelUtils.load(fis);
            int numberOfSheets = workBook.getNumberOfSheets();

            for (int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex++) {
                Sheet sheet = workBook.getSheetAt(sheetIndex);

                //判断sheet的名字是否正确定义
                String sheetName = sheet.getSheetName();
                if (sheetName.toLowerCase().startsWith("sheet") || sheetName.contains("策划") || sheetName.contains("coder")) {
//					System.out.println("warn:excel:"+excelFile.getName()+" sheetName error. name:"+sheet.getSheetName());
                    continue;
                }

                //注释
                String[] notes = ExcelParser.getNotess(sheet);
                //列名
                String[] names = ExcelParser.getNames(sheet);
                //索引
                String[] indexs = ExcelParser.getIndexs(sheet);

                //数据主键名字必须是sn
                if (!names[0].equals("sn")) {
                    continue;
                }

                //列类型
                String[] types = ExcelParser.getTypes(sheet);
                if (names.length != types.length) {
                    throw new Exception("excel error:" + excelFile.getName());
                }

                if (sheetNames.contains(sheetName)) {
                    throw new Exception("same sheet name :" + sheetName);
                }


                sheetNames.add(sheetName);

                //解析内容
                Set<BasicBSONObject> datas = ExcelParser.parseData(names, types, sheet);

                String suffix = "";
                if (sheetName.indexOf("|") != -1) {
                    suffix = sheetName.substring(sheetName.indexOf("|"), sheetName.length());
                }

                //是否生成java和json文件，java文件不在重新生成，json数据需要继续往下写。
                boolean isGenerate = false;
                if (sheetName.contains("-")) {
                    sheetName = sheetName.substring(0, sheetName.indexOf("-"));
                    if (generatedSheets.contains(sheetName)) {
                        isGenerate = true;
                    } else {
                        generatedSheets.add(sheetName);
                    }

                    sheetName += suffix;
                }

                if (isGenerate) {
                    appendToJson(resourcePath, sheetName, datas);
                } else {
                    //生成json文件
                    appendToJson(resourcePath, sheetName, datas);

                    //生成java类
                    generateToJava(projectPath, packageName, excelFile.getName(), sheetName, notes, names, types, indexs);
                }
            }

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateOneSheet(String jsonPath, String projectPath, File excelFile, String packageName) {
        try {
            FileInputStream fis = new FileInputStream(excelFile);
            Workbook workBook = ExcelUtils.load(fis);
            int numberOfSheets = workBook.getNumberOfSheets();

            Sheet sheet = workBook.getSheetAt(0);

            //判断sheet的名字是否正确定义
            String sheetName = sheet.getSheetName();
            if (sheetName.toLowerCase().startsWith("sheet") || sheetName.contains("策划") || sheetName.contains("coder")) {
                throw new Exception("warn:excel:" + excelFile.getName() + " sheetName error. name:" + sheet.getSheetName());
            }
            System.out.println("##>" + sheetName);
            //注释
            String[] notes = ExcelParser.getNotess(sheet);
            //列名
            String[] names = ExcelParser.getNames(sheet);
            //索引
            String[] indexs = ExcelParser.getIndexs(sheet);

            //数据主键名字必须是sn
/*			if(!names[0].equals("sn")){
				return;
			}*/

            //列类型
            String[] types = ExcelParser.getTypes(sheet);
            if (types == null || types.length == 0 || types[0].isEmpty()) {
                return;
            }

            if (names.length != types.length) {
                throw new Exception("excel error:" + excelFile.getName());
            }


            if (sheetNames.contains(sheetName)) {
                throw new Exception("same sheet name :" + sheetName);
            }


            sheetNames.add(sheetName);

            //解析内容
            Set<BasicBSONObject> datas = ExcelParser.parseDataOneSheet(names, types, sheet);

            String suffix = "";
            if (sheetName.indexOf("|") != -1) {
                suffix = sheetName.substring(sheetName.indexOf("|"), sheetName.length());
            }

            //是否生成java和json文件，java文件不在重新生成，json数据需要继续往下写。
            boolean isGenerate = false;
            if (sheetName.contains("-")) {
                sheetName = sheetName.substring(0, sheetName.indexOf("-"));
                if (generatedSheets.contains(sheetName)) {
                    isGenerate = true;
                } else {
                    generatedSheets.add(sheetName);
                }

                sheetName += suffix;
            }

            if (isGenerate) {
                appendToJson(jsonPath, sheetName, datas);
            } else {
                //生成json文件
                appendToJson(jsonPath, sheetName, datas);

                //生成java类
                generateToJava(projectPath, packageName, excelFile.getName(), sheetName, notes, names, types, indexs);
            }


            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 追加json数据
     *
     * @param jsonPath
     * @param sheetName
     * @param datas
     * @throws Exception
     */
    private static void appendToJson(String jsonPath, String sheetName, Set<BasicBSONObject> datas) throws Exception {
        GenerateToJson.append(jsonPath, sheetName, datas);
    }

    /**
     * 生成java类
     *
     * @param names
     * @param types
     */
    private static void generateToJava(String projectPath, String packageName, String excelName, String sheetName, String[] notes, String[] names, String[] types, String[] indexs) {
        GenerateToJava.generate(projectPath, packageName, excelName, sheetName, notes, names, types, indexs);
    }


    /**
     * 查找目录下所有的excel文件，并把excel文件生成json文件和java文件
     *
     * @param excelPath   ../server_excel/
     * @param projectPath ../mmorpg/
     * @param packageName
     */
    public static void loopAndGenerate(String excelPath, String projectPath, String packageName, String jsonPath) {
        File excelFile = new File(excelPath);

        //获得所有的excel文件
        Set<File> excelFiles = new HashSet<>();
        excelFiles(excelFile, excelFiles);


        //生成json文件 和java文件
        for (File excel : excelFiles) {
            System.out.println("@@>" + excel.getName());
            generateOneSheet(jsonPath, projectPath, excel, packageName);
        }
    }


    public static void main(String[] args) {
        if (args.length >= 3) {
            String excelPath = args[0];
            String javaPath = args[1];
            String packageName = args[2];
            String jsonPath = args[3];
            if (args.length == 6) {
                ExcelParser.TYPE_ROW_NUM = Integer.parseInt(args[4]);
                ExcelParser.HEAD_ROW_COUNT = Integer.parseInt(args[5]);
            }
            loopAndGenerate(excelPath, javaPath, packageName, jsonPath);
            System.out.println(".................................................");
            System.out.println("...............genarate success..................");
            System.out.println(".................................................");
        } else {
            throw new RuntimeException("args error:");
        }
    }

}
