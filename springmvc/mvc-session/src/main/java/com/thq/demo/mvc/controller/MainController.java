package com.thq.demo.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class MainController {
	@RequestMapping("main")
	public String main(ModelMap map) {
		map.put("msg", "Hello mvc.");
		return "main";
	}

	@RequestMapping(value = "welcome", method = RequestMethod.GET)
	@ResponseBody
	public String welcome() {
		return "Hello MVC!";
	}

	@ResponseBody
	@RequestMapping("list")
	public List<String> list() {
		List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("cc");
		return list;
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	@ResponseBody
	public String add(HttpServletRequest request) {
		String sessionID = request.getSession().getId();
		System.out.println("sessionID=" + sessionID);

		Integer testKey = (Integer)request.getSession().getAttribute("testKey");
		testKey = testKey ==null ? 1 : ++testKey;
		request.getSession().setAttribute("testKey", testKey);
		System.out.println("testKey=" + testKey);
		return sessionID + " --- " + testKey;
	}

	@RequestMapping(value = "del", method = RequestMethod.GET)
	@ResponseBody
	public String del(HttpServletRequest request) {
		String sessionID = request.getSession().getId();
		System.out.println("sessionID=" + sessionID);

		Integer testKey = (Integer)request.getSession().getAttribute("testKey");
		testKey = 0;
		request.getSession().setAttribute("testKey", testKey);
		System.out.println("testKey=" + testKey);
		return sessionID + " --- " + testKey;
	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	@ResponseBody
	public String get(HttpServletRequest request) {
		String sessionID = request.getSession().getId();
		System.out.println("sessionID=" + sessionID);

		Integer testKey = (Integer)request.getSession().getAttribute("testKey");
		testKey = testKey ==null ? 0 : testKey;
		request.getSession().setAttribute("testKey", testKey);
		System.out.println("testKey=" + testKey);
		return sessionID + " --- " + testKey;
	}

}