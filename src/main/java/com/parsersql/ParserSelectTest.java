package com.parsersql;

import java.util.List;
import com.parsersql.helper.GetSelectSqlGroupby;
import com.parsersql.helper.GetSelectSqlIsolationUnion;
import com.parsersql.helper.GetSelectSqlItemCol;
import com.parsersql.helper.GetSelectSqlItems;
import com.parsersql.helper.GetSelectSqlJoins;
import com.parsersql.helper.GetSelectSqlTables;
import com.parsersql.helper.GetSelectSqlWhere;
import com.parsersql.helper.ReadSelectSql;
import com.parsersql.viewhandle.ExtractInfoFromView;

public class ParserSelectTest {
	public static void main(String[] args) {
		//step1:读取sql，去除单行、多行注释
		ReadSelectSql readSelectSql=new ReadSelectSql();
		String selectSql=readSelectSql.readSelectSql("E:/1.txt");
		
		//step1.1:将“union/union all”语句拆成多个语句,返回List<String>
		GetSelectSqlIsolationUnion getSelectSqlIsolationUnion=new GetSelectSqlIsolationUnion();
		List<String> sqls=getSelectSqlIsolationUnion.isolationUnion(selectSql);
		
		for(int k=0;k<sqls.size();k++){
			selectSql=sqls.get(k);
			System.out.println("去除union后的SQL："+selectSql);

			
			//step3:解析出select tables
			GetSelectSqlTables getSelectSqlTables=new GetSelectSqlTables();
			List<String> tables=getSelectSqlTables.getTables(selectSql);
			for(int i=0;i<tables.size();i++){
				String str=tables.get(i);
				System.out.println("打印tables:"+str);
			}
			
			//step2:解析出select items
			GetSelectSqlItems getSelectSqlItems=new GetSelectSqlItems();
			List<String> items=getSelectSqlItems.getItems(selectSql);
			
			
			GetSelectSqlItemCol getSelectSqlItemCol=new GetSelectSqlItemCol();
			//step2.1:解析item表达式，提取表名（表别名）.字段名
			for(int i=0;i<items.size();i++){
				String itemExpression=items.get(i)==null?"":items.get(i);
				System.out.println("打印item表达式("+(i+1)+")："+itemExpression);
				
				String expressResult=getSelectSqlItemCol.cleanColExpress(itemExpression, selectSql, tables);
				System.out.println("打印item表达式结果("+(i+1)+")："+expressResult);
				
				//step2.1.1 从函数中提取    表.字段
				
				//step2.1.2从case when表达式中提取 表.字段
				
				//step2.1.3从子查询表达式中提取 表.字段
				
				//step2.1.4清洗 表名.字段名
				
			}
			
			

			
			//step4:解析出select joins
			GetSelectSqlJoins getSelectSqlJoins=new GetSelectSqlJoins();
			List<String> joins=getSelectSqlJoins.getJoins(selectSql);
			
			//step5:解析出select where
			GetSelectSqlWhere getSelectSqlWhere=new GetSelectSqlWhere();
			String whereStr=getSelectSqlWhere.getWhere(selectSql);
			
			//step6:解析出group by
			GetSelectSqlGroupby getSelectSqlGroupby=new GetSelectSqlGroupby();
			List<String> groupbys=getSelectSqlGroupby.getGroupby(selectSql);
			
			
		}
	}
}
