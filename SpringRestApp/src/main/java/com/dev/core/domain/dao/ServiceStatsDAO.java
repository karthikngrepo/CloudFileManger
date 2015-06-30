package com.dev.core.domain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dev.core.domain.beans.input.CobrandDetailsRequest;
import com.dev.core.domain.beans.input.ServicePerfRequest;
import com.dev.core.domain.beans.input.servicedetails.ServiceDetailsRequest;
import com.dev.core.domain.beans.output.CobrandDetailsResponse;
import com.dev.core.domain.beans.output.ServiceNames;
import com.dev.core.domain.beans.output.servicedetails.ServiceDetailsResponse;
import com.dev.core.domain.beans.output.servicedetails.ServicePerfResponse;
import com.dev.core.domain.constant.CommonConstants;
import com.dev.core.domain.constant.DAOConstants;
import com.dev.core.domain.constant.Queries;
import com.dev.core.domain.constant.dbtables.Cobrand;
import com.dev.core.domain.constant.dbtables.ServiceStats;
import com.dev.core.domain.constant.dbtables.States;
import com.dev.core.domain.util.CommonUtilities;

@Repository
@PropertySource("classpath:Queries.properties")
public class ServiceStatsDAO {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
    Environment env;
	
	public List<ServicePerfResponse> getAllServicesStats(ServicePerfRequest request) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue(DAOConstants.NUM_DAYS, request.get_numOfDaysStats(), OracleTypes.INTEGER);
		String sqlQuery = env.getProperty(Queries.ALL_API_PERF_STATS);
		
		if(request.get_serviceName() != null) {
			sqlQuery = env.getProperty(Queries.SPECIFIC_API_PERF_STATS);
			paramMap.addValue(DAOConstants.SERVICE_NAME, request.get_serviceName(), OracleTypes.VARCHAR);
			
			if (request.get_hour() != null) {
				sqlQuery = env.getProperty(Queries.SPECIFIC_HOUR_SPECIFIC_API_PERF_STATS);
				paramMap.addValue(DAOConstants.HOUR, request.get_hour(), OracleTypes.INTEGER);
			}
		} else if(request.get_hour() != null) {
			sqlQuery = env.getProperty(Queries.SPECIFIC_HOUR_ALL_API_PERF_STATS);
			paramMap.addValue(DAOConstants.HOUR, request.get_hour(), OracleTypes.INTEGER);
		}
		
		List<ServicePerfResponse> apiPerfResponses = jdbcTemplate.query(sqlQuery, paramMap, new RowMapper<ServicePerfResponse>() {

			@Override
			public ServicePerfResponse mapRow(ResultSet rs, int rownum) throws SQLException {
				
				ServicePerfResponse response = new ServicePerfResponse();
				response.set_serviceName(rs.getString(ServiceStats.SERVICE_NAME.toString()));
				response.set_avgSize(rs.getDouble(ServiceStats.AVG_SIZE.toString()));
				response.set_avgTime(rs.getDouble(ServiceStats.AVG_TIME.toString()));
				response.set_cobrandId(rs.getLong(ServiceStats.COBRAND_ID.toString()));
				response.set_createdDate(rs.getString(ServiceStats.CREATED_DATE.toString()));
				response.set_maxSize(rs.getDouble(ServiceStats.MAX_SIZE.toString()));
				response.set_maxTime(rs.getDouble(ServiceStats.MAX_TIME.toString()));
				response.set_minSize(rs.getDouble(ServiceStats.MIN_SIZE.toString()));
				response.set_minTime(rs.getDouble(ServiceStats.MIN_TIME.toString()));
				response.set_totalCalls(rs.getLong(ServiceStats.TOTAL_CALLS.toString()));
				response.set_totalTime(rs.getLong(ServiceStats.TOTAL_TIME.toString()));
				response.set_hour(rs.getLong(ServiceStats.HOUR.toString()));
				response.set_stateCode(rs.getString(States.STATE_SHORT_NAME.toString()));
				
				return response;
			}
		});
		
