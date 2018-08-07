package com.parsersql.helper;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class GetSelectSqlJoins {
	
	public List<String> getJoins(String selectSql){
		Statement statement=null;
		try {
			statement = (Statement) CCJSqlParserUtil.parse(selectSql);
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		Select selectStatement = (Select) statement;  
		PlainSelect plain = (PlainSelect) selectStatement.getSelectBody();  
		List<Join> joinList = plain.getJoins();  
		List<String> tablewithjoin = new ArrayList<String>();  
		if (joinList != null) {  
		    for (int i = 0; i < joinList.size(); i++) {
		    	String join=joinList.get(i).toString();
		    	System.out.println("打印join("+(i+1)+"):"+join);//"++"
		        tablewithjoin.add(join);  
		    }  
		}  
		return tablewithjoin;  
	}
	
}
