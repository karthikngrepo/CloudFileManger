package com.dev.core.domain.beans.output.errorstats;

public class FinalErrorStatsResponse {
	private String name;
	
	private ErrorStatsResponse[] children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ErrorStatsResponse[] getChildren() {
		return children;
	}

	public void setChildren(ErrorStatsResponse[] children) {
		this.children = children;
	}

}
