package com.matin.taxi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
class WebTaxiController {

	@GetMapping("/last/taxi")
	public String initFindForm() {
		return "last/taxi";
	}

	
}
