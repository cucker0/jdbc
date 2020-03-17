package com.bookmall.www;

import com.bookmall.Utils.JdbcUtils;
import com.bookmall.beans.Book;
import com.bookmall.beans.Order;
import com.bookmall.beans.OrderItem;
import com.bookmall.beans.User;
import com.bookmall.daoimpl.BookDaoImpl;
import com.bookmall.daoimpl.OrderDaoImpl;
import com.bookmall.daoimpl.OrderItemDaoImpl;
import com.bookmall.daoimpl.UserDaoImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Mall {
    private static Scanner sc = new Scanner(System.in);
    private static UserDaoImpl userDao = new UserDaoImpl();
    private static BookDaoImpl bookDao = new BookDaoImpl();
    private static OrderDaoImpl orderDao = new OrderDaoImpl();
    private static OrderItemDaoImpl orderItemDao = new OrderItemDaoImpl();
    private static Connection conn = JdbcUtils.getConnection();

    @Test
    public void test() {
        String str = "123456";
        String sign = DigestUtils.sha1Hex(str);
        System.out.println(sign);
    }

    /**
     * 4. 添加用户
     */
    public static void addUser() {
        while (true) {
            System.out.println("用户名(q: 退出)：");
            String username = sc.next();
            if (username.equalsIgnoreCase("q")) {
                return;
            }
            System.out.println("密码：");
            String password = sc.next();
            System.out.println("email：");
            String email = sc.next();

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);

            Integer id = userDao.saveUser(conn, user);
            if (id != null) {
                System.out.println("用户添加成功");
            }
        }
    }

    /**
     * 添加图书
     */
    public static void addBook() {
        while (true) {
            System.out.println("书名(q: 退出)：");
            String title = sc.next();
            if (title.equalsIgnoreCase("q")) return;
            System.out.println("作者：");
            String author = sc.next();
            System.out.println("价格(￥)：");
            double price = sc.nextDouble();
            System.out.println("库存：");
            int stock = sc.nextInt();
            System.out.println("图片：");
            String imgPath = sc.next();

            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setPrice(price);
            book.setSales(0);
            book.setStock(stock);
            book.setImgPath(imgPath);
            Integer id = bookDao.saveBook(conn, book);
            if (id != null) {
                System.out.println(id);
                System.out.println("图书添加成功");
            }
        }
    }

    /**
     * 用户登录
     */
    public static void login() {
        System.out.println("== 登录系统用 ==");
        System.out.print("用户名：");
        String username = sc.next();
        System.out.print("密码：");
        String password = sc.next();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        boolean isValid = userDao.login(conn, user);
        if (isValid) {
            System.out.println("\n登录成功");
        } else {
            System.out.println("\n用户名或密码错误！");
        }
    }

    /**
     * 查询所有图书
     */
    public static void getAllBooks() {
        List<Book> books = bookDao.getBooks(conn);
        System.out.println("所有图书：");
        books.forEach(System.out::println);
    }

    /**
     * 查询最畅销图书
     */
    public static void getBestSellingBook() {
        List<Book> books = bookDao.getBestSellingBooks(conn);
        System.out.println("最畅销书：");
        books.forEach(System.out::println);
    }

    /**
     * 修改指定图书的库存
     * 修改库存量小于10本的图书的库存量为100
     */
    public static void modifyBookStock() {
        System.out.println("图书id：");
        Integer id = sc.nextInt();
        System.out.println("库存：");
        int stock = sc.nextInt();
        bookDao.modifyBookStockById(conn, id, stock);
    }

    public static void getOrderByUserId() {
        System.out.println("\n查询用户订单\n");
        System.out.println("输入User ID：");
        String userId = sc.next();
        Order order = orderDao.getOrderByUserId(conn, userId);
        System.out.println("订单：");
        System.out.println(order);
        OrderItem orderItem = orderItemDao.getOrderItemByOrderId(conn, order);
        System.out.println("订单详情：");
        System.out.println(orderItem);
    }

    public static void deleteOrder() {
        System.out.println("\n删除用户订单\n");
        System.out.println("订单ID（q退出）：");
        String orderId = sc.next();
        if (orderId.equalsIgnoreCase("q")) {
            return;
        }
        Order order = orderDao.getOrderById(conn, orderId);
        if (order == null) {
            System.out.println("此订单不存在");
            return;
        }
        orderItemDao.deleteOrderItemByOrderId(conn, orderId);
        orderDao.deleteOrderById(conn, orderId);
    }

    public static void main(String[] args) {
        String options = "\n\n== 书屋商城 ==\n\n" +
                "a. 添加用户\n" +
                "b. 添加图书\n" +
                "c. 登陆\n" +
                "d. 查询所有图书\n" +
                "e. 查询最畅销图书\n" +
                "f. 修改图书库存\n" +
                "g. 查询用户订单\n" +
                "h. 删除用户订单\n" +
                "q. 退出系统\n\n" +
                "选择项：";
        while (true) {
            System.out.println(options);
            String option = sc.next().toLowerCase();
            try {
                switch (option) {
                    case "a":
                        addUser();
                        break;
                    case "b":
                        addBook();
                        break;
                    case "c":
                        login();
                        break;
                    case "d":
                        getAllBooks();
                        break;
                    case "e":
                        getBestSellingBook();
                        break;
                    case "f":
                        modifyBookStock();
                        break;
                    case "g":
                        getOrderByUserId();
                        break;
                    case "h":
                        deleteOrder();
                        break;
                    case "q":
                        return;
                    default:
                        System.out.println("无此选项");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
