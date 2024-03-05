package com.lehoo.resource.json;

import org.bson.BasicBSONObject;

import java.io.*;
import java.util.Set;
import java.util.StringTokenizer;

public class GenerateToJson {


    public static void generate(String jsonPath, String sheetName, Set<BasicBSONObject> datas) throws Exception {
        try {
            StringTokenizer st = new StringTokenizer(sheetName, "|");
            String className = st.nextToken();
            String jsonFileName = jsonPath + "/conf/Conf" + className + ".json";
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(jsonFileName)), "UTF-8"));
            for (BasicBSONObject jsonObj : datas) {
                bw.write(jsonObj.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
            System.out.println(">>Conf" + className + ".json");
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 文件末尾追加数据
     *
     * @param jsonPath
     * @param sheetName
     * @param datas
     * @throws Exception
     */
    public static void append(String jsonPath, String sheetName, Set<BasicBSONObject> datas) throws Exception {
        try {
            StringTokenizer st = new StringTokenizer(sheetName, "|");
            String className = st.nextToken();
            String jsonFileName = jsonPath + "/conf/Conf" + className + ".json";

            String lineSeperator = System.getProperty("line.separator");
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(jsonFileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            //写数据
            for (BasicBSONObject jsonObj : datas) {
                String obj = jsonObj.toString();
                randomFile.write((obj + lineSeperator).getBytes("UTF-8"));
            }
            randomFile.close();

            System.out.println(">>Conf" + className + ".json");
        } catch (Exception e) {
            throw e;
        }
    }


}
