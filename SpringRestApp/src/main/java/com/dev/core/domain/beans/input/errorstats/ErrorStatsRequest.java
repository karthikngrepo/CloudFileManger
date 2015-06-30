package com.dev.core.domain.beans.input.errorstats;

import com.dev.core.domain.enums.ErrorType;
import com.dev.core.domain.enums.OrderType;

public class ErrorStatsRequest {
	
	private ErrorType _errorType;
	
	private int _numberOfDaysStats = 1;
	
	private String[] _classNames;
	
	private OrderType _orderType;
	
	public ErrorType get_errorType() {
		return _errorType;
	}

	public void set_errorType(ErrorType _errorType) {
		this._errorType = _errorType;
	}

	public int get_numberOfDaysStats() {
		return _numberOfDaysStats;
	}

	public void set_numberOfDaysStats(int _numberOfDaysStats) {
		this._numberOfDaysStats = _numberOfDaysStats;
	}

	public String[] get_classNames() {
		return _classNames;
	}

	public void set_classNames(String[] _agentNames) {
		this._classNames = _agentNames;
	}

	public OrderType get_orderType() {
		return _orderType;
	}

	public void set_orderType(OrderType _orderType) {
		this._orderType = _orderType;
	}
}