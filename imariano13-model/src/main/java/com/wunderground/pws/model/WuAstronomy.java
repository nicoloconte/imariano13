
package com.wunderground.pws.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wunderground.pws.model.entities.MoonPhase;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WuAstronomy {

	@JsonProperty("moon_phase")
	private MoonPhase moonPhase;

	public MoonPhase getMoonPhase() {
		return moonPhase;
	}

	public void setMoonPhase(MoonPhase moonPhase) {
		this.moonPhase = moonPhase;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
