package io.github.chavesrodolfo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/docs")
	public String showDocs() {
		return "redirect:/swagger-ui.html";
	}

}
