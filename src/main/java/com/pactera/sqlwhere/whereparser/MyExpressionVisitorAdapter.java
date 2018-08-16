package com.pactera.sqlwhere.whereparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;

public class MyExpressionVisitorAdapter extends ExpressionVisitorAdapter {

	
	private List<WhereBloodBean> whereBloodResult=new ArrayList<WhereBloodBean>();
	
	public List<WhereBloodBean> callBackResult(){
		return this.whereBloodResult;
	}
	
	public void visit(AndExpression expr) {
	    if (expr.getLeftExpression() instanceof AndExpression) {
	        expr.getLeftExpression().accept(this);
	    } else if ((expr.getLeftExpression() instanceof EqualsTo)){
	        expr.getLeftExpression().accept(this);
	        //System.out.println(expr.getLeftExpression());
	    }
	    expr.getRightExpression().accept(this);
	    //System.out.println(expr.getRightExpression());
	}
	public void visit(EqualsTo expr) {
		WhereParserServiceImpl whereImpl=new WhereParserServiceImpl();
	    System.out.println("解析前-左:"+expr.getLeftExpression());
	    Set<String> whereLeftItems=whereImpl.matchExtractCaseWhen(expr.getLeftExpression().toString());
	    Iterator<String> it=whereLeftItems.iterator();
	    List<WhereBloodBean> leftResult=new ArrayList<WhereBloodBean>();
	    while(it.hasNext()) {
	    	String resultItm=it.next();
	    	resultItm=whereImpl.matchExtractFunction(resultItm);
	    	boolean isNumber=whereImpl.isNumber(resultItm);
	    	boolean isVarchar=whereImpl.isVarchar(resultItm);
	    	boolean isColumn=!isNumber && !isVarchar;
	    	
	    	String type=null;
	    	if(isNumber) {
	    		type="数字常量";
	    	}else if(isVarchar) {
	    		type="字符常量";
	    	}else if(isColumn) {
	    		type="字段";
	    	}
	    	WhereBloodBean bean=new WhereBloodBean();
	    	bean.setTargeColumnExpr(expr.getLeftExpression().toString());
	    	bean.setTargetColumnName(resultItm);
	    	leftResult.add(bean);
	    	System.out.println("解析后-左："+resultItm+",type:"+type);
	    }
	    //System.out.println("比较符:"+expr.getStringExpression());
	    System.out.println("解析前-右:"+expr.getRightExpression());
	    Set<String> whereRightItems=whereImpl.matchExtractCaseWhen(expr.getRightExpression().toString());
	    Iterator<String> it2=whereRightItems.iterator();
	    //左右字段笛卡尔积集合
	    List<WhereBloodBean> leftRightCPResult=new ArrayList<WhereBloodBean>();
	    while(it2.hasNext()) {
	    	String resultItm=it2.next();
	    	resultItm=whereImpl.matchExtractFunction(resultItm);
	    	
	    	boolean isNumber=whereImpl.isNumber(resultItm);
	    	boolean isVarchar=whereImpl.isVarchar(resultItm);
	    	boolean isColumn=!isNumber && !isVarchar;
	    	
	    	String type=null;
	    	if(isNumber) {
	    		type="数字常量";
	    	}else if(isVarchar) {
	    		type="字符常量";
	    	}else if(isColumn) {
	    		type="字段";
	    	}

	    	for(int i=0;i<leftResult.size();i++) {
	    		WhereBloodBean beanLeft=leftResult.get(i);
	    		String targetColumnExpr=beanLeft.getTargeColumnExpr();
	    		String targetColumnName=beanLeft.getTargetColumnName();
	    		
		    	WhereBloodBean bean=new WhereBloodBean();
		    	bean.setSourceColumnExpr(expr.getRightExpression().toString());
		    	bean.setSourceColumnName(resultItm);
		    	bean.setSourceColumnType(type);
		    	bean.setTargeColumnExpr(targetColumnExpr);
		    	bean.setTargetColumnName(targetColumnName);
		    	leftRightCPResult.add(bean);
	    	}
	    	System.out.println("解析后-右："+resultItm+",type:"+type);
	    }
	    this.whereBloodResult=leftRightCPResult;
	}
}
