package com.parsersql.helper;

import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class GetSelectSqlTables {
	
	public List<String> getTables(String selectSql){
		Statement statement=null;
		try {
			statement = (Statement) CCJSqlParserUtil.parse(selectSql);
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		Select selectStatement = (Select) statement;  
		TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();  
		List<String> tableList = tablesNamesFinder.getTableList(selectStatement);  
		return tableList;
	}
	
	
}
