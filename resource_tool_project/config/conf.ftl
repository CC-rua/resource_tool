package ${package};

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import mmorpg.server.util.common.*;
<#list fileds as prop>
	<#if prop.type == "Date">
		import java.util.Date;
		<#break>
	</#if>
</#list>
import com.mongodb.BasicDBObject;
import com.lehoo.util.io.resource.ResourceListener;
import com.lehoo.util.io.resource.ResourceManager;
import mmorpg.server.util.log.Logger;
import mmorpg.server.util.log.Logger.LoggerSystem;
import com.lehoo.util.io.resource.JSONListener;
import java.util.Collections;
import java.util.Comparator;
import java.lang.reflect.Field;

<#if superClass == "">
<#else>
import com.lehoo.sob.confsuper.${superClass};
</#if>

/**
 * excel|${excelName}
 * @author administrator
 * 此类是系统自动生成类 不要直接修改，修改后也会被覆盖
 */
@JSONListener
<#if superClass == "">	
public class Conf${sheetName} {
<#else>
public class Conf${sheetName} extends ${superClass} {
</#if>
	/** 对应的数据文件 */
	private static final String JSON_NAME = "Conf${sheetName}.json";
	
	/**索引*/
	private static final String [] INDEXS = ${indexs};
	
	<#list fileds as prop>
	/** ${prop.note} */
	private ${prop.type} ${prop.name};
	</#list>
	
	/** 配置数据 */
	private static Map<Object,Conf${sheetName}> datas = new LinkedHashMap<>();
	
	/**索引结构,加快查询速度*/
	private static Map<String,Map<Object,List<Conf${sheetName}>>> indexs = new HashMap<>();
	
	
	/** 私有构造函数 */
	private Conf${sheetName}(){ }
	
	/**初始化索引*/
	private static void initIndex(){
		//初始化索引结构
		for(String index : INDEXS){
			Map<Object,List<Conf${sheetName}>> map = new HashMap<>();
			indexs.put(index, map);
		}
	}
	
