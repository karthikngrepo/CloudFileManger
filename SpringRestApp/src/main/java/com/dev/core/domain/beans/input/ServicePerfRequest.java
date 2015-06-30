package com.dev.core.domain.beans.input;

public class ServicePerfRequest {
	private String _serviceName;

	private Long _numOfDaysStats;
	
	private Long _hour;
	
	public String get_serviceName() {
		return _serviceName;
	}

	public void set_serviceName(String _serviceName) {
		this._serviceName = _serviceName;
	}

	public Long get_numOfDaysStats() {
		return _numOfDaysStats;
	}

	public void set_numOfDaysStats(Long _numOfDaysStats) {
		this._numOfDaysStats = _numOfDaysStats;
	}

	public Long get_hour() {
		return _hour;
	}

	public void set_hour(Long _hour) {
		this._hour = _hour;
	}

}
