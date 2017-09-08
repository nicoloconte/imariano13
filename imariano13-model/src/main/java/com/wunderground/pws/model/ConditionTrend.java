package com.wunderground.pws.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ConditionTrend {

	private Double deltaTempC;
	private Double deltaPressureMb;
	private Double deltaRelativeHumidity;
	private Double deltaUv;
	private Double deltaSolarradiation;

	public Double getDeltaTempC() {
		return deltaTempC;
	}

	public void setDeltaTempC(Double deltaTempC) {
		this.deltaTempC = deltaTempC;
	}

	public Double getDeltaPressureMb() {
		return deltaPressureMb;
	}

	public void setDeltaPressureMb(Double deltaPressureMb) {
		this.deltaPressureMb = deltaPressureMb;
	}

	public Double getDeltaRelativeHumidity() {
		return deltaRelativeHumidity;
	}

	public void setDeltaRelativeHumidity(Double deltaRelativeHumidity) {
		this.deltaRelativeHumidity = deltaRelativeHumidity;
	}

	public Double getDeltaUv() {
		return deltaUv;
	}

	public void setDeltaUv(Double deltaUv) {
		this.deltaUv = deltaUv;
	}

	public Double getDeltaSolarradiation() {
		return deltaSolarradiation;
	}

	public void setDeltaSolarradiation(Double deltaSolarradiation) {
		this.deltaSolarradiation = deltaSolarradiation;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
