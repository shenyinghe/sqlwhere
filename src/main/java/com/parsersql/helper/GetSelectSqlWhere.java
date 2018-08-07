package com.parsersql.helper;

import java.io.StringReader;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class GetSelectSqlWhere {
	
	public String getWhere(String selectSql){
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		Select select=null;
		try {
			select = (Select) parserManager.parse(new StringReader(selectSql));
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		PlainSelect plain = (PlainSelect) select.getSelectBody();  
		Expression where_expression = plain.getWhere();
		String str="";
		if(where_expression!=null){
			str = where_expression.toString();
		}
		System.out.println("打印where:"+str);
		return str;  
	}
	
	
}
