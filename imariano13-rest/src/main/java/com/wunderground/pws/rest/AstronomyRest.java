package com.wunderground.pws.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wunderground.pws.model.entities.MoonPhase;
import com.wunderground.pws.service.AstronomyService;

@Controller
@RequestMapping("/astronomy")
public class AstronomyRest {

	private static final Logger log = LoggerFactory.getLogger(AstronomyRest.class);
	
	@Autowired
	private AstronomyService astronomyService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody MoonPhase getAstronomy() {
		log.info("getting astronomy");
		return astronomyService.getAstronomy();
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/trend")
	public @ResponseBody Integer	 getAstronomyTrend() {
		log.info("getting getAstronomyTrend");
		return astronomyService.getDayLengthTrend();
	}
}
