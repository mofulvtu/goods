package com.lzg.goods.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import com.lzg.goods.book.domain.Book;
import com.lzg.goods.book.service.BookService;
import com.lzg.goods.paper.PageBean;

public class BookServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private BookService bookService = new BookService();

	/**
	 * 获取当前页页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if (param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return pc;
	}

	/**
	 * 截取url,页面中的分页导航中需要使用它作为超链接的目标
	 * @param req
	 * @return
	 */
	/*
	 * http://localhost:8080/goods/BookServlet?method=findByCategory&cid=xxx
	 * goods/BookServlet + method=findByCategory&cid=xxx&pc=3
	 */
	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();// getQueryString()获取的是？后的字符串
		/*
		 * 如果url中存在pc参数，截取掉
		 */
		int index = url.lastIndexOf("&pc=");
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}

	/**
	 * 按分类查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.得到pc:如果页面传递，使用页面的，否则，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获得查询条件，本方法就是cid,即分类的id
		 */
		String cid = req.getParameter("cid");
		/*
		 * 4.使用pc和cid调用service#findByCategory得到PageBean
		 */
		System.out.println(pc+"======"+cid);
		
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		
		System.out.println(pb);
		
		/*
		 * 5.给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 按作者查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.得到pc:如果页面传递，使用页面的，否则，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		System.out.println(url);
		/*
		 * 3.获得查询条件，本方法就是author,即作者名
		 */
		String author = req.getParameter("author");
		/*
		 * 4.使用pc和author调用service#findByAuthor得到PageBean
		 */
		System.out.println(pc+"======"+author);
		
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		
		System.out.println(pb);
		
		/*
		 * 5.给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	/**
	 * 按出版社查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByPress(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.得到pc:如果页面传递，使用页面的，否则，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获得查询条件，本方法就是press,即出版社
		 */
		String press = req.getParameter("press");
		/*
		 * 4.使用pc和press调用service#findByPress得到PageBean
		 */
		System.out.println(pc+"======"+press);
		
		PageBean<Book> pb = bookService.findByPress(press, pc);
		
		System.out.println(pb);
		
		/*
		 * 5.给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	/**
	 * 按书名查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByBname(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.得到pc:如果页面传递，使用页面的，否则，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获得查询条件，本方法就是bname,即书名
		 */
		String bname = req.getParameter("bname");
		/*
		 * 4.使用pc和bname调用service#findByBname得到PageBean
		 */
		System.out.println(pc+"======"+bname);
		
		PageBean<Book> pb = bookService.findByBname(bname, pc);
		
		System.out.println(pb);
		
		/*
		 * 5.给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	/**
	 * 复杂查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCombination(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.得到pc:如果页面传递，使用页面的，否则，pc=1
		 */
		int pc = getPc(req);
		/*
		 * 2.得到url:
		 */
		String url = getUrl(req);
		/*
		 * 3.获得查询条件，本方法需要封装成Book对象
		 */
        Book criteria = CommonUtils.toBean(req.getParameterMap(), Book.class);
		/*
		 * 4.使用pc和criteria调用service#findByCombination得到PageBean
		 */
		System.out.println(pc+"======"+criteria);
		
		PageBean<Book> pb = bookService.findByCombination(criteria, pc);
		
		System.out.println(pb);
		
		/*
		 * 5.给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 按id查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}

}
