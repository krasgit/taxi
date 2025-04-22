package com.matin.taxi.db.WebSocet;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

public class JdbcIndexedSessionRepository {
	
	public JdbcIndexedSessionRepository() {
		super();
		prepareQueries();
	}



	public JdbcIndexedSessionRepository(JdbcTemplate jdbcTemplate) {
		super();
	//	this.jdbcTemplate=jdbcTemplate;
		prepareQueries();
	}



	//private static final Log logger = LogFactory.getLog(JdbcIndexedSessionRepository.class);
	
	
	
	
	
	
	
	
	int insert(){
	// return jdbcTemplate.update(createSessionQuery   ,    "var1", "var2"	);
		return -1;
	}
	
	int delete(){
//		 return jdbcTemplate.update(createSessionQuery   ,    "var1", "var2"	);
		return -1;
		}

	
	
	public static final String DEFAULT_TABLE_NAME = "SESSION";
	
	private String tableName = DEFAULT_TABLE_NAME;
	
	
	private String createSessionQuery,getSessionQuery,updateSessionQuery;

	private static final String CREATE_SESSION_QUERY = """
			INSERT INTO %TABLE_NAME% (PRIMARY_ID, SESSION_ID, CREATION_TIME, LAST_ACCESS_TIME, MAX_INACTIVE_INTERVAL, EXPIRY_TIME, PRINCIPAL_NAME)
			VALUES (?, ?, ?, ?, ?, ?, ?)
			""";
	
	private static final String GET_SESSION_QUERY = """
			SELECT S.PRIMARY_ID, S.SESSION_ID, S.CREATION_TIME, S.LAST_ACCESS_TIME, S.MAX_INACTIVE_INTERVAL, SA.ATTRIBUTE_NAME, SA.ATTRIBUTE_BYTES
			FROM %TABLE_NAME% S
			WHERE S.SESSION_ID = ?
			""";

	private static final String UPDATE_SESSION_QUERY = """
			UPDATE %TABLE_NAME%
			SET SESSION_ID = ?, LAST_ACCESS_TIME = ?, MAX_INACTIVE_INTERVAL = ?, EXPIRY_TIME = ?, PRINCIPAL_NAME = ?
			WHERE PRIMARY_ID = ?
			""";
	
	
	private String getQuery(String base) {
		return StringUtils.replace(base, "%TABLE_NAME%", this.tableName);
	}
	
	private void prepareQueries() {
		this.createSessionQuery = getQuery(CREATE_SESSION_QUERY);
		this.getSessionQuery = getQuery(GET_SESSION_QUERY);
		this.updateSessionQuery = getQuery(UPDATE_SESSION_QUERY);
		//	this.createSessionAttributeQuery = getQuery(CREATE_SESSION_ATTRIBUTE_QUERY);
	//	this.updateSessionAttributeQuery = getQuery(UPDATE_SESSION_ATTRIBUTE_QUERY);
	//	this.deleteSessionAttributeQuery = getQuery(DELETE_SESSION_ATTRIBUTE_QUERY);
	//	this.deleteSessionQuery = getQuery(DELETE_SESSION_QUERY);
	//	this.listSessionsByPrincipalNameQuery = getQuery(LIST_SESSIONS_BY_PRINCIPAL_NAME_QUERY);
	//	this.deleteSessionsByExpiryTimeQuery = getQuery(DELETE_SESSIONS_BY_EXPIRY_TIME_QUERY);
	}
	
	
}
