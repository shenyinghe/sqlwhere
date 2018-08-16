package com.pactera.sqlwhere.whereparser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	
	public boolean isNumber(String expr) {
		expr=expr==null?"":expr;
		String regEx3 = "\\d{1,}\\.{0,1}\\d{0,}";
		Pattern pattern3 = Pattern.compile(regEx3);
		Matcher matcher3 = pattern3.matcher(expr);
		boolean rs3 = matcher3.matches();
		
		return rs3;
	}
	
	public boolean isVarchar(String expr) {
		expr=expr==null?"":expr;
		String regEx4 = "'{1,}(.){0,}'{1,}";
		Pattern pattern4 = Pattern.compile(regEx4);
		Matcher matcher4 = pattern4.matcher(expr);
		boolean rs4 = matcher4.matches();
		return rs4;
	}
	
	
	
	
	/**
	 * 从函数中提取字段
	 * @param colExpressHasFunc
	 * @return
	 */
	public String matchExtractFunction(String colExpressHasFunc){
		String regEx = "(?i)\\w{1,}[(](.){1,}[)]\\s{0,}(as){0,}\\s{0,}\\w{0,}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(colExpressHasFunc);
		boolean rs = matcher.matches();
		String result=null;
		if(rs){
			//如果是函数表达式
			pattern = Pattern.compile("(?i)[(](.){1,}\\s{0,},");
			matcher = pattern.matcher(colExpressHasFunc);
			rs = matcher.find();
			if(rs){
				result=matcher.group();
			}else{
				pattern = Pattern.compile("(?i)[(](.){1,}\\s{0,}[)]");
				matcher = pattern.matcher(colExpressHasFunc);
				rs = matcher.find();
				if(rs){
					result=matcher.group();
				}else{
					pattern = Pattern.compile("(?i)[(](.){1,}\\s{0,}as");
					matcher = pattern.matcher(colExpressHasFunc);
					rs = matcher.find();
					if(rs){
						result=matcher.group();
					}
				}
			}
			
			result=result==null?"":result.trim();
			if(result.endsWith(",")){
				result=result.substring(0,result.length()-1).trim();
			}else if(result.toUpperCase().endsWith("AS")){
				result=result.substring(0,result.length()-2).trim();
			}else if(result.toUpperCase().endsWith(")")){
				result=result.substring(0,result.length()-1).trim();
			}
			if(result.startsWith("(")){
				result=result.substring(1).trim();
			}
		}else{
			return colExpressHasFunc;
		}

		return result;
	}
	
	/**
	 * 从字段表达式提取，then或else后的表达式，如：
	 * 从 case when T.CURR_CD='14' then T.BILL_NUM*6.5 when T.CURR_CD='01' then T.BILL_NUM else cast(T.BILL_NUM as number) end as BILL_NUM,  
	 * ==>提取
	 * T.BILL_NUM*6.5、cast(T.BILL_NUM as number)
	 * @param colExpress
	 * @return
	 */
	public Set<String> matchExtractCaseWhen(String colExpress){
		//List<String> result=new ArrayList<String>();
		Set<String> result=new HashSet<String>();
		
		//判断是否是case when表达式
		//String str = " Case when T.CURR_CD='14' THEN T.BILL_NUM*6.5 when T.CURR_CD='01' THEN T.BILL_NUM else cast(T.BILL_NUM as number) end as BILL_NUM,";
		String regEx = "case\\swhen(.){1,}\\sthen\\s.{0,}end";
		Pattern pattern = Pattern.compile(regEx,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(colExpress);
		boolean rs = matcher.find();
		if(rs){
//			System.out.println("返回匹配内容："+matcher.group());
//			System.out.println("返回位置："+matcher.start());
			
			//<清洗CASE WHEN表达式>
			String str = colExpress;
			
			String regEx0 = "when\\s(.){1,}\\s{0,}then";
			String regEx1_1 = "then(.){1,}\\s(when)";
			String regEx1_2 = "then(.){1,}\\s(else)";
			String regEx2 = "else(.){1,}\\send";
			
			Pattern pattern0 = Pattern.compile(regEx0,Pattern.CASE_INSENSITIVE);
			Pattern pattern1_1 = Pattern.compile(regEx1_1,Pattern.CASE_INSENSITIVE);
			Pattern pattern1_2 = Pattern.compile(regEx1_2,Pattern.CASE_INSENSITIVE);
			
			Pattern pattern2 = Pattern.compile(regEx2,Pattern.CASE_INSENSITIVE);
			
			char[] strChars=str.toCharArray();
			StringBuilder strSb=new StringBuilder();
			for(int i=0;i<strChars.length;i++){
				String tempChar=String.valueOf(strChars[i]);
				strSb.append(tempChar);
				
				Matcher matcher0 = pattern0.matcher(strSb.toString());
				Matcher matcher1_1 = pattern1_1.matcher(strSb.toString());
				Matcher matcher1_2 = pattern1_2.matcher(strSb.toString());
				
				Matcher matcher2 = pattern2.matcher(strSb.toString());
				
				boolean rs0 = matcher0.find();
				boolean rs1_1 = matcher1_1.find();
				boolean rs1_2 = matcher1_2.find();
				boolean rs2 = matcher2.find();
				String tmpExpress=null;
				if(rs0) {
//					System.out.println("返回匹配内容："+matcher0.group());
//					System.out.println("返回位置："+matcher0.start());
					
					tmpExpress=matcher0.group();
					strSb=new StringBuilder();
					strSb.append("then ");
				}
				if(rs1_1){
//					System.out.println("返回匹配内容："+matcher1_1.group());
//					System.out.println("返回位置："+matcher1_1.start());
					
					tmpExpress=matcher1_1.group();
					strSb=new StringBuilder();
					strSb.append("when ");
				}
				if(rs1_2){
//					System.out.println("返回匹配内容："+matcher1_2.group());
//					System.out.println("返回位置："+matcher1_2.start());
					
					tmpExpress=matcher1_2.group();
					strSb=new StringBuilder();
					strSb.append("else ");
				}
				
				if(rs2){
//					System.out.println("返回匹配内容："+matcher2.group());
//					System.out.println("返回位置："+matcher2.start());
					
					tmpExpress=matcher2.group();
					strSb=new StringBuilder();
				}
				
				//去头去尾
				if(rs0 || rs1_1 || rs1_2 || rs2){
					tmpExpress=tmpExpress==null?"":tmpExpress.trim();
					int endIndex=tmpExpress.lastIndexOf(" ");
					int startIndex=tmpExpress.indexOf(" ");
					
					if(endIndex!=-1 && startIndex!=-1 && startIndex<endIndex){
						tmpExpress=tmpExpress.substring(startIndex,endIndex).trim();
						//去加减乘除
						String[] strs=tmpExpress.split("[*/+-]");
						
						String regEx3 = "\\d{1,}\\.{0,1}\\d{0,}";
						Pattern pattern3 = Pattern.compile(regEx3);
						StringBuilder tmpExpress2=new StringBuilder();
						for(int k=0;k<strs.length;k++){
							String tmpStr=strs[k]==null?"":strs[k].trim();
							Matcher matcher3 = pattern3.matcher(tmpStr);
							boolean rs3 = matcher3.matches();
							if(rs3){
								//去纯数字
								strs[k]=null;
							}else{
								tmpExpress2.append(strs[k]);
							}
						}
						tmpExpress=tmpExpress2.toString();
						//去文本连接符号
						strs=tmpExpress.split("\\|\\|");
						tmpExpress2=new StringBuilder();
						for(int k=0;k<strs.length;k++){
							String tmpstr=strs[k].trim();
							//去纯文本
							//String regEx4 = "\\s{0,}'{1,}(.){0,}'{1,}\\s{0,}";
							String regEx4 = "'{1,}(.){0,}'{1,}";
							
							Pattern pattern4 = Pattern.compile(regEx4);
							Matcher matcher4 = pattern4.matcher(tmpstr);
							boolean rs4 = matcher4.matches();
							if(rs){
								strs[k]=null;
							}else{
								tmpExpress2.append(tmpstr);
							}
						}
						tmpExpress=tmpExpress+tmpExpress2.toString();
					}
					//此处判断是否纯字符串
					String regEx4 = "'{1,}(.){0,}'{1,}";
					Pattern pattern4 = Pattern.compile(regEx4);
					Matcher matcher4 = pattern4.matcher(tmpExpress.trim());
					boolean rs4 = matcher4.matches();
					//此处判断是否纯数字
					String regEx3 = "\\d{1,}\\.{0,1}\\d{0,}";
					Pattern pattern3 = Pattern.compile(regEx3);
					Matcher matcher3 = pattern3.matcher(tmpExpress.trim());
					boolean rs3 = matcher3.matches();
					if(!(rs3||rs4)) {
						result.add(tmpExpress.trim());
					}
				}
			}
			//</清洗CASE WHEN表达式>
			
		}else{
			result.add(colExpress);
		}
		
		Iterator<String> it=result.iterator();
		Set<String> result2=new HashSet<String>();
		
		while(it.hasNext()) {
			String next=it.next();
			next=next==null?"":next;
			
			String leftExpr=null;
			String rightExpr=null;
			
			if(next.indexOf("!=")!=-1) {
				String[] strs=next.split("!=");
				leftExpr=strs[0];
				rightExpr=strs[1];
			}else if(next.indexOf("<>")!=-1) {
				String[] strs=next.split("<>");
				leftExpr=strs[0];
				rightExpr=strs[1];
			}else if(next.indexOf(">=")!=-1) {
				String[] strs=next.split(">=");
				leftExpr=strs[0];
				rightExpr=strs[1];
			}else if(next.indexOf("<=")!=-1) {
				String[] strs=next.split("<=");
				leftExpr=strs[0];
				rightExpr=strs[1];
			}else if(next.indexOf("=")!=-1) {
				String[] strs=next.split("=");
				leftExpr=strs[0];
				rightExpr=strs[1];
			}else {
				result2.add(next);
			}
			if(leftExpr!=null || rightExpr!=null) {
				leftExpr=leftExpr==null?"":leftExpr;
				rightExpr=rightExpr==null?"":rightExpr;
				
				//此处判断是否纯字符串
				String regEx4 = "'{1,}(.){0,}'{1,}";
				Pattern pattern4 = Pattern.compile(regEx4);
				Matcher matcher4 = pattern4.matcher(leftExpr.trim());
				Matcher matcher4_2 = pattern4.matcher(rightExpr.trim());
				boolean rs4 = matcher4.matches();
				boolean rs4_2 = matcher4_2.matches();
				
				//此处判断是否纯数字
				String regEx3 = "\\d{1,}\\.{0,1}\\d{0,}";
				Pattern pattern3 = Pattern.compile(regEx3);
				Matcher matcher3 = pattern3.matcher(leftExpr.trim());
				Matcher matcher3_2 = pattern3.matcher(rightExpr.trim());
				
				boolean rs3 = matcher3.matches();
				boolean rs3_2 = matcher3_2.matches();
				
				if(!(rs4 || rs3)) {
					if(!(leftExpr.trim().equals(""))) {
						result2.add(leftExpr.trim());
					}
				}
				if(!(rs4_2 || rs3_2)) {
					if(!(rightExpr.trim().equals(""))) {
						result2.add(rightExpr.trim());
					}
				}
			}
			
		}
		
		return result2;
	}
	
	//从函数中提取“表名.字段名”或“表别名.字段名”或“字段名”表达式
	//判断是否函数：包含“(” “)”，且括号字符不在单引号内 
	//判断是否case when:包含“case when”，且不在单引号内
	//判断是否常量值：1.以单引号开头，以单引号结尾
	//                2.全数字
	
	//从case when表达式中提取“表名.字段名”或“表别名.字段名”或“字段名”表达式
	//从完整SQL中根据“表别名.字段名”提取“表名.字段名”，遇到子查询需要无限向下递归寻找，找到源头“表名.字段名”
	public void test(String statement) {
		//String whereClause = "a=3 AND b=4 AND c=5";
		String whereClause = null;
		//String statement = "SELECT case when a=1 then a else 0 end a,b,c FROM mytable t1 WHERE t1.col = 9 and b=c LIMIT 3, ?";
		//String statement="select t.* from P_BLOOD_RELATIONSHIP_ERROR t where case when t.sql_number=0 then '1' else '2' end='2' AND decode(t.FILTRAT_ID,'hello') = 'DATA_SOURCE_ID'";
		
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
			//ExpressionVisitorAdapter adapter=new ExpressionVisitorAdapter() {};
			MyExpressionVisitorAdapter adapter=new MyExpressionVisitorAdapter();
			expr.accept(adapter);
			System.out.println("WHERE血缘结果："+adapter.callBackResult());
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WhereParserServiceImpl impl=new WhereParserServiceImpl();
		impl.test("select t.* from P_BLOOD_RELATIONSHIP_ERROR t where case when t.sql_number=0 then '1' else '2' end='2' AND decode(t.FILTRAT_ID,'hello') = 'DATA_SOURCE_ID'");
	}
	
}
