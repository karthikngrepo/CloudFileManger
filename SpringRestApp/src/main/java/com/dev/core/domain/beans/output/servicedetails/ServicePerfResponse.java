package com.dev.core.domain.beans.output.servicedetails;


public class ServicePerfResponse {
	private String _serviceName;
	
	private long _cobrandId;

	private String _createdDate;
	
	private long _hour;
	
	private long _totalCalls;
	
	private double _totalTime;
	
	private double _avgTime;
	
	private double _avgSize;
	
	private double _maxSize;
	
	private double _minSize;
	
	private double _minTime;
	
	private double _maxTime;
	
	private String _stateCode;
	
	public String get_serviceName() {
		return _serviceName;
	}

	public void set_serviceName(String _serviceName) {
		this._serviceName = _serviceName;
	}

	public long get_cobrandId() {
		return _cobrandId;
	}

	public void set_cobrandId(long _cobrandId) {
		this._cobrandId = _cobrandId;
	}

	public String get_createdDate() {
		return _createdDate;
	}

	public void set_createdDate(String _createdDate) {
		this._createdDate = _createdDate;
	}

	public long get_hour() {
		return _hour;
	}

	public void set_hour(long _hour) {
		this._hour = _hour;
	}

	public long get_totalCalls() {
		return _totalCalls;
	}

	public void set_totalCalls(long _totalCalls) {
		this._totalCalls = _totalCalls;
	}

	public double get_totalTime() {
		return _totalTime;
	}

	public void set_totalTime(double _totalTime) {
		this._totalTime = _totalTime;
	}

	public double get_avgTime() {
		return _avgTime;
	}

	public void set_avgTime(double _avgTime) {
		this._avgTime = _avgTime;
	}

	public double get_avgSize() {
		return _avgSize;
	}

	public void set_avgSize(double _avgSize) {
		this._avgSize = _avgSize;
	}

	public double get_maxSize() {
		return _maxSize;
	}

	public void set_maxSize(double _maxSize) {
		this._maxSize = _maxSize;
	}

	public double get_minSize() {
		return _minSize;
	}

	public void set_minSize(double _minSize) {
		this._minSize = _minSize;
	}

	public double get_minTime() {
		return _minTime;
	}

	public void set_minTime(double _minTime) {
		this._minTime = _minTime;
	}

	public double get_maxTime() {
		return _maxTime;
	}

	public void set_maxTime(double _maxTime) {
		this._maxTime = _maxTime;
	}

	public String get_stateCode() {
		return _stateCode;
	}

	public void set_stateCode(String _stateCode) {
		this._stateCode = _stateCode;
	}
}
