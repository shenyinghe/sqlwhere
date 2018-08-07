package com.parsersql.helper;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class GetSelectSqlGroupby {
	
	public List<String> getGroupby(String selectSql){
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		Select select=null;
		try {
			select = (Select) parserManager.parse(new StringReader(selectSql));
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PlainSelect plain = (PlainSelect) select.getSelectBody();  
		List<Expression> GroupByColumnReferences = plain.getGroupByColumnReferences();  
		List<String> str_groupby = new ArrayList<String>();  
		if (GroupByColumnReferences != null) {  
		    for (int i = 0; i < GroupByColumnReferences.size(); i++) {
		    	String groupby=GroupByColumnReferences.get(i).toString();
		    	System.out.println("打印groupby("+(i+1)+"):"+groupby);//"++"
		        str_groupby.add(groupby);  
		    }  
		}  
		return str_groupby;  
	}
	
}
