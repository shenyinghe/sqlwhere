package com.parsersql.helper;

import java.util.List;
import java.util.Map;

public class GetSelectSqlItemCol {
	
	
	/**
	 * 该方法处理最简单的item表达式，如T.STAT_DT,或EDW_MDL.S_03_PROM_DRAFT_INFO.STAT_DT
	 * 提取item表达式中的表名、表别名，验证该表是否在一个子查询中，如果是则提取该子查询完整语句
	 * @param itemExpression
	 * @param selectSql
	 * @param tables
	 * @return
	 */
	public String cleanColExpress(String itemExpression,String selectSql,List<String> tables){
		ReadSelectSql readSelectSql=new ReadSelectSql();
		int pointCount=readSelectSql.countStr(itemExpression, ".");
		//step1:没有“.”  -->  遍历所有表的元数据
		
		
		//step2:有一个“.” --> 则“.”前的字符肯定是表名或表别名
		//首先匹配所有表名，
		if(pointCount>0){
			String tabName=itemExpression.substring(0,itemExpression.lastIndexOf(".", itemExpression.length())).trim().toUpperCase();
			for(int i=0;i<tables.size();i++){
				String tab=tables.get(i)==null?"":tables.get(i).trim().toUpperCase();
				if(tabName.equals(tab)){
					return itemExpression;
				}
			}
			
			//寻找表别名对应的表名
			int indexOfCt=0;
			while(true){
				//递归寻找tabName匹配的字符
				int indexOf=selectSql.indexOf(" "+tabName+" ", indexOfCt);
				//判断是否在子查询中
				if(indexOf!=-1){
					//向前寻找“(select”，向后寻找“)”
//					Map<String, Object> subQueryFlag=readSelectSql.matchSubquery(selectSql, " "+tabName+" ", indexOf);
					String subQuery=readSelectSql.matchSubquery3(selectSql, tabName, indexOf);
					
					System.out.println("表名："+tabName+",打印该表是否在子查询中："+subQuery);//"++"
					
					indexOfCt=indexOf+(" "+tabName+" ").length();
				}else{
					break;
				}
			}
			
		}
		
		
		
		
		
		
		return null;
	}
	
	
	
	
}
