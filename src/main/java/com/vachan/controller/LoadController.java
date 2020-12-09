package com.vachan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vachan.service.InferenceService;

/**
 * 
 * @author kalyan
 *
 */

@RestController("/jena")
public class LoadController {

	@Autowired
	@Qualifier("infService")
	private InferenceService infService;

	@GetMapping("/loadTest")
	public ResponseEntity<String> loadTest() throws Exception {
		return new ResponseEntity<String>("Completed", HttpStatus.ACCEPTED);
	}

	@GetMapping("/loadSingleNode")
	public ResponseEntity<String> loadSingleNode() throws Exception {
		long time_taken = infService.loadSingle();
		String resp_text = "Time taken:" + time_taken + "ms";
		return new ResponseEntity<String>(resp_text, HttpStatus.ACCEPTED);
	}

	@GetMapping("/loadInput")
	public ResponseEntity<String> loadInput(@RequestParam String file) throws Exception {
		String fileName = file + ".txt";
		long time_taken = infService.loadInputFile(fileName);
		String resp_text = "Time taken:" + time_taken + "ms";
		return new ResponseEntity<String>(resp_text, HttpStatus.ACCEPTED);
	}

	@GetMapping("/loadFullData")
	public ResponseEntity<String> loadFullData() throws Exception {
		long time_taken = infService.loadFullFile();
		String resp_text = "Time taken:" + time_taken + "ms";
		return new ResponseEntity<String>(resp_text, HttpStatus.ACCEPTED);
	}
}
