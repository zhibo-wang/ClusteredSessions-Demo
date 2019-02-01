package com.thq.demo.mvc.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;


/**
 * <pre>
 * JSON工具类。
 * </pre>
 * @author 周家鑫 zhoujiaxin@kungeek.com
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容: 
 * </pre>
 */
public abstract class FtspJSONUtil {
	

	private static final Log log = LogFactory.getLog(FtspJSONUtil.class);
	/** objectMapper。**/
	public static final ObjectMapper objectMapper;
	
	/**
	 * static。
	 */
	static {
		objectMapper = new ObjectMapper();

		// 去掉默认的时间戳格式
		// objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
		// false);
		// 设置为中国上海时区
		 objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	
		objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 反序列化时，属性不存在的兼容处理
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 单引号处理
		//objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		
		//忽略属性的双引号
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); 
		// 日期的统一格式
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		//设置属性为Null的时候不序列化
		objectMapper.setSerializationInclusion(Include.NON_NULL);		

	}

	/**
	 * Json字符串转成对象。
	 * 
	 * @param json
	 *            Json字符串
	 * @param clazz
	 *            对象的类
	 * @param <T>
	 *            对象类型
	 * @return T
	 */
	public static <T> T objParseToJSONObject(Object json, Class<T> clazz) {
		try {
			String jsonStr = objectToJsonString(json);
			return objectMapper.readValue(jsonStr, clazz);
		} catch (JsonParseException e) {
			log.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
		/**
		 * Json字符串转成对象。
		 * 
		 * @param json
		 *            Json字符串
		 * @param clazz
		 *            对象的类
		 * @param <T>
		 *            对象类型
		 * @return T
		 */
		public static <T> T parseToJSONObject(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			log.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 对象转换为json串。
	 * 
	 * @param object
	 *            目标对象
	 * @return String 目标对象的jsonString
	 */
	public static String objectToJsonString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * Json字符串转成集合对象。
	 * 
	 * @param json
	 *            String
	 * @param clz
	 *            类型
	 * @param <T>
	 *            对象类型
	 * @return List<T>
	 */
	public static <T> List<T> toCollection(String json, Class<T> clz) {

		List<T> list = null;
		try {
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clz);
			list = objectMapper.readValue(json, javaType);
		} catch (JsonParseException e) {
			log.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * json串转换为Map。
	 * 
	 * @param jsonString
	 *            String
	 * @return Map<String, Object>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseToMap(String jsonString) {
		try {
			return objectMapper.readValue(jsonString, Map.class);
		} catch (JsonParseException e) {
			log.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取过滤对应字段的JsonConfig。
	 * @param t T
	 * @param propertyes String
	 * @return JsonConfig 获取到的config
	 */
	public static <T> ObjectNode objectToJsonStringFiled(T t, String... propertyes) {
		
		ObjectMapper om = new ObjectMapper();

		om.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 反序列化时，属性不存在的兼容处理
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		// 日期的统一格式
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		
		SimpleFilterProvider filter = new SimpleFilterProvider().addFilter(t.getClass().getName(), 
                SimpleBeanPropertyFilter.filterOutAllExcept(propertyes));
		filter.setFailOnUnknownId(false);
		om.setFilterProvider(filter); 
		
		om.setAnnotationIntrospector(new JacksonAnnotationIntrospector() 
        { 
            /**
			 * 序列号。
			 */
			private static final long serialVersionUID = 6436459496039339288L;

			@Override 
            public Object findFilterId(Annotated annotated) {
            	return annotated.getName();
            };
        }); 

		try {
			ObjectNode jsonNode = om.readValue(om.writeValueAsString(t), ObjectNode.class);
			return jsonNode;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}
	
	/**
	 * 获取过滤对应字段的JsonConfig。
	 * @param t T
	 * @param filter 过滤器
	 * @return ObjectNode 获取到的config
	 */
	public static <T> ObjectNode objectToJsonStringFiled(T t,SimpleFilterProvider filter) {
		
		ObjectMapper om = new ObjectMapper();

		om.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 反序列化时，属性不存在的兼容处理
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		// 日期的统一格式
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		filter.setFailOnUnknownId(false);
		om.setFilterProvider(filter); 
		
		om.setAnnotationIntrospector(new JacksonAnnotationIntrospector() 
        { 
            /**
			 * 序列号。
			 */
			private static final long serialVersionUID = 6436459496039339288L;

			@Override 
            public Object findFilterId(Annotated annotated) {
            	return annotated.getName();
            };
        }); 

		try {
			ObjectNode jsonNode = om.readValue(om.writeValueAsString(t), ObjectNode.class);
			return jsonNode;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return objectMapper.getNodeFactory().objectNode();
	}
	
	/**
	 * 获取过滤对应字段的JsonConfig。
	 * @param list List<T>
	 *  @param propertyes String
	 * @return JsonConfig 获取到的config
	 */
	public static <T> ArrayNode listToJsonStringFiled(List<T> list, String... propertyes) {
		
		if(list == null || list.size() == 0){
			return objectMapper.getNodeFactory().arrayNode();
		}
		
		ObjectMapper om = new ObjectMapper();
		T t=list.get(0);
		om.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 反序列化时，属性不存在的兼容处理
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		// 日期的统一格式
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		
		FilterProvider filters = new SimpleFilterProvider().addFilter(t.getClass().getName(), 
                SimpleBeanPropertyFilter.filterOutAllExcept(propertyes)); 
		om.setFilterProvider(filters); 
		
		om.setAnnotationIntrospector(new JacksonAnnotationIntrospector() 
        { 
            /**
			 * 序列号。
			 */
			private static final long serialVersionUID = 6436459496039339288L;

			@Override 
            public Object findFilterId(Annotated annotated) {
            	return annotated.getName();
            };
        }); 

		try {
			ArrayNode jsonNode = om.readValue(om.writeValueAsString(list), ArrayNode.class);
			return jsonNode;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return objectMapper.getNodeFactory().arrayNode();
	}
	
	
	/**获取JsonNode 对象。
	 * @param json 字符串
	 * @return JsonNode
	 */
	public  static JsonNode getJsonNode(String json) {
		try {
			return objectMapper.readValue(json, JsonNode.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	/**
	 * 获取json字符串，解决不含key返回错误问题。
	 * @param jsonNode		JsonNode
	 * @param key		String
	 * @return String	获取到的字符串
	 */
	public static String getString(JsonNode jsonNode, String key) {
		if (jsonNode==null){
			return null;
		}
		JsonNode tempNode=jsonNode.findValue(key);
		
		if (tempNode != null) {
			if (tempNode instanceof ObjectNode || tempNode instanceof ArrayNode) {
				return tempNode.toString();
			} else {
				return tempNode.asText();
			}
		}
		
		return null;
	}
	
	/**
	 * 获取json字符串，解决不含key返回错误问题。
	 * @param jsonNode		JsonNode
	 * @param key		String
	 * @return String	获取到的字符串
	 */
	public static double getDouble(JsonNode jsonNode, String key) {
		
		JsonNode tempNode=jsonNode.findValue(key);
		
		if(tempNode != null){
			return Double.parseDouble(tempNode.toString());
		}
		
		return 0;
		
	}

	/**
	 * Obj对像转换为JsonNode。
	 * @param object 数据对象
	 * @return JsonNode
	 */
	public static JsonNode getJsonNodeFromObj(Object object){
		String json = objectToJsonString(object);
		if (StringUtils.isNotBlank(json)){
			return getJsonNode(json);
		}
		return null;
	}
	
}