	/**
	 * 加载数据，并注册监听
	 * @param path
	 */
	public static void load(String path) {
		final File file = new File(path + JSON_NAME);
		ResourceListener listener = new ResourceListener() {
			@Override
			public File listenedFile() {
				return file;
			}
			
			@Override
			public void onResourceChange(File file) {
				try {
					datas.clear();
					indexs.clear();
					initIndex();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
					String line = null;
					while ((line = reader.readLine()) != null) {
						BasicDBObject jsonObj = BasicDBObject.parse(line);
						Conf${sheetName} conf${sheetName} = new Conf${sheetName}();
						<#list fileds as prop>
						<#if prop.type == "short">
						conf${sheetName}.${prop.name} = (short)jsonObj.getInt("${prop.name}");
						<#elseif prop.type == "String">
						conf${sheetName}.${prop.name} = jsonObj.getString("${prop.name}");
						<#elseif prop.type == "byte">
						conf${sheetName}.${prop.name} = (byte)jsonObj.getInt("${prop.name}");
						<#elseif prop.type == "boolean">
						conf${sheetName}.${prop.name} = jsonObj.getBoolean("${prop.name}");
						<#elseif prop.type == "int">
						conf${sheetName}.${prop.name} = jsonObj.getInt("${prop.name}");
						<#elseif prop.type == "long">
						conf${sheetName}.${prop.name} = jsonObj.getLong("${prop.name}");
						<#elseif prop.type == "float">
						conf${sheetName}.${prop.name} = (float)jsonObj.getDouble("${prop.name}");
						<#elseif prop.type == "double">
						conf${sheetName}.${prop.name} = jsonObj.getDouble("${prop.name}");
						<#elseif prop.type == "Date">
						conf${sheetName}.${prop.name} = DateUtil.parseDataTime(jsonObj.getString("${prop.name}"));
						<#elseif prop.type == "IDCount">
						conf${sheetName}.${prop.name} = IDCount.fromString(jsonObj.getString("${prop.name}"));
						<#elseif prop.type == "IDIntValue">
						conf${sheetName}.${prop.name} = IDIntValue.fromString(jsonObj.getString("${prop.name}"));
						<#elseif prop.type == "IDValueValue">
						conf${sheetName}.${prop.name} = IDValueValue.fromString(jsonObj.getString("${prop.name}"));
						<#elseif prop.type == "IDWeight">
						conf${sheetName}.${prop.name} = IDWeight.fromString(jsonObj.getString("${prop.name}"));
						<#elseif prop.type == "IDWeightCount">
						conf${sheetName}.${prop.name} = IDWeightCount.fromString(jsonObj.getString("${prop.name}"));
						<#elseif prop.type == "Range">
						conf${sheetName}.${prop.name} = Range.fromString(jsonObj.getString("${prop.name}"));
						<#elseif prop.type == "RangeWeight">
						conf${sheetName}.${prop.name} = RangeWeight.fromString(jsonObj.getString("${prop.name}"));
						<#elseif prop.type == "String[]">
						conf${sheetName}.${prop.name} = StringUtils.toStringArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "short[]">
						conf${sheetName}.${prop.name} = StringUtils.toShortArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "byte[]">
						conf${sheetName}.${prop.name} = StringUtils.toByteArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "boolean[]">
						conf${sheetName}.${prop.name} = StringUtils.toBoolArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "int[]">
						conf${sheetName}.${prop.name} = StringUtils.toIntArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "long[]">
						conf${sheetName}.${prop.name} = StringUtils.toLongArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "float[]">
						conf${sheetName}.${prop.name} = StringUtils.toFloatArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "double[]">
						conf${sheetName}.${prop.name} = StringUtils.toDoubleArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "IDCount[]">
						conf${sheetName}.${prop.name} = StringUtils.toIDCountArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "IDIntValue[]">
						conf${sheetName}.${prop.name} = StringUtils.toIDIntValueArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "IDValueValue[]">
						conf${sheetName}.${prop.name} = StringUtils.toIDValueValueArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "IDWeight[]">
						conf${sheetName}.${prop.name} = StringUtils.toIDWeightArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "IDWeightCount[]">
						conf${sheetName}.${prop.name} = StringUtils.toIDWeightCountArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "Range[]">
						conf${sheetName}.${prop.name} = StringUtils.toRangeArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "RangeWeight[]">
						conf${sheetName}.${prop.name} = StringUtils.toRangeWeightArray(jsonObj.getString("${prop.name}"),";");
						<#elseif prop.type == "String[][]">
						conf${sheetName}.${prop.name} = StringUtils.toString2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "short[][]">
						conf${sheetName}.${prop.name} = StringUtils.toShort2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "byte[][]">
						conf${sheetName}.${prop.name} = StringUtils.toByte2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "int[][]">
						conf${sheetName}.${prop.name} = StringUtils.toInt2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "long[][]">
						conf${sheetName}.${prop.name} = StringUtils.toLong2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "float[][]">
						conf${sheetName}.${prop.name} = StringUtils.toFloat2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "double[][]">
						conf${sheetName}.${prop.name} = StringUtils.toDouble2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "IDCount[][]">
						conf${sheetName}.${prop.name} = StringUtils.toIDCount2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "IDIntValue[][]">
						conf${sheetName}.${prop.name} = StringUtils.toIDIntValue2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "IDValueValue[][]">
						conf${sheetName}.${prop.name} = StringUtils.toIDValueValue2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "IDWeight[][]">
						conf${sheetName}.${prop.name} = StringUtils.toIDWeight2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "IDWeightCount[][]">
						conf${sheetName}.${prop.name} = StringUtils.toIDWeightCount2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "Range[][]">
						conf${sheetName}.${prop.name} = StringUtils.toRange2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#elseif prop.type == "RangeWeight[][]">
						conf${sheetName}.${prop.name} = StringUtils.toRangeWeight2DimArray(jsonObj.getString("${prop.name}"),"|",";");
						<#else>
						conf${sheetName}.${prop.name} = jsonObj.get<@upperFC>${prop.type}</@upperFC>("${prop.name}");
						</#if>
						<#if prop.name == "sn">
						<#if prop.type == "String">
						datas.put(jsonObj.getString("sn"), conf${sheetName});
						<#else>
						datas.put(jsonObj.getInt("sn"), conf${sheetName});
						</#if>
						</#if>
						</#list>

						//处理索引结构
						for(String index : INDEXS){
							Map<Object, List<Conf${sheetName}>> indexMap = indexs.get(index);
							Object indexValue = conf${sheetName}.getFieldValue(index);
							
							List<Conf${sheetName}> list = indexMap.get(indexValue);
							if(list == null){
								list = new ArrayList<Conf${sheetName}>();
								indexMap.put(indexValue, list);
							}
							list.add(conf${sheetName});
						}
					}
					reader.close();
					Logger.info(Logger.LoggerSystem.LOADING, "==============载入"+file.getName()+"==============");
				} catch (Exception e) {
					Logger.error(LoggerSystem.LOADING, e, "加载配置文件失败", file.getName());
				}
			}

			@Override
			public String toString() {
				return "Conf${sheetName}";
			}
		};
		listener.onResourceChange(file);
		ResourceManager.getInstance().registerResourceListener(listener);
	}
	
	
	/**
	 * 根据主键获得数据
	 * @param sn
	 * @return
	 */
	<#list fileds as prop>
	<#if prop.name == "sn">
	<#if prop.type == "String">
	public static Conf${sheetName} getSn(String sn) {
	   return datas.get(sn);
	}
	<#else>
	public static Conf${sheetName} getSn(int sn) {
	   return datas.get(sn);
	}
	public static Conf${sheetName} getSn(String sn) {
	   return datas.get(Integer.parseInt(sn));
	}
	</#if>
	<#break>
	</#if>
	</#list>

