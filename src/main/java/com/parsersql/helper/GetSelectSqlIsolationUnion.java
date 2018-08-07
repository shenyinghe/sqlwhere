package com.parsersql.helper;

import java.util.ArrayList;
import java.util.List;

public class GetSelectSqlIsolationUnion {
	
	public List<String> isolationUnion(String selectSql){
		List<String> list=new ArrayList<String>();
		int indexofCount=0;
		//是否存在union,并且union前后字符是否是空格或tab
		while(true){
			boolean isUnion=selectSql.toUpperCase().indexOf("UNION",indexofCount)!=-1
					&& selectSql.length()>selectSql.toUpperCase().indexOf("UNION",indexofCount)+5
					&& (selectSql.substring(selectSql.toUpperCase().indexOf("UNION",indexofCount)-1,selectSql.toUpperCase().indexOf("UNION",indexofCount)).equals(" ")
							|| selectSql.substring(selectSql.toUpperCase().indexOf("UNION",indexofCount)-1,selectSql.toUpperCase().indexOf("UNION",indexofCount)).equals("	"))
					&& (selectSql.substring(selectSql.toUpperCase().indexOf("UNION",indexofCount)+5,selectSql.toUpperCase().indexOf("UNION",indexofCount)+6).equals(" ")
							|| selectSql.substring(selectSql.toUpperCase().indexOf("UNION",indexofCount)+5,selectSql.toUpperCase().indexOf("UNION",indexofCount)+6).equals("	"));
			if(isUnion==true){
				//截断
				String tmpSql=selectSql.substring(0,selectSql.toUpperCase().indexOf("UNION",indexofCount));
				list.add(tmpSql);
				
				selectSql=selectSql.substring(selectSql.toUpperCase().indexOf("UNION",indexofCount)+5).trim();
				String selectSqlUpper=selectSql.toUpperCase().trim();
				if(selectSqlUpper.startsWith("ALL ") || selectSqlUpper.startsWith("ALL	")){
					selectSql=selectSql.substring(4);
				}
				
				continue;
			}else if(selectSql.toUpperCase().indexOf("UNION")!=-1){
				indexofCount=selectSql.toUpperCase().indexOf("UNION")+5;
				continue;
			}else if(selectSql.toUpperCase().indexOf("UNION")==-1){
				list.add(selectSql);
				break;
			}
		}
		return list;
	}
	
	
}
