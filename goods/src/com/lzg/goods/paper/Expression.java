package com.lzg.goods.paper;

/**
 * <p>Title:Expression</p>
 * <p>Description:用来把SQL语句的where子句进行封装</p>
 * @author 刘增刚
 * @date 2016年9月1日下午3:45:40
 */
public class Expression {
	private String name;//ex:uid , bname 对应数据库中的字段
	private String operator;//ex: = 、 like
	private String value;//ex:uid , bname 从页面获得的值，即SQL的查询条件

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Expression() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Expression(String name, String operator, String value) {
		super();
		this.name = name;
		this.operator = operator;
		this.value = value;
	}
	
	

}
