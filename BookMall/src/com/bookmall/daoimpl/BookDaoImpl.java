package com.bookmall.daoimpl;

import com.bookmall.beans.Book;
import com.bookmall.beans.Page;
import com.bookmall.dao.BaseDao;
import com.bookmall.dao.BookDao;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.List;

public class BookDaoImpl extends BaseDao<Book> implements BookDao {

    @Override
    public List<Book> getBooks(Connection conn) {
        List<Book> beanList = null;
        String sql = "SELECT id, title, author, price, sales, stock, img_path imgPath FROM books";
        beanList = getBeanList(conn, sql);
        return beanList;
    }

    @Override
    public Integer saveBook(Connection conn, Book book) {
        Integer auto_increment_id = null;
        String sql = "INSERT INTO books (title, author, price, sales, stock, img_path) VALUES (?, ?, ?, ?, ?, ?);";
        // 调用BaseDao类中的update方法
        auto_increment_id = insert(conn, sql, book.getTitle(), book.getAuthor(), book.getPrice(),
                book.getSales(), book.getStock(), book.getImgPath());
        return auto_increment_id;
    }

    @Override
    public void deleteBookById(Connection conn, String bookId) {
        String sql = "DELETE FROM books WHERE id = ?;";
        update(conn, sql, bookId);
    }

    @Override
    public Book getBookById(Connection conn, String bookId) {
        Book book = null;
        String sql = "SELECT title, author, price, sales, stock, img_path imgPath FROM books WHERE id = ?;";
        book = getBean(conn, sql);
        return book;
    }

    @Override
    public void updateBook(Connection conn, Book book) {
        String sql = "UPDATE book SET title = ?, author = ?, price = ?, sales = ?, stock = ? WHERE id = ?;";
        update(conn, sql, book.getTitle(), book.getAuthor(), book.getPrice(),
                book.getSales(), book.getStock(), book.getId());
    }

    @Override
    public Page<Book> getPageBooks(Connection conn, Page<Book> page) {
        // 获取数据库中图书的总记录数
        String sql = "SELECT COUNT(*) FROM books;";
        long totalRecord = (long) getValue(conn, sql);
        // 将总记录数设置到page对象中
        page.setTotalRecord((int) totalRecord);

        // 获取当前页中的记录存放到List
        String sql2 = "SELECT id, title, author, price, sales, stock, img_path imgPath FROM books LIMIT ?, ?;";
        List<Book> beanList = getBeanList(conn, sql2, (page.getPageNo() - 1) * page.getPageSize(), page.getPageSize());
        // 将这个List设置到page对象中
        page.setList(beanList);
        return page;
    }

    @Override
    public Page<Book> getPageBooksByPrice(Connection conn, Page<Book> page, double minPrice, double maxPrice) {
        String sql = "SELECT count(*) FROM books WHERE price BETWEEN ? AND ?;";
        // 获取图书价格在[minPrice, maxPrice]的总记录数
        long totalRecord = (long) getValue(conn, sql, minPrice, maxPrice);
        page.setTotalRecord((int) totalRecord);

        String sql2 = "SELECT id, title, author, price, sales, stock, img_path imgPath FROM books WHERE price BETWEEN ? AND ? LIMIT ?, ?;";
        List<Book> beanList = getBeanList(conn, sql2, minPrice, maxPrice, (page.getPageNo() - 1) * page.getPageSize(), page.getPageSize());
        page.setList(beanList);
        return page;
    }

    /**
     * 获取最畅销的图书
     * @return
     */
    public List<Book> getBestSellingBooks(Connection conn) {
        List<Book> beanList = null;
        String sql = "SELECT id, title, author, price, sales, stock, img_path imgPath FROM books WHERE sales = (SELECT MAX(sales) FROM books)";
        beanList = getBeanList(conn, sql);
        return beanList;
    }

    /**
     * 修改制定ID的图书的库存
     *
     * @param conn
     * @param bookId
     * @param stock
     */
    public void modifyBookStockById(Connection conn, Integer bookId, int stock) {
        String sql = "UPDATE books SET stock = ? WHERE id = ?;";
        update(conn, sql, stock, bookId);
    }
}
