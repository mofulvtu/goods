package com.lzg.goods.category.service;

import java.sql.SQLException;
import java.util.List;

import com.lzg.goods.category.dao.CategoryDao;
import com.lzg.goods.category.domain.Category;

/**
 * 分类模块业务层
 * @author 刚刚
 * @date 2016年5月14日
 */
public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll(){
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	

}
