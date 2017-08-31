package com.wunderground.pws.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wunderground.pws.model.CurrentObservation;
import com.wunderground.pws.service.ConditionsService;

@Controller
@RequestMapping("/conditions")
public class ConditionsRest {

	private static final Logger log = LoggerFactory.getLogger(ConditionsRest.class);
	
	@Autowired
	private ConditionsService conditionsService;

	@RequestMapping(method = RequestMethod.GET, path = "/actual")
	public @ResponseBody CurrentObservation getActualCondition() {
		log.info("getting actual conditions");
		return conditionsService.getActualCondition();
	}
}
