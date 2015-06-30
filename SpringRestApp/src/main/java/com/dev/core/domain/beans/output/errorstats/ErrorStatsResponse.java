package com.dev.core.domain.beans.output.errorstats;


public class ErrorStatsResponse {
	private String _agentName;

	private ErrorStatsData[] children;
	
	public String get_agentName() {
		return _agentName;
	}

	public void set_agentName(String _agentName) {
		this._agentName = _agentName;
	}

	public ErrorStatsData[] getChildren() {
		return children;
	}

	public void setChildren(ErrorStatsData[] children) {
		this.children = children;
	}
}