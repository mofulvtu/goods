package com.lzg.goods.paper;

import java.util.List;

/**
 * <p>Title:PageBean</p>
 * <p>Description:分页Bean，将页面展示所需的数据包装成PageBean</p>
 * @author 刘增刚
 * @date 2016年8月20日上午10:45:14
 */
public class PageBean<T> {
	private int pc;// 当前页码
	private int tr;// 总记录数
	private int ps;// 每页记录数
	private String url;// 请求路径和参数
	private List<T> beanList;//所有书的列表

	// 计算总页数
	public int getTp() {
		int tp = tr / ps;
		return tr % ps == 0 ? tp : tp + 1;
	}
	public void setTr(int tr) {
		this.tr = tr;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<T> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}

}
