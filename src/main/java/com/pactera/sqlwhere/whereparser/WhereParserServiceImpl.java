package com.pactera.sqlwhere.whereparser;

import java.io.StringReader;

import org.springframework.stereotype.Service;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

@Service
public class WhereParserServiceImpl {
	
	//从函数中提取“表名.字段名”或“表别名.字段名”或“字段名”表达式
	//判断是否函数：包含“(” “)”，且括号字符不在单引号内 
	//判断是否case when
	//判断是否常量值：1.以单引号开头，以单引号结尾
	//                2.全数字
	
	//从case when表达式中提取“表名.字段名”或“表别名.字段名”或“字段名”表达式
	//从完整SQL中根据“表别名.字段名”提取“表名.字段名”，遇到子查询需要无限向下递归寻找，找到源头“表名.字段名”
	public void test() {
		//String whereClause = "a=3 AND b=4 AND c=5";
		String whereClause = null;
		//String statement = "SELECT case when a=1 then a else 0 end a,b,c FROM mytable t1 WHERE t1.col = 9 and b=c LIMIT 3, ?";
		String statement="select t.* from P_BLOOD_RELATIONSHIP_ERROR t where case when t.sql_number=0 then '1' else '2' end='2'";
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		Select select=null;
		try {
			select = (Select) parserManager.parse(new StringReader(statement));
			PlainSelect ps = (PlainSelect) select.getSelectBody();
			whereClause=ps.getWhere().toString();
			System.out.println(whereClause);
		} catch (JSQLParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Expression expr=null;
		try {
			expr = CCJSqlParserUtil.parseCondExpression(whereClause);
			expr.accept(new ExpressionVisitorAdapter() {
				public void visit(AndExpression expr) {
				    if (expr.getLeftExpression() instanceof AndExpression) {
				        expr.getLeftExpression().accept(this);
				    } else if ((expr.getLeftExpression() instanceof EqualsTo)){
				        expr.getLeftExpression().accept(this);
				        System.out.println(expr.getLeftExpression());
				    }
				    expr.getRightExpression().accept(this);
				    System.out.println(expr.getRightExpression());
				}
				public void visit(EqualsTo expr) {
				    System.out.println("左:"+expr.getLeftExpression());
				    System.out.println("比较符:"+expr.getStringExpression());
				    System.out.println("右:"+expr.getRightExpression());
				}
			});
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WhereParserServiceImpl impl=new WhereParserServiceImpl();
		impl.test();
	}
	
}
