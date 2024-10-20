package com.matin.taxi.db;


import org.springframework.stereotype.Controller;

@Controller
public class SimulateController {

	public static SimulateRepository get()
	{
		return simulate;
	}

	private static SimulateRepository simulate;

	public SimulateController(SimulateRepository clinicService) {
		simulate = clinicService;
	}


	




}
