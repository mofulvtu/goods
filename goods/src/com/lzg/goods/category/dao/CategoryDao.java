package com.lzg.goods.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.lzg.goods.category.domain.Category;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class CategoryDao {
	
	 QueryRunner queryRunner = new TxQueryRunner();
	 
	 /*
	  * 把一个Map中的数据映射到Category中
	  */
	 private Category toCategory(Map<String, Object> map){
		 /*
		  * map(cid:xx,cname:xx,pid:xx,desc:xx,orderBy:xx)
		  * Category(cid:xx,cname:xx,parent:xx,desc:xx)
		  */
		 Category category = CommonUtils.toBean(map, Category.class);
		 String pid = (String) map.get("pid");
		 if(pid != null){//如果父分类id不为空
			 /*
			  * 使用一个父分类对象来包装pid
			  * 再把父分类设置给category
			  */
			 Category parent = new Category();//创建父分类
			 parent.setCid(pid);//包装成父分类给Category
			 category.setParent(parent);
		 }
		 return category;
	 }
	 
	 /*
	  * 可以把多个Map(List<Map>) 映射成多个Category(List<Map<String,Object>> mapList)
	  */
	 private List<Category> toCategoryList(List<Map<String, Object>> mapList){
		 List<Category> categoryList = new ArrayList<Category>();
		 for(Map<String, Object> map: mapList){
			 Category c = toCategory(map);
			 categoryList.add(c);
		 }
		 return categoryList;
	 }
	 
	 /**
	  * 返回所有分类
	  * @return
	 * @throws SQLException 
	  */
	 public List<Category> findAll() throws SQLException{
		 /*
		  * 1.查询出所有一级分类
		  */
		String sql = "select * from t_category where pid is null";
		List<Map<String, Object>> mapList = queryRunner.query(sql, new MapListHandler());
		
		List<Category> parents = toCategoryList(mapList);
		
		/*
		 * 2.循环遍历所有的一级分类，为每个一级分类加载它的二级分类
		 */
		for(Category parent : parents){
			List<Category> children = findByParent(parent.getCid());
			parent.setChildren(children);
		}
		return parents;
	 }
	 
	 /**
	  * 通过父分类查询子分类
	  * @param pid
	  * @return
	 * @throws SQLException 
	  */
	 public List<Category> findByParent(String pid) throws SQLException{
		 String sql = "select * from t_category where pid = ?";
		 List<Map<String, Object>> mapList =queryRunner.query(sql, new MapListHandler(), pid);
		 return toCategoryList(mapList);
	 }
 
}
