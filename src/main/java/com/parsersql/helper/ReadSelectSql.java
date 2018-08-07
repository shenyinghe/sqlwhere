package com.parsersql.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadSelectSql {
	
	public static void main(String[] args) {
		ReadSelectSql readSelectSql=new ReadSelectSql();
		List<Map<Integer, Integer>> list=readSelectSql.matchBrackets(" (select to_char(xxx,xxxx),t.* from xxxcx)");
		System.out.println(list);
		
	}
	
	/**
	 * 由matchSubquery3循环调用matchSubquery2方法，直到取得子查询最简式
	 * @param selectSql
	 * @param tabName
	 * @param indexOf
	 * @return
	 */
	public String matchSubquery3(String selectSql, String tabName, int indexOf){
		String result=this.matchSubquery2(selectSql, tabName, indexOf);
		String resultTmp=result;
		while(true){
			if(result!=null && result.length()>0){
				result=result.substring(1);
				result=this.matchSubquery2(result, tabName, indexOf);
				if(result!=null && result.length()>0){
					resultTmp=result;
				}
			}else{
				break;
			}
		}
		return resultTmp;
	}
	
	private String matchSubquery2(String selectSql, String tabName, int indexOf){
		Integer findIndex=0;
		
		tabName=tabName==null?"":tabName.trim();
		char[] tabChars=tabName.toCharArray();
		StringBuilder tabRex=new StringBuilder();
		for(int i=0;i<tabChars.length;i++){
			String tabChar=String.valueOf(tabChars[i]);
			tabRex.append("["+tabChar.toUpperCase()+"|"+tabChar.toLowerCase()+"]");
		}
		
		while(true){
			String regEx = "[(][\\s]{0,}[S|s][E|e][L|l][E|e][C|c][T|t][\\s]{0,}.{1,}[)][\\s]{1,}(?i)[as]{0,}[\\s]{0,}"+tabRex.toString()+"[\\s]{1,}";//"++"
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(selectSql);
			if(findIndex>selectSql.length()){
				break;
			}
			boolean rs = matcher.find(findIndex);
			System.out.println(rs);
			if(rs){
				findIndex=findIndex+matcher.start()+matcher.group().length();
				System.out.println("返回匹配内容："+matcher.group());
				System.out.println("返回位置："+matcher.start());
				return matcher.group();
			}else{
				break;
			}
		}
		return null;
	}
	
	/**
	 * 匹配字符串中括号组()
	 * @param content
	 * @return
	 */
	public List<Map<Integer, Integer>> matchBrackets(String content){
		List<Map<Integer, Integer>> list=new ArrayList<Map<Integer,Integer>>();

		int fromIndex=0;
		if(content.indexOf("(", fromIndex)==-1){
			return list;
		}else{
			Map<Integer, Integer> map=new HashMap<Integer, Integer>();
			map.put(content.indexOf("(", fromIndex), null);
			list.add(map);//"++"
			fromIndex=content.indexOf("(", fromIndex)+1;
		}
		
		while(true){
			int intS=content.indexOf("(", fromIndex);
			int intE=content.indexOf(")", fromIndex);
			
			boolean flag=(intS!=-1 && intE!=-1 && intE<intS) || (intS==-1 && intE!=-1);
			boolean flag2=intS!=-1 && intE!=-1 && intE>intS;
			if(flag==true){
				Map<Integer, Integer> tmp=null;
				for(int i=list.size()-1;i>=0;i--){
					tmp=list.get(i);
					Iterator it=tmp.keySet().iterator();
					boolean breakflag=false;
					while(it.hasNext()){
						Object obj=it.next();
						Integer key=(Integer) obj;
						if(tmp.get(key)==null){
							breakflag=true;
							break;
						}
					}
					if(breakflag){
						break;
					}
				}
				//tmp=tmp+intE;
				Iterator it=tmp.keySet().iterator();
				while(it.hasNext()){
					Object obj=it.next();
					Integer key=(Integer) obj;
					tmp.put(key, intE);
				}
				
				fromIndex=intE+1;
			}else if(flag2==true){
				Map<Integer, Integer> map=new HashMap<Integer, Integer>();
				map.put(intS, null);
				list.add(map);
				fromIndex=intS+1;
			}else{
				break;
			}
		}
		return list;
	}
	
	/**
	 * 统计字符串subKey在字符串content中出现的次数
	 * @param content
	 * @param subKey
	 * @return
	 */
	public int countStr(String content,String subKey){
		int count=0;
		while(true){
			if(content.indexOf(subKey)!=-1){
				count=count+1;
				content=content.substring(content.indexOf(subKey)+1);
			}else{
				break;
			}
		}
		return count;
	}
	
	
	public String readSelectSql(String sqlFilePath){
		BufferedReader br=null;
        try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFilePath), "UTF-8"));
			String line=null;
			
			StringBuilder sb=new StringBuilder();
			while (( line = br.readLine() ) != null){
				//如果含有“--”sql单行注释符号，则需要判断注释符号之前的单引号是否是双数，如果是单数则不是注释符号
				sb.append(line+" ");
			}
			System.out.println("打印去除单行注释的sql:"+sb.toString());
			String strSql=sb.toString();
			return strSql;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        return null;
	}
	
}
