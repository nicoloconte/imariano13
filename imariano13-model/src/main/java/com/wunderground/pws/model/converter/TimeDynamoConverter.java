package com.wunderground.pws.model.converter;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.wunderground.pws.model.Time;

public class TimeDynamoConverter implements DynamoDBTypeConverter<String, Time>{

	@Override
	public String convert(Time time) {
		if(time == null){
			return "";
		}
		return time.getHour().concat(":").concat(time.getMinute());
	}

	@Override
	public Time unconvert(String stringTime) {
		if(StringUtils.isBlank(stringTime)){
			return new Time();
		}
		String[] split = stringTime.split(":");
		Time time = new Time();
		time.setHour(split[0]);
		time.setMinute(split[1]);
		return time;
	}

}
