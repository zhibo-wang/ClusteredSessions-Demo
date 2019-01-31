package com.thq.demo.mvc.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FuncUtil {
	
	private static final Log log = LogFactory.getLog(FuncUtil.class);

	private FuncUtil() { throw new IllegalStateException("Utility class"); }

	/**
	 * 字符串处理，转换前端标签。
	 * @param obj			Object	要处理的串
	 * @param defaultVal	String	为null时的默认值
	 * @return	String				结果
	 */
	public static String convertSpecial(Object obj, String defaultVal) {
		String value = valueOf(obj, null) ;
		if(value==null) {
			return defaultVal ;
		}
		value = value.replaceAll("<br>", "\n") ;
		value = value.replaceAll("<br[\\s]*/>", "\n") ;
		return value ;
	}
	/**
	 * 字符串处理，过滤前端特殊字符。
	 * @param obj	Object		
	 * @return		String处理后的结果
	 */
	public static String valueOfEscapeSpecial(Object obj) {
		return valueOfEscapeSpecial(obj, null) ;
	}
	
	/**
	 * 字符串处理，过滤前端特殊字符。
	 * @param obj			Object	要处理的串
	 * @param defaultVal	String	为null时的默认值
	 * @return	String				结果
	 */
	public static String valueOfEscapeSpecial(Object obj, String defaultVal) {
		String value = valueOf(obj, null) ;
		if(value==null) {
			return defaultVal ;
		}
		value = JavaScriptUtils.javaScriptEscape(HtmlUtils.htmlEscape(value)) ;
		value = value.replace("&middot;", "·") ;
		return value ;
	}
	/**
	 * 字符串处理，还原被转换的特殊字符。
	 * @param obj Object
	 * @return String 处理后的结果
	 */
	public static String unEscapeSpaecial(Object obj){
		return unEscapeSpaecial(obj,null);
	}
	/**
	 * 字符串处理，还原被转换的特殊字符。
	 * @param obj   Object	要处理的串
	 * @param defaultVal    String	为null时的默认值
	 * @return String				结果
	 */
	public static String unEscapeSpaecial(Object obj,String defaultVal){
		String value = valueOf(obj, null) ;
		if(value==null) {
			return defaultVal ;
		}
		value = HtmlUtils.htmlUnescape(value).replaceAll("\\\\/","/").replaceAll("\\\\\\\\","\\\\");
		return value;
	}

	/**
	 * 字符串处理  null，undefined均视作空字符串。
	 * @param obj	final Object
	 * @return	String	处理后的结果
	 */
	public static String valueOf(final Object obj) {
		return valueOf(obj, null) ;
	}

	/**
	 * 字符串处理  null，undefined均视作空字符串。
	 * @param obj			Object	要处理的串
	 * @param defaultVal	String	默认值
	 * @return				String	处理后的结果
	 */
	public static String valueOf(final Object obj, String defaultVal) {
		if(obj==null) {
			return defaultVal ;
		}
		String str = StringUtils.trimToNull(obj.toString()) ;
		if(str==null || "null".equalsIgnoreCase(str) || "undefined".equals(str)) {
			return defaultVal ;
		}
		return str ;
	}

	/**
	 * 处理 null，为null返回0。
	 * @param obj	Object	要处理的对象
	 * @return		int		处理后的结果
	 */
	public static int parseInt(Object obj) {
		return parseInt(obj, 0) ;
	}
	
	/**
	 * 处理null为int类型，可设置默认值。
	 * @param obj			Object	要处理的对象
	 * @param defaultVal	int		默认值
	 * @return				int		处理后的结果
	 */
	public static int parseInt(Object obj, int defaultVal) {
		return Integer.parseInt(filterUnDouble(obj, String.valueOf(defaultVal))) ;
	}
	
	/**
	 * 处理 null，为null返回0。
	 * @param obj	Object	要处理的对象
	 * @return		int		处理后的结果
	 */
	public static long parseLong(Object obj) {
		return parseLong(obj, 0) ;
	}
	
	/**
	 * 处理null为int类型，可设置默认值。
	 * @param obj			Object	要处理的对象
	 * @param defaultVal	int		默认值
	 * @return				int		处理后的结果
	 */
	public static long parseLong(Object obj, long defaultVal) {
		return Long.parseLong(filterUnDouble(obj, String.valueOf(defaultVal))) ;
	}
	
	/**
	 * 处理null值为double类型，可设置默认值。
	 * @param obj			Object	要处理的对象
	 * @param defaultVal	double	要设置的默认值
	 * @return				double	处理后的结果		
	 */
	public static double parseDouble(Object obj, double defaultVal) {
		return Double.parseDouble(filterUnDouble(obj, String.valueOf(defaultVal))) ;
	}
	
	/**
	 * 处理 null，为null返回0。
	 * @param obj			Object	要处理的对象
	 * @return				double	处理后的结果
	 */
	public static double parseDouble(Object obj) {
		return parseDouble(obj, 0D) ;
	}

	/**
	 * 判断是否是中文。
	 * @param str			String	要判断的字符
	 * @return				boolean	判断结果
	 */
	public static boolean isChineseChar(String str){
		boolean temp = false ;
		Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
		Matcher m=p.matcher(str); 
		if(m.find()){ 
           temp =  true ;
		}
		return temp ;
	}
	
	/**
	 * 判断是否为数字，整形或浮点数。
	 * @param str String
	 * @return boolean
	 */
	public static boolean isNum(final String str){
		String tmp = valueOf(str) ;
		if(tmp==null){
			return false;
		}
		return tmp.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	
	/**
	 * 过滤浮点数用到的字符以外的字符。
	 * @param obj 			Object	需要过滤的字符串
	 * @param defaultVal	String	默认值
	 * @return				String	过滤后的字符串
	 */
	public static String filterUnDouble(Object obj, String defaultVal) {
		String str  = valueOf(obj) ;
		if(str == null) {
			return defaultVal ;
		}
		// 只允数字
		String regEx = "[^-0-9E|\\.]" ;
       	Pattern p = Pattern.compile(regEx) ;
       	Matcher m = p.matcher(str) ;
       	str = m.replaceAll("").trim() ;
       	if("".equals(str)) {
       		return defaultVal ;
       	}
      	return str ;
  	}
	
	/**
	 * 过滤数字以外的字符。
	 * @param obj 			Object	需要过滤的字符串
	 * @return				String	处理后的结果
	 */
	public static String filterUnNumber(Object obj) {
		return filterUnNumber(obj, "") ;
	}
	
	/**
	 * 过滤数字以外的字符。
	 * @param obj 			Object	需要过滤的字符串
	 * @param defaultVal	String	默认值
	 * @return				String	处理后的结果
	 */
	public static String filterUnNumber(Object obj, String defaultVal) {
		String str  = valueOf(obj) ;
		if(str == null) {
			return defaultVal ;
		}
		// 只允数字
		String regEx = "[^0-9]" ;
      	Pattern p = Pattern.compile(regEx) ;
      	Matcher m = p.matcher(str) ;
      	//替换与模式匹配的所有字符（即非数字的字符将被""替换）
      	return m.replaceAll("").trim() ;
 	}
	
	/**
	 * js输出。
	 * @param response		HttpServletResponse	响应
	 * @param msg				String				消息字符串
	 */
	public static void srciptOut(HttpServletResponse response, String msg) {
		FuncUtil.srciptOut(response, msg, null, false) ;
	}
	
	/**
	 * js输出。
	 * @param response			HttpServletResponse	响应
	 * @param msg				String				消息字符串
	 * @param callback			String				回调
	 * @param isGoBack			boolean				是否返回
	 */
	public static void srciptOut(HttpServletResponse response, String msg, String callback, boolean isGoBack) {
		PrintWriter pw = null ;
		StringBuffer sb = new StringBuffer() ;
		if(callback == null) {
			callback = "window.parent.$.fn.loaded(); " ;
		} 
		if(valueOf(msg) == null){
			sb.append("<script>").append(callback).append("</script>") ;
		}else{
			String prePage = "";
			if (isGoBack) {
				prePage = "history.go(-1);";
			}
			sb.append("<script>") ;
			if(msg.contains("成功") || msg.contains("完成")){
				sb.append("window.parent.ftsps.dialog.toastS('") ;
			} else {
				sb.append("window.parent.ftsps.dialog.toast('") ;
			}
			sb.append(msg).append("'); ").append(callback).append(prePage+"</script>") ;
		}
		try {
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			pw = response.getWriter() ;
			pw.write(sb.toString());
		} catch (IOException e) {
			log.error("",e);
		} finally {
			if(pw !=null ) {
				pw.close();
				pw = null ;
			}
		}
		
	}

	/**
	 * 利用反射机制获取值。
	 * @param obj			Object目标对象
	 * @param methodName	String方法名称
	 * @return				Object获取到的值对象
	 */
	public static Object reflectObject(Object obj, String methodName) {
		Class<?> clazz = obj.getClass() ;
		Method method = null ;
		Object result = null ;
		try {
			method = clazz.getMethod(methodName) ;
			result = method.invoke(obj) ;
		} catch (Exception e) {
			log.error("", e);
		}	
		return result ;
	}	
	
	/**
	 * 利用反射机制获取double类型值。
	 * @param obj			Object目标对象
	 * @param methodName	String方法名称
	 * @return				double值
	 */
	public static double reflectDouble(Object obj, String methodName) {
		return parseDouble(reflectObject(obj, methodName)) ;
	}
	
	/**
	 * 利用反射机制获取BigDecimal类型值。
	 * @param obj			Object目标对象
	 * @param methodName	String方法名称
	 * @return				double值
	 */
	public static BigDecimal reflectBigDecimal(Object obj, String methodName) {
		Object o =reflectObject(obj, methodName);
		String value = "0.0";
		if(o != null){
			value = valueOf(o);
		}
		return new BigDecimal(value) ;
	}

	/**
	 * 利用反射机制获取int类型的值。
	 * @param obj			Object目标对象
	 * @param methodName	String方法名称
	 * @return				int	获取到的int类型的值
	 */
	public static int reflectInt(Object obj, String methodName) {
		return parseInt(reflectObject(obj, methodName)) ;
	}

	/**
	 * 利用反射机制获取String类型的值。
	 * @param obj				Object目标对象
	 * @param methodName		String方法名称
	 * @return					String获取到的String类型的值	
	 */
	public static String reflectString(Object obj, String methodName) {
		return valueOf(reflectObject(obj, methodName)) ;
	}
    /**
     * 利用反射机制set值。
     * @param classObj          目标对象
     * @param methodName   setting方法名称
     * @param valueObj          传入的值
     */
    public static void reflectSetValue(Object classObj, String methodName,Object valueObj){
        Class<?> clazz = classObj.getClass();
        Method method = null;
        try {	
            method = clazz.getMethod(methodName, valueObj.getClass());
            method.invoke(classObj, valueObj);
        } catch (Exception e) {
            log.error("", e);
        }
    }

	/**
	 * 生成32位UUID。
	 * @return String 基于当前时间生成32位UUID
	 */
	public static String generateUUID(){
		UUID uuid = UUID.randomUUID();
		// 得到对象产生的ID
		String uuidStr = uuid.toString();
		// 转换为大写
		uuidStr = uuidStr.toUpperCase();
		// 替换-
		uuidStr = uuidStr.replaceAll("-", "");
		return uuidStr;
	}

	/**
	 * @param mobilePhone
	 * @return
	 */
	public static String getSafePhone(String mobilePhone) {
		mobilePhone = FuncUtil.filterUnNumber(mobilePhone) ;
		if(StringUtils.isEmpty(mobilePhone)) {
			return "" ;
		}
		if(mobilePhone.length()>=7) {
			mobilePhone = mobilePhone.substring(0, 3) + "****" + mobilePhone.substring(7) ;
		} 
		return mobilePhone ;
	}
	
	/**
	 * 格式化打印金额。
	 * @param value Object
	 * @return String
	 */
	public static String formatMoney(Object value) {
		return formatMoney(value, true) ;
	}

	/**
	 * 格式化打印金额。
	 * @param value Object
	 * @param zeroEmpty 数据0显示为空
	 * @return String
	 */
	public static String formatMoney(Object value, boolean zeroEmpty) {
		return formatMoney(value, zeroEmpty, "###,###,###,##0.00");
	}

	/**
	 * 格式化打印金额。
	 * @param value Object
	 * @param zeroEmpty 数据0显示为空
	 * @param pattern 数据格式（默认"###,###,###,##0.00"）
	 * @return String
	 */
	public static String formatMoney(Object value, boolean zeroEmpty, String pattern) {
		double je = FuncUtil.parseDouble(value) ;
		if(je==0 && zeroEmpty) {
			return "" ;
		}
		if(isEmpty(pattern)){
			pattern = "###,###,###,##0.00";
		}
		return new DecimalFormat(pattern).format(je) ;
	}

	/**
	 * 字符截断处理
	 * @param str		字符串
	 * @param len		长度
	 */
	public static String ellipsisString(String str, int len) {
		if(str == null || str.length() <= len) {
			return str ;
		}
		return str.substring(0, len) + "..." ;
	}

	/**
	 * 判断对象是否为空
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;
		else if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;
		else if (obj instanceof Collection)
			return ((Collection<?>) obj).isEmpty();
		else if (obj instanceof Map)
			return ((Map<?, ?>) obj).isEmpty();
		else if (obj.getClass().isArray())
			return Array.getLength(obj) == 0;
		return false;
	}

	/**
	 * 判断对象是否非空
	 *
	 * @param obj 对象
	 * @return {@code true}: 非空<br>{@code false}: 空
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmptyOrZero(String value) {
		return StringUtils.isBlank(value) || value.matches("^0+[0|\\\\.]*$");
	}

	/**
	 * 将字符串split并trim
	 * @param input
	 * @param reg
	 * @return
	 */
	public static String[] splitTrim(String input, String reg) {
		return input.trim().split("\\s*" + reg + "\\s*");
	}

/*
	public static void main(String[] args) {
		String test = FuncUtil.valueOfEscapeSpecial("热河木提江·阿不力米提买买提?", "") ;
		System.out.println(test + " == " + test.length());//15
		test = HtmlUtils.htmlUnescape(test) ;
		System.out.println(test + " == " + test.length());//15
		System.out.println("000020180000000" + " == " + FuncUtil.isEmptyOrZero("000020180000000"));//false
		System.out.println("000000000000000" + " == " + FuncUtil.isEmptyOrZero("000000000000000"));//true
		System.out.println("isEmpty(\"0\")" + " == " + FuncUtil.isEmpty("0"));//false
		System.out.println("isEmpty(List)" + " == " + FuncUtil.isEmpty(new ArrayList<>()));
		System.out.println("isEmpty(Map)" + " == " + FuncUtil.isEmpty(new HashMap<>()));
		System.out.println("isEmpty(Array)" + " == " + FuncUtil.isEmpty(new int[1]));//false
		double a = 1.23456, b = 1.23457;
		System.out.println("isNumberEquals" + " == " + FuncUtil.isNumberEquals(a+0.001, b));//true
	}*/
}
