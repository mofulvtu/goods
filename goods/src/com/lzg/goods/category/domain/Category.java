package com.lzg.goods.category.domain;

import java.util.List;

/**
 * 分类模块实体类
 * @author 刚刚
 * @date 2016年5月14日
 */
public class Category {
	private String cid;//主键
	private String cname;//分类名称
	private Category parent;//父分类
	private String desc;//分类描述
    private List<Category> children;//子分类  双向关联+自关联
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<Category> getChildren() {
		return children;
	}
	public void setChildren(List<Category> children) {
		this.children = children;
	}
    
}
