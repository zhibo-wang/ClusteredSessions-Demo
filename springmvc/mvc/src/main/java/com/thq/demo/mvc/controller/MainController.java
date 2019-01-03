package com.thq.demo.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
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

	@RequestMapping(value = "welcome", method = RequestMethod.GET)
	@ResponseBody
	public String welcome() {
		return "Hello Spring MVC!";
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

}