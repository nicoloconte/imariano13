package com.wunderground.pws.model.converter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;

public class ZonedDateTimeDynamoMarshaller implements DynamoDBMarshaller<ZonedDateTime> {

	@Override
	public String marshall(ZonedDateTime zonedDateTime) {
		return DateTimeFormatter.ISO_INSTANT.format(zonedDateTime);
	}

	@Override
	public ZonedDateTime unmarshall(Class<ZonedDateTime> clazz, String stringDate) {
		return Instant.parse(stringDate).atZone(ZoneId.systemDefault());
	}

}
