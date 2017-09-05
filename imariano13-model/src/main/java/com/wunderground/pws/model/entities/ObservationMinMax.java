package com.wunderground.pws.model.entities;

import java.time.ZonedDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.wunderground.pws.model.converter.ZonedDateTimeDynamoConverter;

@DynamoDBTable(tableName = "ConditionsMinMax")
public class ObservationMinMax {

	@DynamoDBHashKey
	private String id;

	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime timestampUpdate = ZonedDateTime.now();

	@DynamoDBAttribute
	private Double tempC;
	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime tempCObservationTime;

	@DynamoDBAttribute
	private Double windKph;
	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime windKphObservationTime;

	@DynamoDBAttribute
	private Double windGustKph;
	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime windGustKphObservationTime;

	@DynamoDBAttribute
	private Double pressureMb;
	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime pressureMbObservationTime;

	@DynamoDBAttribute
	private Integer dewpointC;
	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime dewpointCObservationTime;

	@DynamoDBAttribute
	private Double feelslikeC;
	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime feelslikeCObservationTime;

	@DynamoDBAttribute
	private Double solarradiation;
	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime solarradiationObservationTime;

	@DynamoDBAttribute
	private Double uV;
	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime uVObservationTime;

	@DynamoDBAttribute
	private Double precip1hrMetric;
	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime precip1hrMetricObservationTime;

	@DynamoDBAttribute
	private Double precipTodayMetric;
	@DynamoDBTypeConverted(converter = ZonedDateTimeDynamoConverter.class)
	private ZonedDateTime precipTodayMetricObservationTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ZonedDateTime getTimestampUpdate() {
		return timestampUpdate;
	}

	public void setTimestampUpdate(ZonedDateTime timestampUpdate) {
		this.timestampUpdate = timestampUpdate;
	}

	public Double getTempC() {
		return tempC;
	}

	public void setTempC(Double tempC) {
		this.tempC = tempC;
	}

	public ZonedDateTime getTempCObservationTime() {
		return tempCObservationTime;
	}

	public void setTempCObservationTime(ZonedDateTime tempCObservationTime) {
		this.tempCObservationTime = tempCObservationTime;
	}

	public Double getWindKph() {
		return windKph;
	}

	public void setWindKph(Double windKph) {
		this.windKph = windKph;
	}

	public ZonedDateTime getWindKphObservationTime() {
		return windKphObservationTime;
	}

	public void setWindKphObservationTime(ZonedDateTime windKphObservationTime) {
		this.windKphObservationTime = windKphObservationTime;
	}

	public Double getWindGustKph() {
		return windGustKph;
	}

	public void setWindGustKph(Double windGustKph) {
		this.windGustKph = windGustKph;
	}

	public ZonedDateTime getWindGustKphObservationTime() {
		return windGustKphObservationTime;
	}

	public void setWindGustKphObservationTime(ZonedDateTime windGustKphObservationTime) {
		this.windGustKphObservationTime = windGustKphObservationTime;
	}

	public Double getPressureMb() {
		return pressureMb;
	}

	public void setPressureMb(Double pressureMb) {
		this.pressureMb = pressureMb;
	}

	public ZonedDateTime getPressureMbObservationTime() {
		return pressureMbObservationTime;
	}

	public void setPressureMbObservationTime(ZonedDateTime pressureMbObservationTime) {
		this.pressureMbObservationTime = pressureMbObservationTime;
	}

	public Integer getDewpointC() {
		return dewpointC;
	}

	public void setDewpointC(Integer dewpointC) {
		this.dewpointC = dewpointC;
	}

	public ZonedDateTime getDewpointCObservationTime() {
		return dewpointCObservationTime;
	}

	public void setDewpointCObservationTime(ZonedDateTime dewpointCObservationTime) {
		this.dewpointCObservationTime = dewpointCObservationTime;
	}

	public Double getFeelslikeC() {
		return feelslikeC;
	}

	public void setFeelslikeC(Double feelslikeC) {
		this.feelslikeC = feelslikeC;
	}

	public ZonedDateTime getFeelslikeCObservationTime() {
		return feelslikeCObservationTime;
	}

	public void setFeelslikeCObservationTime(ZonedDateTime feelslikeCObservationTime) {
		this.feelslikeCObservationTime = feelslikeCObservationTime;
	}

	public Double getSolarradiation() {
		return solarradiation;
	}

	public void setSolarradiation(Double solarradiation) {
		this.solarradiation = solarradiation;
	}

	public ZonedDateTime getSolarradiationObservationTime() {
		return solarradiationObservationTime;
	}

	public void setSolarradiationObservationTime(ZonedDateTime solarradiationObservationTime) {
		this.solarradiationObservationTime = solarradiationObservationTime;
	}

	public Double getuV() {
		return uV;
	}

	public void setuV(Double uV) {
		this.uV = uV;
	}

	public ZonedDateTime getuVObservationTime() {
		return uVObservationTime;
	}

	public void setuVObservationTime(ZonedDateTime uVObservationTime) {
		this.uVObservationTime = uVObservationTime;
	}

	public Double getPrecip1hrMetric() {
		return precip1hrMetric;
	}

	public void setPrecip1hrMetric(Double precip1hrMetric) {
		this.precip1hrMetric = precip1hrMetric;
	}

	public ZonedDateTime getPrecip1hrMetricObservationTime() {
		return precip1hrMetricObservationTime;
	}

	public void setPrecip1hrMetricObservationTime(ZonedDateTime precip1hrMetricObservationTime) {
		this.precip1hrMetricObservationTime = precip1hrMetricObservationTime;
	}

	public Double getPrecipTodayMetric() {
		return precipTodayMetric;
	}

	public void setPrecipTodayMetric(Double precipTodayMetric) {
		this.precipTodayMetric = precipTodayMetric;
	}

	public ZonedDateTime getPrecipTodayMetricObservationTime() {
		return precipTodayMetricObservationTime;
	}

	public void setPrecipTodayMetricObservationTime(ZonedDateTime precipTodayMetricObservationTime) {
		this.precipTodayMetricObservationTime = precipTodayMetricObservationTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
