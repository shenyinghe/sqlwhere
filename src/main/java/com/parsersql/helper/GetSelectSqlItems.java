package com.parsersql.helper;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

public class GetSelectSqlItems {
	
	public List<String> getItems(String selectSql){
		CCJSqlParserManager parserManager = new CCJSqlParserManager();  
		Select select=null;
		try {
			select = (Select) parserManager.parse(new StringReader(selectSql));
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PlainSelect plain = (PlainSelect) select.getSelectBody();
		List<SelectItem> selectitems = plain.getSelectItems();  
		List<String> str_items = new ArrayList<String>();  
		if (selectitems != null) {  
		    for (int i = 0; i < selectitems.size(); i++) {
		    	String itemName=selectitems.get(i).toString();
		    	System.out.println("打印items("+(i+1)+"):"+itemName);//"++"
		        str_items.add(itemName);  
		    }  
		}  
		
		return str_items;
		
		
	}
	
	
}
