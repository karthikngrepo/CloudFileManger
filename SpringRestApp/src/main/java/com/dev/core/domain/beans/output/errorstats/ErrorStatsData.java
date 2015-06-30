package com.dev.core.domain.beans.output.errorstats;

import com.dev.core.domain.enums.ErrorType;

public class ErrorStatsData {
	
	private String _agentName;
	
	private ErrorType _errorType;
	
	private String _createDate;
	
	private long _count;
	
	private double _percentage_change;
	
	private String _trend;
	
	private String _ttrStatus;
	
	private long _latency;
	
	private long _priority;
	
	private double _percentChange;

	public String get_agentName() {
		return _agentName;
	}

	public void set_agentName(String _agentName) {
		this._agentName = _agentName;
	}

	public ErrorType get_errorType() {
		return _errorType;
	}

	public void set_errorType(ErrorType _errorType) {
		this._errorType = _errorType;
	}

	public String get_createDate() {
		return _createDate;
	}

	public void set_createDate(String _createDate) {
		this._createDate = _createDate;
	}

	public long get_count() {
		return _count;
	}

	public void set_count(long _count) {
		this._count = _count;
	}

	public double get_percentage_change() {
		return _percentage_change;
	}

	public void set_percentage_change(double _percentage_change) {
		this._percentage_change = _percentage_change;
	}

	public String get_trend() {
		return _trend;
	}

	public void set_trend(String _trend) {
		this._trend = _trend;
	}

	public String get_ttrStatus() {
		return _ttrStatus;
	}

	public void set_ttrStatus(String _ttrStatus) {
		this._ttrStatus = _ttrStatus;
	}

	public long get_latency() {
		return _latency;
	}

	public void set_latency(long _latency) {
		this._latency = _latency;
	}

	public long get_priority() {
		return _priority;
	}

	public void set_priority(long _priority) {
		this._priority = _priority;
	}

	public double get_percentChange() {
		return _percentChange;
	}

	public void set_percentChange(double _percentChange) {
		this._percentChange = _percentChange;
	}
}
