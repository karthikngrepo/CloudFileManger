package com.dev.core.domain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dev.core.domain.beans.input.errorstats.ErrorStatsRequest;
import com.dev.core.domain.beans.output.errorstats.ErrorStats24HrsData;
import com.dev.core.domain.beans.output.errorstats.ErrorStatsData;
import com.dev.core.domain.beans.output.errorstats.ErrorStatsResponse;
import com.dev.core.domain.constant.CommonConstants;
import com.dev.core.domain.constant.DAOConstants;
import com.dev.core.domain.constant.Queries;
import com.dev.core.domain.constant.dbtables.ClassName;
import com.dev.core.domain.constant.dbtables.LatencyStats;
import com.dev.core.domain.constant.dbtables.RefreshStats;
import com.dev.core.domain.constant.dbtables.TTR;
import com.dev.core.domain.enums.ErrorType;
import com.dev.core.domain.enums.OrderType;
import com.dev.core.domain.util.CommonUtilities;

@Repository
@PropertySource("classpath:Queries.properties")
public class ErrorStatsDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
    Environment env;
	
	private SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.DATE_FORMAT_MM_DD_YYYY);
	
	public List<ErrorStatsResponse> getAllAgentErrorStats(ErrorStatsRequest request) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		
		String sqlQuery = env.getProperty(Queries.ALL_AGENT_ERROR_STATS_BY_COUNT_ORDER);
		if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PRIORITY)) {
			sqlQuery = env.getProperty(Queries.ALL_AGENT_ERROR_PRIORITY_STATS);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PERCENT)) {
			sqlQuery = env.getProperty(Queries.ALL_AGENT_ERROR_STATS_BY_PERCENT_ORDER);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PERCENT_CHANGE)) {
			sqlQuery = env.getProperty(Queries.ALL_AGENT_ERROR_PERCENT_CHANGE_STATS);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_classNames() != null) {
			paramMap.addValue(DAOConstants.CLASS_NAME, CommonUtilities.convertArrayToCSV(request.get_classNames()), OracleTypes.VARCHAR);
			paramMap.addValue(DAOConstants.NUM_DAYS, request.get_numberOfDaysStats(), OracleTypes.INTEGER);
			sqlQuery = env.getProperty(Queries.SINGLE_AGENT_ERROR_STATS);
		} else {
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
		}
		
		List<ErrorStatsResponse> errorStatsList = jdbcTemplate.query(sqlQuery, paramMap, new RowMapper<ErrorStatsResponse>() {

			@Override
			public ErrorStatsResponse mapRow(ResultSet rs, int rownum) throws SQLException {
				ErrorStatsResponse errorStatsResponse = new ErrorStatsResponse();
				String class_name = rs.getString(ClassName.CLASS_NAME.toString());
				errorStatsResponse.set_agentName(class_name);
				
				ErrorStatsData[] statsResponseArray = new ErrorStatsData[1];
				ErrorStatsData statsResponse = new ErrorStatsData();
				statsResponseArray[0] = statsResponse;
				statsResponse.set_agentName(class_name);
				statsResponse.set_count(rs.getLong(RefreshStats.AGENT_ERROR_COUNT.toString()));
				statsResponse.set_createDate(sdf.format(rs.getDate(RefreshStats.CREATED_DATE.toString())));
				statsResponse.set_errorType(ErrorType.AGENT_ERRORS);
				statsResponse.set_percentage_change(rs.getLong(RefreshStats.AGENT_ERROR_PERCENT.toString()));
				statsResponse.set_trend(rs.getString(RefreshStats.AGENT_ERROR_TREND.toString()));
				statsResponse.set_latency(rs.getLong(LatencyStats.LATENCY.toString()));
				statsResponse.set_ttrStatus(rs.getString(TTR.TTR_STATUS.toString()));
				statsResponse.set_priority(rs.getLong(ClassName.PRIORITY.toString()));
				statsResponse.set_percentChange(rs.getDouble(RefreshStats.AGENT_ERROR_PERCENT_INCR.toString()));
				
				errorStatsResponse.setChildren(statsResponseArray);
				
				return errorStatsResponse;
			}
		});
		
		return errorStatsList;
	}
	
	public List<ErrorStatsResponse> getAllUARErrorStats(ErrorStatsRequest request) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		
		String sqlQuery = env.getProperty(Queries.ALL_UAR_ERROR_STATS_BY_COUNT_ORDER);
		if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PRIORITY)) {
			sqlQuery = env.getProperty(Queries.ALL_UAR_ERROR_PRIORITY_STATS);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PERCENT)) {
			sqlQuery = env.getProperty(Queries.ALL_UAR_ERROR_STATS_BY_PERCENT_ORDER);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PERCENT_CHANGE)) {
			sqlQuery = env.getProperty(Queries.ALL_SITE_ERROR_PERCENT_CHANGE_STATS);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_classNames() != null) {
			paramMap.addValue(DAOConstants.CLASS_NAME, CommonUtilities.convertArrayToCSV(request.get_classNames()), OracleTypes.VARCHAR);
			paramMap.addValue(DAOConstants.NUM_DAYS, request.get_numberOfDaysStats(), OracleTypes.INTEGER);
			sqlQuery = env.getProperty(Queries.SINGLE_UAR_ERROR_STATS);
		} else {
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
		}
		
		List<ErrorStatsResponse> errorStatsList = jdbcTemplate.query(sqlQuery, paramMap, new RowMapper<ErrorStatsResponse>() {

			@Override
			public ErrorStatsResponse mapRow(ResultSet rs, int rownum) throws SQLException {
				ErrorStatsResponse errorStatsResponse = new ErrorStatsResponse();
				String class_name = rs.getString(ClassName.CLASS_NAME.toString());
				errorStatsResponse.set_agentName(class_name);
				
				ErrorStatsData[] statsResponseArray = new ErrorStatsData[1];
				ErrorStatsData statsResponse = new ErrorStatsData();
				statsResponseArray[0] = statsResponse;
				statsResponse.set_agentName(class_name);
				statsResponse.set_count(rs.getLong(RefreshStats.UAR_ERROR_COUNT.toString()));
				statsResponse.set_createDate(sdf.format(rs.getDate(RefreshStats.CREATED_DATE.toString())));
				statsResponse.set_errorType(ErrorType.UAR_ERRORS);
				statsResponse.set_percentage_change(rs.getLong(RefreshStats.UAR_ERROR_PERCENT.toString()));
				statsResponse.set_trend(rs.getString(RefreshStats.UAR_ERROR_TREND.toString()));
				statsResponse.set_latency(rs.getLong(LatencyStats.LATENCY.toString()));
				statsResponse.set_ttrStatus(rs.getString(TTR.TTR_STATUS.toString()));
				statsResponse.set_priority(rs.getLong(ClassName.PRIORITY.toString()));
				statsResponse.set_percentChange(rs.getDouble(RefreshStats.AGENT_ERROR_PERCENT_INCR.toString()));
				
				errorStatsResponse.setChildren(statsResponseArray);
				
				return errorStatsResponse;
			}
		});
		
		return errorStatsList;
	}
	
	public List<ErrorStatsResponse> getAllSiteErrorStats(ErrorStatsRequest request) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		
		String sqlQuery = env.getProperty(Queries.ALL_SITE_ERROR_STATS_BY_COUNT_ORDER);
		if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PRIORITY)) {
			sqlQuery = env.getProperty(Queries.ALL_SITE_ERROR_PRIORITY_STATS);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PERCENT)) {
			sqlQuery = env.getProperty(Queries.ALL_SITE_ERROR_STATS_BY_PERCENT_ORDER);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PERCENT_CHANGE)) {
			sqlQuery = env.getProperty(Queries.ALL_UAR_ERROR_PERCENT_CHANGE_STATS);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_classNames() != null) {
			paramMap.addValue(DAOConstants.CLASS_NAME, CommonUtilities.convertArrayToCSV(request.get_classNames()), OracleTypes.VARCHAR);
			paramMap.addValue(DAOConstants.NUM_DAYS, request.get_numberOfDaysStats(), OracleTypes.INTEGER);
			sqlQuery = env.getProperty(Queries.SINGLE_SITE_ERROR_STATS);
			
		} else {
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
		}
		
		List<ErrorStatsResponse> errorStatsList = jdbcTemplate.query(sqlQuery, paramMap, new RowMapper<ErrorStatsResponse>() {

			@Override
			public ErrorStatsResponse mapRow(ResultSet rs, int rownum) throws SQLException {
				ErrorStatsResponse errorStatsResponse = new ErrorStatsResponse();
				String class_name = rs.getString(ClassName.CLASS_NAME.toString());
				errorStatsResponse.set_agentName(class_name);
				
				ErrorStatsData[] statsResponseArray = new ErrorStatsData[1];
				ErrorStatsData statsResponse = new ErrorStatsData();
				statsResponseArray[0] = statsResponse;
				statsResponse.set_agentName(class_name);
				statsResponse.set_count(rs.getLong(RefreshStats.SITE_ERROR_COUNT.toString()));
				statsResponse.set_createDate(sdf.format(rs.getDate(RefreshStats.CREATED_DATE.toString())));
				statsResponse.set_errorType(ErrorType.SITE_ERRORS);
				statsResponse.set_percentage_change(rs.getLong(RefreshStats.SITE_ERROR_PERCENT.toString()));
				statsResponse.set_trend(rs.getString(RefreshStats.SITE_ERROR_TREND.toString()));
				statsResponse.set_latency(rs.getLong(LatencyStats.LATENCY.toString()));
				statsResponse.set_ttrStatus(rs.getString(TTR.TTR_STATUS.toString()));
				statsResponse.set_priority(rs.getLong(ClassName.PRIORITY.toString()));
				statsResponse.set_percentChange(rs.getDouble(RefreshStats.AGENT_ERROR_PERCENT_INCR.toString()));
				
				errorStatsResponse.setChildren(statsResponseArray);
				
				return errorStatsResponse;
			}
		});
		
		return errorStatsList;
	}
	
	public List<ErrorStatsResponse> getAllInfraErrorStats(ErrorStatsRequest request) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		
		String sqlQuery = env.getProperty(Queries.ALL_INFRA_ERROR_STATS_BY_COUNT_ORDER);
		if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PRIORITY)) {
			sqlQuery = env.getProperty(Queries.ALL_INFRA_ERROR_PRIORITY_STATS);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PERCENT)) {
			sqlQuery = env.getProperty(Queries.ALL_INFRA_ERROR_STATS_BY_PERCENT_ORDER);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_orderType()!= null && request.get_orderType().equals(OrderType.ORDER_BY_PERCENT_CHANGE)) {
			sqlQuery = env.getProperty(Queries.ALL_INFRA_ERROR_PERCENT_CHANGE_STATS);
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
			
		} else if(request.get_classNames() != null) {
			paramMap.addValue(DAOConstants.CLASS_NAME, CommonUtilities.convertArrayToCSV(request.get_classNames()), OracleTypes.VARCHAR);
			paramMap.addValue(DAOConstants.NUM_DAYS, request.get_numberOfDaysStats(), OracleTypes.INTEGER);
			sqlQuery = env.getProperty(Queries.SINGLE_INFRA_ERROR_STATS);
		} else {
			paramMap.addValue(DAOConstants.NUM_DAYS, 1, OracleTypes.INTEGER);
		}
		
		List<ErrorStatsResponse> errorStatsList = jdbcTemplate.query(sqlQuery, paramMap, new RowMapper<ErrorStatsResponse>() {

			@Override
			public ErrorStatsResponse mapRow(ResultSet rs, int rownum) throws SQLException {
				ErrorStatsResponse errorStatsResponse = new ErrorStatsResponse();
				String class_name = rs.getString(ClassName.CLASS_NAME.toString());
				errorStatsResponse.set_agentName(class_name);
				
				ErrorStatsData[] statsResponseArray = new ErrorStatsData[1];
				ErrorStatsData statsResponse = new ErrorStatsData();
				statsResponseArray[0] = statsResponse;
				statsResponse.set_agentName(class_name);
				statsResponse.set_count(rs.getLong(RefreshStats.INFRA_ERROR_COUNT.toString()));
				statsResponse.set_createDate(sdf.format(rs.getDate(RefreshStats.CREATED_DATE.toString())));
				statsResponse.set_errorType(ErrorType.INFRA_ERRORS);
				statsResponse.set_percentage_change(rs.getLong(RefreshStats.INFRA_ERROR_PERCENT.toString()));
				statsResponse.set_trend(rs.getString(RefreshStats.INFRA_ERROR_TREND.toString()));
				statsResponse.set_latency(rs.getLong(LatencyStats.LATENCY.toString()));
				statsResponse.set_ttrStatus(rs.getString(TTR.TTR_STATUS.toString()));
				statsResponse.set_priority(rs.getLong(ClassName.PRIORITY.toString()));
				statsResponse.set_percentChange(rs.getDouble(RefreshStats.AGENT_ERROR_PERCENT_INCR.toString()));
				
				errorStatsResponse.setChildren(statsResponseArray);
				
				return errorStatsResponse;
			}
		});
		
		return errorStatsList;
	}

	public List<ErrorStats24HrsData> get24HrsErrorStats(ErrorStatsRequest request) {
		
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue(DAOConstants.CLASS_NAME, request.get_classNames()[0], OracleTypes.VARCHAR);
		
		String sqlQuery = env.getProperty(Queries.SINGLE_CLASS_24_HRS_STATS);
		
		List<ErrorStats24HrsData> errorStatsList = jdbcTemplate.query(sqlQuery, paramMap, new RowMapper<ErrorStats24HrsData>() {

			@Override
			public ErrorStats24HrsData mapRow(ResultSet rs, int rownum) throws SQLException {
				ErrorStats24HrsData statsResponse = new ErrorStats24HrsData();
				
				statsResponse.set_agentName(rs.getString(ClassName.CLASS_NAME.toString()));
				
				statsResponse.set_totalRefreshCount(rs.getLong(RefreshStats.TOTAL_REFRESH_COUNT.toString()));
				statsResponse.set_successPercent(rs.getDouble(RefreshStats.SUCCESS_PERCENT.toString()));
				
				statsResponse.set_agent_error_count(rs.getInt(RefreshStats.AGENT_ERROR_COUNT.toString()));
				statsResponse.set_agent_error_percent(rs.getLong(RefreshStats.AGENT_ERROR_PERCENT.toString()));
				statsResponse.set_agent_error_percent_incr(rs.getDouble(RefreshStats.AGENT_ERROR_PERCENT_INCR.toString()));
				statsResponse.set_agent_error_trend(rs.getString(RefreshStats.AGENT_ERROR_TREND.toString()));
				
				statsResponse.set_site_error_count(rs.getInt(RefreshStats.SITE_ERROR_COUNT.toString()));
				statsResponse.set_site_error_percent(rs.getLong(RefreshStats.SITE_ERROR_PERCENT.toString()));
				statsResponse.set_site_error_percent_incr(rs.getDouble(RefreshStats.SITE_ERROR_PERCENT_INCR.toString()));
				statsResponse.set_site_error_trend(rs.getString(RefreshStats.SITE_ERROR_TREND.toString()));
				
				statsResponse.set_uar_error_count(rs.getInt(RefreshStats.UAR_ERROR_COUNT.toString()));
				statsResponse.set_uar_error_percent(rs.getLong(RefreshStats.UAR_ERROR_PERCENT.toString()));
				statsResponse.set_uar_error_percent_incr(rs.getDouble(RefreshStats.UAR_ERROR_PERCENT_INCR.toString()));
				statsResponse.set_uar_error_trend(rs.getString(RefreshStats.UAR_ERROR_TREND.toString()));
				
				statsResponse.set_infra_error_count(rs.getInt(RefreshStats.INFRA_ERROR_COUNT.toString()));
				statsResponse.set_infra_error_percent(rs.getLong(RefreshStats.INFRA_ERROR_PERCENT.toString()));
				statsResponse.set_infra_error_percent_incr(rs.getDouble(RefreshStats.INFRA_ERROR_PERCENT_INCR.toString()));
				statsResponse.set_infra_error_trend(rs.getString(RefreshStats.INFRA_ERROR_TREND.toString()));
				
				statsResponse.set_createDate(sdf.format(rs.getDate(RefreshStats.CREATED_DATE.toString())));
				statsResponse.set_latency(rs.getLong(LatencyStats.LATENCY.toString()));
				statsResponse.set_ttrStatus(rs.getString(TTR.TTR_STATUS.toString()));
				statsResponse.set_priority(rs.getLong(ClassName.PRIORITY.toString()));
				statsResponse.set_percentChange(rs.getDouble(RefreshStats.AGENT_ERROR_PERCENT_INCR.toString()));
				statsResponse.set_hour(rs.getInt(RefreshStats.HOUR.toString()));
				
				
				return statsResponse;
			}
		});
		
		return errorStatsList;
	}
}