	/**
	 * 清除所有数据
	 * @return
	 */
	public static void clearAll() {
		datas.clear();
		indexs.clear();
	}
	
	/**
	 * 数据大小
	 * @return
	 */
	public static int size(){
		return datas.size();
	}
	
	/**
	 * 获得所有数据
	 * @return
	 */
	public static Collection<Conf${sheetName}> findAll() {
		Collection<Conf${sheetName}> values = datas.values();
		List<Conf${sheetName}> result = new ArrayList<>(0);
		result.addAll(values);
		
		
		// 对结果进行排序，排序的规则是看本类中是否存在order字段
		// 如果有order字段，那么就按照此字段排序，如果没有则认为不需要进行排序。
		try {
			final Field oderFiled = Conf${sheetName}.class.getDeclaredField("order");
			Collections.sort(result, new Comparator<Conf${sheetName}>() {
				@Override
				public int compare(Conf${sheetName} o1, Conf${sheetName} o2) {
					oderFiled.setAccessible(true);
					try {
						int value1 = (int) oderFiled.get(o1);
						int value2 = (int) oderFiled.get(o2);
						return value1 - value2;
					} catch (Exception e) {
						e.printStackTrace();
					} 
					return 0;
				}
			});
		} catch(NoSuchFieldException e) {
			// 没有order 属性不进行排序
		}
		return result;
	} 
	
	/**
	 * 根据条件获得单条数据
	 * @param object
	 * @return
	 */
	public static Conf${sheetName} findBy(Object object) {
		return datas.containsKey(object) ? datas.get(object) : null;
	} 
	
	/**
	 * 根据条件获得多条数据
	 * @param params
	 * @return
	 */
	public static Collection<Conf${sheetName}> findBy(Object ... params) {
		return utilBase(params);
	} 
	
