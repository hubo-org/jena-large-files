package com.vachan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vachan.service.InferenceService;

/**
 * 
 * @author kalyan
 *
 */

@Controller
@RequestMapping("/")
public class TestController {

	@Autowired
	@Qualifier("infService")
	private InferenceService infService;

	@RequestMapping(value = "/loadSingleNode", method = RequestMethod.GET)
	public String loadSingleNode() throws Exception {
		infService.loadSingle();
		return "Completed";
	}

	@RequestMapping(value = "/loadInput", method = RequestMethod.GET)
	public String loadInput(@RequestParam String file) throws Exception {
		String fileName = file + ".txt";
		infService.loadInputFile(fileName);
		return "Completed";
	}

	@RequestMapping(value = "/loadFullData", method = RequestMethod.GET)
	public String loadFullData() throws Exception {
		infService.loadFullFile();
		return "Completed";
	}
}
