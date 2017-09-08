package com.wunderground.pws.service;

import com.wunderground.pws.model.entities.MoonPhase;

public interface AstronomyService {

	public void saveAstronomy(MoonPhase moonPhase);

	public MoonPhase getAstronomy();

	public Integer getDayLengthTrend();

}
