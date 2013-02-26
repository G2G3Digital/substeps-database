package com.technophobia.substeps.database.runner;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

public class DatabaseExecutionContext {

	//The results of a query where the key is a column and the value is its list of values
	private Map<String, List<String>> queryResult;

	public Set<String> getColumns() {

		Assert.assertNotNull("Attempted to get query results, but they have not yet been set", queryResult);
		
		return queryResult.keySet();
	}
	
	public List<String> getResultsForColumn(final String columnName) {
		
		Assert.assertNotNull("Attempted to get query results, but they have not yet been set", queryResult);
		
		return queryResult.get(columnName);
	}
	
	public void setQueryResult(final ResultSet rs) throws SQLException {
		
		createResultMap(rs);

		while(rs.next()) {

			for(String columnName : queryResult.keySet()) {
				
				queryResult.get(columnName).add(rs.getString(columnName));
			}
		}
	}

	private void createResultMap(final ResultSet rs) throws SQLException {
		
		final ResultSetMetaData metaData = rs.getMetaData();
		final int numberOfColumns = metaData.getColumnCount();
		
		queryResult = new HashMap<String, List<String>>();
		for(int i = 1; i <= numberOfColumns; i++) {
			
			final String columnName = metaData.getColumnName(i);
			queryResult.put(columnName, new ArrayList<String>());
		}
	}
	
}
