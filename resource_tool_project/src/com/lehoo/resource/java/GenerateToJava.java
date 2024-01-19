package com.lehoo.resource.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GenerateToJava {
	
	/**
	 * 生成协议类
	 * @param projectPath
	 * @param packageName
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void generate(String projectPath,String packageName,String excelName,String sheetName,String [] notes,String[] names,String[] types,String [] indexs){
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		try {
			cfg.setDirectoryForTemplateLoading(new File(".\\config"));
        	cfg.setSharedVariable("upperFC", new FristToUpper());//添加一个"宏"共享变量用来将属性名首字母大写
			Template t = cfg.getTemplate("conf.ftl","UTF-8");//指定模板
			StringTokenizer st = new StringTokenizer(sheetName,"|");
			String className = st.nextToken();
			
			
			File jFile = new File(projectPath+"\\Conf"+className+".java");
			if(jFile.exists()){
				jFile.delete();
			}
			
			FileOutputStream fos = new FileOutputStream(jFile);//java文件的生成目录
			
			//模拟数据源
			Map data = new HashMap();
			data.put("package", packageName);//包名
			data.put("sheetName", className);//sheet名称，用来生成类名
			data.put("excelName", excelName);//sheet名称，用来生成类名
			
			if(st.hasMoreTokens()){
				data.put("superClass", st.nextToken());
			}else{
				data.put("superClass", "");
			}
			List fileds = new ArrayList();
			
			for(int i = 0;i<notes.length;i++){
				if(!types[i].equals("")){
					Map pro_1 = new HashMap();
					pro_1.put("type", types[i]);
					if(i==0){
						pro_1.put("name","sn");
					} else {
						pro_1.put("name",names[i]);
					}
					pro_1.put("note",notes[i]);
					fileds.add(pro_1);
				}
			}
			data.put("fileds", fileds);
			
			//{"",""}
			StringBuilder sb = new StringBuilder("{");
			for(int i=0;i<indexs.length;i++){
				sb.append("\"").append(indexs[i]).append("\"");
				if(i != indexs.length - 1){
					sb.append(",");
				}
			}
			sb.append("}");
			
			data.put("indexs", sb.toString());
			
			t.process(data, new OutputStreamWriter(fos,"utf-8"));//
			fos.flush();
			fos.close();
			
			System.out.println(">>Conf"+className+".java");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
	}
	

}