
package com.wunderground.pws.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Time {

	@JsonProperty("hour")
	private String hour;
	@JsonProperty("minute")
	private String minute;

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
