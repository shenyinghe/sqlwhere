package com.pactera.filechange;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 筛选在某时间之后被修改过的文件（在没有可靠版本管理工具的条件下使用）
 * @author shenyinghe
 *
 */
public class FileChangeUtils {
	
	public static void main(String[] args) throws ParseException {
		FileChangeUtils utils=new FileChangeUtils();
		utils.outputChangeFileByTime();
	}
	
	/**
	 * 比较两个文件路径是否相等
	 * @param path1
	 * @param path2
	 * @return
	 */
	private boolean comparePath(String path1,String path2) {
		path1=path1==null?"":path1.replace("\\", "/");
		path2=path2==null?"":path2.replace("\\", "/");
		if(path1.endsWith("/")) {
			path1=path1.substring(0,path1.length()-1);
		}
		if(path2.endsWith("/")) {
			path2=path2.substring(0,path2.length()-1);
		}
		
		if(path1.equals(path2)) {
			return true;
		}
		return false;
	}
	
	
	public void outputChangeFileByTime() throws ParseException {
		List<String> paths=new ArrayList<String>();
		paths.add("E:/workspace_sqlparser/datacontrol");//初始目录
		
		
		List<String> excludePaths=new ArrayList<String>();
		excludePaths.add("E:/workspace_sqlparser/datacontrol/target");
		
		String paramTime="2018-08-14 00:00:00";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date paramDate=sdf.parse(paramTime);
		
		//1.递归遍历目录下所有文件，找到最后修改时间
		for(int i=0;i<paths.size();i++) {
			String path=paths.get(i);
			File[] files=(new File(path)).listFiles();
			for(int k=0;k<files.length;k++) {
				File file=files[k];
				if(file.exists() && file.isDirectory()) {
					boolean isExclude=false;
					for(int k2=0;k2<excludePaths.size();k2++) {
						String excludePath=excludePaths.get(k2);
						boolean isEqualPath=this.comparePath(excludePath, file.getPath());
						if(isEqualPath) {
							isExclude=true;
							break;
						}
					}
					if(!isExclude) {
						paths.add(file.getPath());
					}
				}else if(file.exists() && file.isFile()) {
					//正题--
					//2.与参数时间对比，如果大于参数时间则输出
					long modifyTime=file.lastModified();
					Date modifyDate=new Date(modifyTime);
					if(modifyDate.getTime()>paramDate.getTime()) {
						//输出文件路径、修改时间
						String modifyTimeText=sdf.format(modifyDate);
						System.out.println("文件名:"+file.getPath()+"，在"+modifyTimeText+"被修改。");
					}
				}
			}
		}
	}
}
