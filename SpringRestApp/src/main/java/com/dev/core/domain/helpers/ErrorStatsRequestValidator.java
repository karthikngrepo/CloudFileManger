package com.dev.core.domain.helpers;

import com.dev.core.domain.beans.input.errorstats.ErrorStatsRequest;
import com.dev.core.domain.constant.DAOConstants;
import com.dev.core.domain.exception.InvalidArgumentException;

public class ErrorStatsRequestValidator {

	/**
	 * This method is used to validate request-JSON sent while requesting error stats.
	 * @param request
	 */
	public static void validateErrorStatRequest(ErrorStatsRequest request) {
		
		if(request.get_numberOfDaysStats() > DAOConstants.MAX_NUM_OF_DAYS_STATS_PULLED) {
			throw new InvalidArgumentException("Please change the \"_numberOfDaysStats\" to 7 days or less !!!");
		}
	}
}
