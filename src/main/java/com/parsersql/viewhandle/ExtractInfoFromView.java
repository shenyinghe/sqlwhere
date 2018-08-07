package com.parsersql.viewhandle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class ExtractInfoFromView {
	
	/**
	 * 从视图源码文本文件中提取SQL
	 * @param viewSourcePath
	 * @return
	 */
	public String extractSqlFromView(String viewSourcePath){
		BufferedReader br=null;
		BufferedWriter bw=null;
		String fold=viewSourcePath.substring(0,viewSourcePath.lastIndexOf("/")+1);
		UUID uuid=UUID.randomUUID();
		String outputFileName=fold+uuid.toString().replace("-", "")+".txt";
		
        try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(viewSourcePath), "UTF-8"));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-8"));
			String line=null;
			boolean sqlFlag=false;
			while ((line = br.readLine()) != null){
				while(sqlFlag==false && line.toUpperCase().indexOf("AS")!=-1 && line.length()>line.toUpperCase().indexOf("AS") && line.toUpperCase().indexOf("AS")>0){
					//取得“AS”的前一位字符
					String startAs=line.substring(line.toUpperCase().indexOf("AS")-1,line.toUpperCase().indexOf("AS"));
					//取得“AS”的后一位字符
					String endAs=line.substring(line.toUpperCase().indexOf("AS")+2,line.toUpperCase().indexOf("AS")+3);
					
					boolean isAsFlag=(startAs.equals(" ") || startAs.equals("	")) && (endAs.equals(" ") || endAs.equals("	"));
					if(isAsFlag==true){
						line=line.substring(line.toUpperCase().indexOf("AS")+2);
						sqlFlag=true;
						break;
					}else{
						line=line.substring(line.toUpperCase().indexOf("AS")+2);
					}
				}
				if(sqlFlag==true){
					bw.write(line);
					bw.newLine();
					bw.flush();
				}
			}
			return outputFileName;
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
			if(bw!=null){
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        return null;
	}
}
