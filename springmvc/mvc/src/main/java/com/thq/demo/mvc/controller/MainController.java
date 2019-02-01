package com.thq.demo.mvc.controller;

import com.thq.demo.mvc.utils.HttpRequestUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping(value = "/")
public class MainController {
	@RequestMapping("main")
	public String main(ModelMap map) {
		map.put("msg", "Hello mvc.");
		return "main";
	}

	@RequestMapping(value = "welcome")
	public String welcome() {
		return "screenCut";
	}

	@RequestMapping(value = "sendImageToBaidu")
	@ResponseBody
	public String sendImageToBaidu(String base64Img) throws Exception {
		String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
		Map<String, String> header = new HashMap<>();
		header.put("Content-Type", "application/x-www-form-urlencoded");
		Map<String, Object> mParam = new HashMap<>();
		mParam.put("access_token", "24.d30774321ce0fa25914e21166766f5d3.2592000.1551317879.282335-15372905");
		try {
			mParam.put("image", base64Img);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = HttpRequestUtil.httpRequest("POST", url, mParam, header);
		System.out.println(result);
		return result;
	}


	@ResponseBody
	@RequestMapping("list")
	public String list() {
		List<String> list = new ArrayList<>();
		list.add("a广发");
		list.add("咖");
		list.add("cc");
		return list.toString();
	}

}