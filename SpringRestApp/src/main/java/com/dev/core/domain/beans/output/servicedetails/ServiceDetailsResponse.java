package com.dev.core.domain.beans.output.servicedetails;

public class ServiceDetailsResponse {
	private Status status = new Status();
	
	private ServicePerfResponse[] results;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ServicePerfResponse[] getResults() {
		return results;
	}

	public void setResults(ServicePerfResponse[] results) {
		this.results = results;
	}

}
