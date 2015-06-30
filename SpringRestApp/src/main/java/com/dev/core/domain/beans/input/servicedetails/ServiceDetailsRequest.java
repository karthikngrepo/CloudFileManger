package com.dev.core.domain.beans.input.servicedetails;

public class ServiceDetailsRequest {
	private long _cobrandid;
	
	private String _serviceName;
	
	private String _sizeByFilter;
	
	private String _slaComplianceParameter;

	public long get_cobrandid() {
		return _cobrandid;
	}

	public void set_cobrandid(long _cobrandid) {
		this._cobrandid = _cobrandid;
	}

	public String get_serviceName() {
		return _serviceName;
	}

	public void set_serviceName(String _serviceName) {
		this._serviceName = _serviceName;
	}

	public String get_sizeByFilter() {
		return _sizeByFilter;
	}

	public void set_sizeByFilter(String _sizeByFilter) {
		this._sizeByFilter = _sizeByFilter;
	}

	public String get_slaComplianceParameter() {
		return _slaComplianceParameter;
	}

	public void set_slaComplianceParameter(String _slaComplianceParameter) {
		this._slaComplianceParameter = _slaComplianceParameter;
	}
	
}