		return apiPerfResponses;
	}
	
	public CobrandDetailsResponse[] getCobrandDetails(CobrandDetailsRequest request) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		
		String sqlQuery = env.getProperty(Queries.ALL_COBRAND_DETAILS);
		
		if(request.get_cobrandId() > 0L) {
			sqlQuery = env.getProperty(Queries.COBRAND_DETAILS_FOR_COBRAND_ID);
			paramMap.addValue(DAOConstants.COBRAND_ID, request.get_cobrandId(), OracleTypes.INTEGER);
		} else if(request.get_cobrandName() != null) {
			sqlQuery = env.getProperty(Queries.COBRAND_DETAILS_FOR_COBRAND_NAME);
			paramMap.addValue(DAOConstants.COBRAND_NAME, request.get_cobrandName(), OracleTypes.VARCHAR);
		}
		
		List<CobrandDetailsResponse> apiPerfResponses = jdbcTemplate.query(sqlQuery, paramMap, new RowMapper<CobrandDetailsResponse>() {

			@Override
			public CobrandDetailsResponse mapRow(ResultSet rs, int rownum) throws SQLException {
				
				CobrandDetailsResponse response = new CobrandDetailsResponse();
				response.set_cobrandId(rs.getLong(Cobrand.COBRAND_ID.toString()));
				response.set_cobrandName(rs.getString(Cobrand.COBRAND_NAME.toString()));
				response.set_stateId(rs.getLong(Cobrand.STATE_ID.toString()));
				
				return response;
			}
		});
		
		return apiPerfResponses.toArray(new CobrandDetailsResponse[apiPerfResponses.size()]);
	}
	
	public ServiceNames[] getDistinctServiceNames() {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		
		String sqlQuery = env.getProperty(Queries.DISTINCT_SERICE_NAMES);
		
		List<ServiceNames> responses = jdbcTemplate.query(sqlQuery, paramMap, new RowMapper<ServiceNames>() {

			@Override
			public ServiceNames mapRow(ResultSet rs, int rownum) throws SQLException {
				
				ServiceNames response = new ServiceNames();
				response.set_serviceName(rs.getString(ServiceStats.SERVICE_NAME.toString()));
				
				return response;
			}
		});
		
		return responses.toArray(new ServiceNames[responses.size()]);
	}
	
	public ServiceDetailsResponse getServiceDetails(ServiceDetailsRequest request) {
		ServiceDetailsResponse serviceDetailsResponse = new ServiceDetailsResponse(); 
		
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue(DAOConstants.HOUR, CommonUtilities.getRecentPreviousHour(), OracleTypes.INTEGER);
		paramMap.addValue(DAOConstants.COBRAND_ID, request.get_cobrandid(), OracleTypes.INTEGER);
		paramMap.addValue(DAOConstants.SERVICE_NAME, request.get_serviceName(), OracleTypes.VARCHAR);
		
		String sqlQuery = env.getProperty(Queries.SERVICE_DETAILS_BASED_ON_ALL_FILTERS_POS);
		
		if(request.get_cobrandid() > 0L) {
			if(request.get_serviceName() != null) {
				if(request.get_slaComplianceParameter().equals(CommonConstants.MISS_SLA)) {
					sqlQuery = env.getProperty(Queries.SERVICE_DETAILS_BASED_ON_ALL_FILTERS_NEG);
				} else { 
					sqlQuery = env.getProperty(Queries.SERVICE_DETAILS_BASED_ON_ALL_FILTERS_POS);
				}
			} else {
				if(request.get_slaComplianceParameter().equals(CommonConstants.MISS_SLA)) {
					sqlQuery = env.getProperty(Queries.SERVICE_DETAILS_BASED_ON_COBRAND_AND_NEG_SLA);
				} else { 
					sqlQuery = env.getProperty(Queries.SERVICE_DETAILS_BASED_ON_COBRAND_AND_POS_SLA);
				}
			}
		} else {
			if(request.get_serviceName() != null) {
				if(request.get_slaComplianceParameter().equals(CommonConstants.MISS_SLA)) {
					sqlQuery = env.getProperty(Queries.SERVICE_DETAILS_BASED_ON_SERVICE_AND_NEG_SLA);
				} else { 
					sqlQuery = env.getProperty(Queries.SERVICE_DETAILS_BASED_ON_SERVICE_AND_POS_SLA);
				}
			} else {
				if(request.get_slaComplianceParameter().equals(CommonConstants.MISS_SLA)) {
					sqlQuery = env.getProperty(Queries.SERVICE_DETAILS_BASED_ONLY_ON_NEG_SLA);
				} else { 
					sqlQuery = env.getProperty(Queries.SERVICE_DETAILS_BASED_ONLY_ON_POS_SLA);
				}
			}
		}
		
		List<ServicePerfResponse> responses = jdbcTemplate.query(sqlQuery, paramMap, new RowMapper<ServicePerfResponse>() {

			@Override
			public ServicePerfResponse mapRow(ResultSet rs, int rownum) throws SQLException {
				
				ServicePerfResponse response = new ServicePerfResponse();
				response.set_serviceName(rs.getString(ServiceStats.SERVICE_NAME.toString()));
				response.set_avgSize(rs.getDouble(ServiceStats.AVG_SIZE.toString()));
				response.set_avgTime(rs.getDouble(ServiceStats.AVG_TIME.toString()));
				response.set_cobrandId(rs.getLong(ServiceStats.COBRAND_ID.toString()));
				response.set_createdDate(rs.getString(ServiceStats.CREATED_DATE.toString()));
				response.set_maxSize(rs.getDouble(ServiceStats.MAX_SIZE.toString()));
				response.set_maxTime(rs.getDouble(ServiceStats.MAX_TIME.toString()));
				response.set_minSize(rs.getDouble(ServiceStats.MIN_SIZE.toString()));
				response.set_minTime(rs.getDouble(ServiceStats.MIN_TIME.toString()));
				response.set_totalCalls(rs.getLong(ServiceStats.TOTAL_CALLS.toString()));
				response.set_totalTime(rs.getLong(ServiceStats.TOTAL_TIME.toString()));
				response.set_hour(rs.getLong(ServiceStats.HOUR.toString()));
				response.set_stateCode(rs.getString(States.STATE_SHORT_NAME.toString()));
				
				return response;
			}
		});
		
		serviceDetailsResponse.setResults(responses.toArray(new ServicePerfResponse[responses.size()]));
		return serviceDetailsResponse;
	}
}