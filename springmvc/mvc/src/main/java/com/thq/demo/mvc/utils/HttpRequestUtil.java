/**
 * Copyright(c) Beijing Kungeek Science & Technology Ltd. 
 */
package com.thq.demo.mvc.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpRequestUtil {

	private static final Log log = LogFactory.getLog(HttpRequestUtil.class);

	private static SSLConnectionSocketFactory sslsf = null;
	private static PoolingHttpClientConnectionManager cm = null;

	private static void init(){
		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(20);
		cm.setDefaultMaxPerRoute(50);

		SSLContext sslContext;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				//信任所有
				@Override
				public boolean isTrusted(X509Certificate[] xcs, String string){
					return true;
				}
			}).build();

			sslsf = new SSLConnectionSocketFactory(sslContext);
		} catch (Exception e) {
			log.error("构建SSLContext失败", e);
		}
	}

	/**
	 * httpPost。
	 * @param url String
	 * @return String
	 */
	public static String httpPost(String url) throws Exception {

		return httpPost(url, null, null);
	}

	/**
	 * httpPost。
	 * @param url String
	 * @param se HttpEntity
	 * @return String
	 */
	public static String httpPost(String url, HttpEntity se) throws Exception {
		HttpPost http = new HttpPost(url);
		if(se != null){
			http.setEntity(se);
		}
		return httpRequest(http);
	}

	/**
	 * httpPost。
	 * @param url String
	 * @param se HttpEntity
	 * @return String
	 */
	public static String httpPost(String url, Map<String, String> header, HttpEntity se) throws Exception {
		HttpPost http = new HttpPost(url);
		if(se != null){
			http.setEntity(se);
		}
		// 设置头信息
		if (MapUtils.isNotEmpty(header)) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				http.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return httpRequest(http);
	}

	/**
	 * httpPost。
	 * @param url String
	 * @param header Map<String, String>
	 * @return String
	 */
	public static String httpPostWithHeader(String url, Map<String, String> header) throws Exception {

		return httpPost(url, header, null);
	}

	/**
	 * httpPost。
	 * @param url String
	 * @param mParam Map<String, Object>
	 * @param header Map<String, String>
	 * @return String
	 */
	public static String httpPostByParam(String url, Map<String, Object> mParam, Map<String, String> header) throws Exception {
		// 封装参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if(FuncUtil.isNotEmpty(mParam)) {
			for (String key : mParam.keySet()) {
				Object o = mParam.get(key);
				if (o == null) {
					continue;
				} else if (o instanceof String[]) {
					String[] aValues = (String[]) o;
					for (String aValue : aValues) {
						log.debug("params:"+key+"="+aValue);
						nvps.add(new BasicNameValuePair(key, aValue));
					}
				} else {
					nvps.add(new BasicNameValuePair(key, o.toString()));
				}
			}
		}
		UrlEncodedFormEntity uefeParams = new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8);
		// 执行查询
		return httpPost(url, header, uefeParams);
	}
	
	/**
	 * httpPost。
	 * @param url String
	 * @param param String 
	 * @return String
	 */
	public static String httpPostByString(String url, String param, Map<String, String> header) throws Exception {
		// 封装参数
        StringEntity entity = new StringEntity(param, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
		// 执行查询
		return httpPost(url, header, entity);
	}
	
	/**
	 * httpGet。
	 * @param url String
	 * @return String
	 */
	public static String httpGet(String url) throws Exception {
		HttpGet http = new HttpGet(url);
		return httpRequest(http);
	}

	/**
	 * httpGet。
	 * @param url String
	 * @param encoding String
	 * @return String
	 */
	public static String httpGet(String url, String encoding) throws Exception {
		HttpGet http = new HttpGet(url);
		return httpRequest(http, encoding);
	}

	/**
	 * httpGet。
	 * @param url String
	 * @param header Map<String, String>
	 * @return String
	 */
	public static String httpGetWithHeader(String url, Map<String, String> header) throws Exception {
		HttpGet http = new HttpGet(url);
		// 设置头信息
		if (MapUtils.isNotEmpty(header)) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				http.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return httpRequest(http);
	}

	/**
	 *
	 * @param method
	 * @param url
	 * @param mParam
	 * @param header
	 * @return
	 */
	public static String httpRequest(String method, String url, Map<String, Object> mParam, Map<String, String> header) throws Exception {
		if("GET".equalsIgnoreCase(method)){
			return httpGetWithHeader(url, header);
		} else if("POST".equalsIgnoreCase(method)){
			return httpPostByParam(url, mParam, header);
		}
		return "";
	}

	/**
	 * httpRequest。
	 * @param http HttpRequestBase
	 * @return String
	 */
	public static String httpRequest(HttpRequestBase http) throws Exception {
		return httpRequest(http, null);
	}

	/**
	 * httpRequest。
	 * @param http HttpRequestBase
	 *  @param encoding String
	 * @return String
	 */
	public static String httpRequest(HttpRequestBase http, String encoding) throws Exception {
		CloseableHttpClient httpclient = wrapHttpClient(http);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(http);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			String result = "";
			if (StringUtils.isEmpty(encoding)) {
				result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
			} else {
				result = EntityUtils.toString(entity, encoding);
			}
			if (statusCode != 200) {
				log.error("访问错误：[" + statusCode + "]" + result);
				throw new Exception("访问错误：[" + statusCode + "]" + result);
			}
			return result;
		} catch (IOException e) {
			log.error("", e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				log.error("", e);
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error("", e);
			}
		}
		return "";
	}

	private static CloseableHttpClient wrapHttpClient(HttpRequestBase http) {
		init();
		HttpClientBuilder custom = HttpClients.custom();
		if(cm != null){
			custom.setConnectionManager(cm);
		}
		if(http.getURI().getScheme().startsWith("https") && sslsf != null) {
			custom.setSSLSocketFactory(sslsf);
		}
		return custom.build();
	}

	/**
	 * httpsRequest。
	 * @param http HttpRequestBase
	 *  @param encoding String
	 * @return String
	 */
	public static String httpsRequest(final HttpRequestBase http, String encoding) {
		String result = "";

		CloseableHttpClient httpClient = wrapHttpClient(http);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(http);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					if (FuncUtil.isEmpty(encoding)) {
						result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
					} else {
						result = EntityUtils.toString(entity, encoding);
					}
				}
			}
		} catch (IOException ex) {
			log.error("", ex);
		} finally {
			if (null != response) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException ex) {
					log.error("", ex);
				}
			}
		}

		return result;
	}

	public static void main(String[] args) throws Exception {
		String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general";
		// String url = "https://etax.zhejiang.chinatax.gov.cn/behaviorlog/save/logurl.do";
		Map<String, String> header = new HashMap<>();
		header.put("Content-Type", "application/x-www-form-urlencoded");
		Map<String, Object> mParam = new HashMap<>();
		mParam.put("access_token", "24.d30774321ce0fa25914e21166766f5d3.2592000.1551317879.282335-15372905");
		// 本地图片路径
		String filePath = "D:\\MyDocument\\Desktop\\CAS协议流程图.png";
		try {
			byte[] imgData = readFileByBytes(filePath);
			String imgStr = Base64.encodeBase64String(imgData);
			System.out.println(imgStr);
			String image = URLEncoder.encode(imgStr, "UTF-8");
			System.out.println(image);
			mParam.put("image", imgStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long start = System.currentTimeMillis();
		String result = HttpRequestUtil.httpRequest("POST", url, mParam, header);
		System.out.println(result);
		System.out.println("耗时：" + (System.currentTimeMillis() - start));
	}

	/**
	 * 根据文件路径读取byte[] 数组
	 */
	public static byte[] readFileByBytes(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException(filePath);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
			BufferedInputStream in = null;

			try {
				in = new BufferedInputStream(new FileInputStream(file));
				short bufSize = 1024;
				byte[] buffer = new byte[bufSize];
				int len1;
				while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
					bos.write(buffer, 0, len1);
				}

				byte[] var7 = bos.toByteArray();
				return var7;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException var14) {
					var14.printStackTrace();
				}

				bos.close();
			}
		}
	}
}
