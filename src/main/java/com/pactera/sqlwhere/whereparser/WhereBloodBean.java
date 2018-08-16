package com.pactera.sqlwhere.whereparser;

public class WhereBloodBean {
	
	/**
		create table P_BLOOD_RELATIONSHIP_RESULT
		(
		  procedure_name     VARCHAR2(64),
		  sql_number         NUMBER(3),
		  target_schema      VARCHAR2(64),
		  target_table_name  VARCHAR2(128),
		  target_column_name VARCHAR2(128),
		  source_schema      VARCHAR2(64),
		  source_table_name  VARCHAR2(128),
		  source_column_name VARCHAR2(128),
		  source_column_expr VARCHAR2(4000),
		  syscode            VARCHAR2(64),
		  mapping_type       VARCHAR2(2),
		  view_or_procedure  VARCHAR2(2) default 0,
		  schema             VARCHAR2(64),
		  is_error           VARCHAR2(2) default 0
		)
		
		增加是否where条件字段
		增加目标字段表达式
		增加source_column_type
	 */
	
	private String procedureName;
	
	private Integer sqlNumber;
	
	private String targetSchema;
	
	private String targetTableName;
	
	private String targetColumnName;
	
	private String targeColumnExpr;
	
	private String sourceSchema;
	
	private String sourceTableName;
	
	private String sourceColumnName;
	
	private String sourceColumnExpr;
	
	private String syscode;
	
	private String mappingType;
	
	private String viewOrProcedure;
	
	private String schema;
	
	private String isError;
	
	private String isWhere;
	
	private String sourceColumnType;

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public Integer getSqlNumber() {
		return sqlNumber;
	}

	public void setSqlNumber(Integer sqlNumber) {
		this.sqlNumber = sqlNumber;
	}

	public String getTargetSchema() {
		return targetSchema;
	}

	public void setTargetSchema(String targetSchema) {
		this.targetSchema = targetSchema;
	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public String getTargetColumnName() {
		return targetColumnName;
	}

	public void setTargetColumnName(String targetColumnName) {
		this.targetColumnName = targetColumnName;
	}

	public String getTargeColumnExpr() {
		return targeColumnExpr;
	}

	public void setTargeColumnExpr(String targeColumnExpr) {
		this.targeColumnExpr = targeColumnExpr;
	}

	public String getSourceSchema() {
		return sourceSchema;
	}

	public void setSourceSchema(String sourceSchema) {
		this.sourceSchema = sourceSchema;
	}

	public String getSourceTableName() {
		return sourceTableName;
	}

	public void setSourceTableName(String sourceTableName) {
		this.sourceTableName = sourceTableName;
	}

	public String getSourceColumnName() {
		return sourceColumnName;
	}

	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}

	public String getSourceColumnExpr() {
		return sourceColumnExpr;
	}

	public void setSourceColumnExpr(String sourceColumnExpr) {
		this.sourceColumnExpr = sourceColumnExpr;
	}

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getMappingType() {
		return mappingType;
	}

	public void setMappingType(String mappingType) {
		this.mappingType = mappingType;
	}

	public String getViewOrProcedure() {
		return viewOrProcedure;
	}

	public void setViewOrProcedure(String viewOrProcedure) {
		this.viewOrProcedure = viewOrProcedure;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getIsError() {
		return isError;
	}

	public void setIsError(String isError) {
		this.isError = isError;
	}

	public String getIsWhere() {
		return isWhere;
	}

	public void setIsWhere(String isWhere) {
		this.isWhere = isWhere;
	}

	public String getSourceColumnType() {
		return sourceColumnType;
	}

	public void setSourceColumnType(String sourceColumnType) {
		this.sourceColumnType = sourceColumnType;
	}

	
	public String toString() {
		return "WhereBloodBean [procedureName=" + procedureName + ", sqlNumber=" + sqlNumber + ", targetSchema="
				+ targetSchema + ", targetTableName=" + targetTableName + ", targetColumnName=" + targetColumnName
				+ ", targeColumnExpr=" + targeColumnExpr + ", sourceSchema=" + sourceSchema + ", sourceTableName="
				+ sourceTableName + ", sourceColumnName=" + sourceColumnName + ", sourceColumnExpr=" + sourceColumnExpr
				+ ", syscode=" + syscode + ", mappingType=" + mappingType + ", viewOrProcedure=" + viewOrProcedure
				+ ", schema=" + schema + ", isError=" + isError + ", isWhere=" + isWhere + ", sourceColumnType="
				+ sourceColumnType + "]";
	}
	
}
