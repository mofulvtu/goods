package com.lzg.goods.book.service;

import java.sql.SQLException;

import com.lzg.goods.book.dao.BookDao;
import com.lzg.goods.book.domain.Book;
import com.lzg.goods.paper.PageBean;

/**
 * <p>Title:BookService</p>
 * <p>Description:</p>
 * @author 刘增刚
 * @date 2016年8月20日下午4:32:37
 */
public class BookService {

	private BookDao bookDao = new BookDao();
	
	/**
	 * 按分类查
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCategory(String cid, int pc){
		try {
			return bookDao.findByCategory(cid, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按书名查
	 * @param bname
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByBname(String bname, int pc){
		try {
			return bookDao.findByBname(bname, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按作者查
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author, int pc){
		try {
			return bookDao.findByAuthor(author, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 按出版社查
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press, int pc){
		try {
			return bookDao.findByPress(press, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 多条件组合查询
	 * @param criteria
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book criteria, int pc){
		try {
			return bookDao.findByCombinastion(criteria, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}