	/**
	 * 根据条件获得一条数据
	 * @param params
	 * @return
	 */
	public static Conf${sheetName} getBy(Object ... params) {
		List<Conf${sheetName}> utilBase = utilBase(params);
		if (utilBase.size() > 0) {
			return utilBase.get(0);
		}
		return null;
	}

	 /**
	 * 根据条件获得第一条数据
	 * @param params
	 * @return
	 */
	public static Conf${sheetName} findFirst(Object ... params){
		List<Conf${sheetName}> result = utilBase(params);
		if(result.size() == 0){
			return null;
		}
		
		return result.get(0);
	}
	
	/**
	 * 根据条件获得最后一条数据
	 * @param params
	 * @return
	 */
	public static Conf${sheetName} findLast(Object ... params){
		List<Conf${sheetName}> result = utilBase(params);
		if(result.size() == 0){
			return null;
		}
		
		return result.get(result.size() - 1);
	}
	
	/**
	 * 通过属性获取数据集合 支持排序
	 * @param params
	 * @return
	 */
	public static List<Conf${sheetName}> utilBase(Object...params) {
		List<Object> settings = new ArrayList<>(0);
		for (Object obj : params) {
			settings.add(obj);
		}

		// 查询参数
		final Map<String, Object> paramsFilter = new LinkedHashMap<>(0);		//过滤条件
				
		// 参数数量
		int len = settings.size();
		
		// 参数必须成对出现
		if (len % 2 != 0) {
			String param = "";
			for (Object p : params) {
				param += p + ",";
			}
			throw new RuntimeException("查询参数必须成对出现:query={" + param +"}");
		}
		
		// 处理成对参数
		for (int i = 0; i < len; i += 2) {
			String key = (String)settings.get(i);
			Object val = settings.get(i + 1);
			
			// 参数 
			paramsFilter.put(key, val);
		}
		
		// 返回结果
		List<Conf${sheetName}> result = null;
		try {
			result = utilBase(paramsFilter);
						
			// 对结果进行排序，排序的规则是看本类中是否存在order字段
			// 如果有order字段，那么就按照此字段排序，如果没有则认为不需要进行排序。
			try {
				final Field oderFiled = Conf${sheetName}.class.getDeclaredField("order");
				Collections.sort(result, new Comparator<Conf${sheetName}>() {
					@Override
					public int compare(Conf${sheetName} o1, Conf${sheetName} o2) {
						oderFiled.setAccessible(true);
						try {
							int value1 = (int) oderFiled.get(o1);
							int value2 = (int) oderFiled.get(o2);
							return value1 - value2;
						} catch (Exception e) {
							e.printStackTrace();
						} 
						return 0;
					}
				});
			} catch(NoSuchFieldException e) {
				// 没有order 属性不进行排序
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// 对结果进行排序
		return result;
	}
	
	
	/**
	 * 查询匹配的结果
	 * @param paramsFilter
	 * @return
	 */
	private static List<Conf${sheetName}> utilBase(Map<String, Object> paramsFilter){
		List<String> indexHit = new ArrayList<>();
		for(String index : INDEXS){
			if(paramsFilter.containsKey(index)){
				indexHit.add(index);
			}
		}
		
		//先找到索引命中的结果
		List<Conf${sheetName}> hitResult = null;
		if(indexHit.size() > 0){
			for(String hit : indexHit){
				Map<Object, List<Conf${sheetName}>> map = indexs.get(hit);
				List<Conf${sheetName}> list = map.get(paramsFilter.get(hit));
				if(hitResult == null){
					hitResult = list;
				}else{
					hitResult.retainAll(list);//求交集
				}
				paramsFilter.remove(hit);
			}
		}

		if(hitResult == null){
			hitResult = new ArrayList<>();
		}
		
		// 返回结果
		if(paramsFilter.size() == 0){
			return hitResult;
		}
		
		Collection<Conf${sheetName}> loopCollections = null;
		if(hitResult.size() == 0){
			loopCollections = datas.values();
		}else{
			loopCollections = hitResult;
		}
		
		List<Conf${sheetName}> result = new ArrayList<>(0);
		// 通过条件获取结果
		for (Conf${sheetName} c : loopCollections) {
			// 本行数据是否符合过滤条件
			boolean bingo = true;
			
			// 判断过滤条件
			for (Entry<String, Object> p : paramsFilter.entrySet()) {
				
				// 实际结果
				Object valTrue = c.getFieldValue(p.getKey());
				// 期望结果
				Object valWish = p.getValue();
				
				// 有不符合过滤条件的
				if (!valWish.toString().equals(valTrue.toString())) {
					bingo = false;
					break;
				}
			}
			
			// 记录符合结果
			if (bingo) {
				result.add(c);
			}
		}
		return result;
	}
	
	
	/**
	 * 获得字段值
	 * @param key
	 * @return
	 */
	private Object getFieldValue(String key) {
		Object value = null;
	
		switch (key) {
			<#list fileds as prop>
			case "${prop.name}":
				value = this.${prop.name};	
				break;
			</#list>
			default: break;
		}	
		
		return value;
	}
	
	<#list fileds as prop>
	/**
	 *获得${prop.note}
	 */
	public ${prop.type} get<@upperFC>${prop.name}</@upperFC>() {
		return ${prop.name};
	}
	
	</#list>
	
	/**
	 * byte 类型
	 * @param fieldName
	 * @return
	 */
	public byte getByteValue(String fieldName) {
		return Byte.parseByte(getFieldValue(fieldName) + "");
	}
	
	/**
	 * short 类型
	 * @param fieldName
	 * @return
	 */
	public short getShortValue(String fieldName) {
		return Short.parseShort(getFieldValue(fieldName) + "");
	}
	
	/**
	 * int 类型
	 * @param fieldName
	 * @return
	 */
	public int getIntValue(String fieldName) {
		return Integer.parseInt(getFieldValue(fieldName) + "");
	}
	
	/**
	 * long 类型
	 * @param fieldName
	 * @return
	 */
	public long getLongValue(String fieldName) {
		return Long.parseLong(getFieldValue(fieldName) + "");
	}
	
	/**
	 * boolean 类型
	 * @param fieldName
	 * @return
	 */
	public boolean getBooleanValue(String fieldName) {
		return Boolean.parseBoolean(getFieldValue(fieldName) + "");
	}
	
	/**
	 * float 类型
	 * @param fieldName
	 * @return
	 */
	public float getFloatValue(String fieldName) {
		return Float.parseFloat(getFieldValue(fieldName) + "");
	}
	
	/**
	 * double 类型
	 * @param fieldName
	 * @return
	 */
	public double getDoubleValue(String fieldName) {
		return Double.parseDouble(getFieldValue(fieldName) + "");
	}
	
	/**
	 * String 类型
	 * @param fieldName
	 * @return
	 */
	public String getStringValue(String fieldName) {
		return getFieldValue(fieldName) + "";
	}
	
	
	/**
	 * 获得数据集中的第一条数据
	 * @return
	 */
	public static Conf${sheetName} getFirst() {
		Collection<Conf${sheetName}> all = findAll();
		List<Conf${sheetName}> result = new ArrayList<>();
		result.addAll(all);		
		return result.get(0);
	}
	
	
	/**
	 * 获得数据集中的最后一条数据
	 * @return
	 */
	public static Conf${sheetName} getLast() {
		Collection<Conf${sheetName}> all = findAll();
		List<Conf${sheetName}> result = new ArrayList<>(0);
		result.addAll(all);		
		if (result.size() > 0) {
			return result.get(result.size() - 1);
		}
		
		return null;
	}
	
	/**
	 * 数据字段
	 * @author chuer
	 */
	public static final class K {
		
		<#list fileds as prop>
		/**${prop.note}*/
		public static final String ${prop.name} = "${prop.name}";
		</#list>
		
	}
	
}
