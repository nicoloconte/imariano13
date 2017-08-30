package com.wunderground.pws.model.converter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class ZonedDateTimeDynamoConverter implements DynamoDBTypeConverter<Calendar, ZonedDateTime>{

	@Override
	public Calendar convert(ZonedDateTime zonedDateTime) {
		return GregorianCalendar.from(zonedDateTime);
	}

	@Override
	public ZonedDateTime unconvert(Calendar calendar) {
		return ZonedDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
	}

}
