package com.dev.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.core.domain.beans.input.CobrandDetailsRequest;
import com.dev.core.domain.beans.input.ServicePerfRequest;
import com.dev.core.domain.beans.input.errorstats.ErrorStatsRequest;
import com.dev.core.domain.beans.input.servicedetails.ServiceDetailsRequest;
import com.dev.core.domain.beans.output.CobrandDetailsResponse;
import com.dev.core.domain.beans.output.ServiceNames;
import com.dev.core.domain.beans.output.errorstats.ErrorStats24HrsData;
import com.dev.core.domain.beans.output.errorstats.ErrorStatsResponse;
import com.dev.core.domain.beans.output.errorstats.FinalErrorStats24HrResponse;
import com.dev.core.domain.beans.output.errorstats.FinalErrorStatsResponse;
import com.dev.core.domain.beans.output.servicedetails.ServiceDetailsResponse;
import com.dev.core.domain.beans.output.servicedetails.ServicePerfResponse;
import com.dev.core.domain.dao.ErrorStatsDAO;
import com.dev.core.domain.dao.ServiceStatsDAO;
import com.dev.core.domain.enums.ErrorType;
import com.dev.core.domain.exception.CoreException;
import com.dev.core.domain.exception.InvalidArgumentException;
import com.dev.core.domain.helpers.ErrorStatsRequestValidator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value= "/StatsManager")
public class StatsManager {
	
	@Autowired
	private ErrorStatsDAO errorStatsDAO;
	
	@Autowired
	private ServiceStatsDAO serviceStatsDAO;
	
	@RequestMapping(value="/getErrorStats", method=RequestMethod.GET)
	public FinalErrorStatsResponse getErrorStats(@RequestParam(value="requestJson", required=true) String requestJson) {
    	
		ObjectMapper mapper = new ObjectMapper();
		FinalErrorStatsResponse responses = new FinalErrorStatsResponse();
		responses.setName("analytics");
		
		try {
			ErrorStatsRequest request = mapper.readValue(requestJson, ErrorStatsRequest.class);
			ErrorStatsRequestValidator.validateErrorStatRequest(request);
			
			List<ErrorStatsResponse> errorStatsResponseList = new ArrayList<ErrorStatsResponse>();
			
			if(request.get_errorType() == ErrorType.AGENT_ERRORS) {
				errorStatsResponseList = errorStatsDAO.getAllAgentErrorStats(request);
			} else if(request.get_errorType() == ErrorType.SITE_ERRORS) {
				errorStatsResponseList = errorStatsDAO.getAllSiteErrorStats(request);
			} else if(request.get_errorType() == ErrorType.UAR_ERRORS) {
				errorStatsResponseList = errorStatsDAO.getAllUARErrorStats(request);
			} else if(request.get_errorType() == ErrorType.INFRA_ERRORS) {
				errorStatsResponseList = errorStatsDAO.getAllInfraErrorStats(request);
			}
			responses.setChildren(errorStatsResponseList.toArray(new ErrorStatsResponse[errorStatsResponseList.size()]));
			
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new InvalidArgumentException("Exception in parsing the JSON input passed to method!!!");
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new InvalidArgumentException("The JSON input parameter passed to the method is in incorrect format");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CoreException("IOException Exception while mapping the JSON to JAVA object !!!");
		}
		
		return responses;
	}
	
	@RequestMapping(value="/getServiceStats", method=RequestMethod.GET)
	public ServicePerfResponse[] getServicePerformanceStats(@RequestParam(value="requestJson", required=true) String requestJson) {
		ObjectMapper mapper = new ObjectMapper();
		ServicePerfResponse[] responses = null;
	
		try {
			ServicePerfRequest request = mapper.readValue(requestJson, ServicePerfRequest.class);
			
			List<ServicePerfResponse> serviceList = serviceStatsDAO.getAllServicesStats(request);
			responses = serviceList.toArray(new ServicePerfResponse[serviceList.size()]);
			
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new InvalidArgumentException("Exception in parsing the JSON input passed to method!!!");
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new InvalidArgumentException("The JSON input parameter passed to the method is in incorrect format");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CoreException("IOException Exception while mapping the JSON to JAVA object !!!");
		}
		
		return responses;
	}
	
	@RequestMapping(value="/get24HourErrorStats", method=RequestMethod.GET)
	public FinalErrorStats24HrResponse get24HourErrorStats(@RequestParam(value="requestJson", required=true) String requestJson) {
    	
		ObjectMapper mapper = new ObjectMapper();
		
		FinalErrorStats24HrResponse finalErrorStats24HrResponse = new FinalErrorStats24HrResponse();
		
		try {
			ErrorStatsRequest request = mapper.readValue(requestJson, ErrorStatsRequest.class);
			
			List<ErrorStats24HrsData> errorStatsResponseList = new ArrayList<ErrorStats24HrsData>();
			
			errorStatsResponseList = errorStatsDAO.get24HrsErrorStats(request);

			finalErrorStats24HrResponse.setResults(errorStatsResponseList.toArray(new ErrorStats24HrsData[errorStatsResponseList.size()]));
			
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new InvalidArgumentException("Exception in parsing the JSON input passed to method!!!");
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new InvalidArgumentException("The JSON input parameter passed to the method is in incorrect format");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CoreException("IOException Exception while mapping the JSON to JAVA object !!!");
		}
		
		return finalErrorStats24HrResponse;
	}
	
	@RequestMapping(value="/getCobrandDetails", method=RequestMethod.GET)
	public CobrandDetailsResponse[] getCobrandDetails(@RequestParam(value="requestJson", required=true) String requestJson) {
    	
		ObjectMapper mapper = new ObjectMapper();
		
		CobrandDetailsResponse[] cobrandDetailsResponse = null;
		
		try {
			CobrandDetailsRequest request = mapper.readValue(requestJson, CobrandDetailsRequest.class);
			
			cobrandDetailsResponse = serviceStatsDAO.getCobrandDetails(request);

		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new InvalidArgumentException("Exception in parsing the JSON input passed to method!!!");
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new InvalidArgumentException("The JSON input parameter passed to the method is in incorrect format");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CoreException("IOException Exception while mapping the JSON to JAVA object !!!");
		}
		
		return cobrandDetailsResponse;
	}
	
	@RequestMapping(value="/getDistinctServiceNames", method=RequestMethod.GET)
	public ServiceNames[] getDistinctServiceNames() {
		
		return serviceStatsDAO.getDistinctServiceNames();
	}
	
	@RequestMapping(value="/getServiceDetails", method=RequestMethod.GET)
	public ServiceDetailsResponse getServiceDetails(@RequestParam(value="requestJson", required=true) String requestJson) {
    	
		ObjectMapper mapper = new ObjectMapper();
		
		ServiceDetailsResponse serviceDetailsResponse = null;
		
		try {
			ServiceDetailsRequest request = mapper.readValue(requestJson, ServiceDetailsRequest.class);
			
			serviceDetailsResponse = serviceStatsDAO.getServiceDetails(request);

		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new InvalidArgumentException("Exception in parsing the JSON input passed to method!!!");
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new InvalidArgumentException("The JSON input parameter passed to the method is in incorrect format");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CoreException("IOException Exception while mapping the JSON to JAVA object !!!");
		}
		
		return serviceDetailsResponse;
	}
}