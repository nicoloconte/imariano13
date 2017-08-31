package com.wunderground.pws.rest;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wunderground.pws.model.ObservationMinMax;
import com.wunderground.pws.service.MinMaxService;

@Controller
@RequestMapping("/conditions")
public class MinMaxRest {

	private static final Logger log = LoggerFactory.getLogger(MinMaxRest.class);

	@Autowired
	private MinMaxService minMaxService;

	@RequestMapping(method = RequestMethod.GET, path = "/extremes")
	public @ResponseBody Map<String, List<ObservationMinMax>> getMinMax() {
		log.info("getting MIN MAX");
		return minMaxService.getMinMax();
	}
}
