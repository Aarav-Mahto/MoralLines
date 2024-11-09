package com.story.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OtherPageController {
	
	@GetMapping("/info/mstcecommerce")
	public String getMstcecommerce() {
		return "mstcecommerce";
	}
}
