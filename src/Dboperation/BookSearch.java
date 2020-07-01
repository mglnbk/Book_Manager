package Dboperation;

import Utils.Tools;
import View.bookSearch;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static View.bookSearch.*;
import static  View.loginFrame.conn;

public class BookSearch {

    /**
     * 获取bookSearch中的成员变量所取得String值
     */
    private static String minPrice;
    private static String maxPrice;
    private static String minYear;
    private static String maxYear;
    /**
     * 书名关键字检索
     * @return 返回一个二维Object数组
     * @throws SQLException 抛出异常
     */
    public Object[][] findBook(String bnu, String bna, String cate, String pre, String edit) throws SQLException {
        int i;
        bna = '%' + bna + '%';
        cate = '%' + cate + '%';
        pre = '%' + pre + '%';
        edit = '%' + edit + '%';
        bnu = '%' + bnu + '%';
        /*
         * 准备SQL字符串语句并且，设置参数
         * SQL由bookSearch内的checkRadioButton传递而来
         */
        String SQL = bookSearch.checkRadioButton();
        PreparedStatement SelectQuery = conn.prepareStatement(SQL);
        SelectQuery.setString(1, bna);
        SelectQuery.setString(2, cate);
        SelectQuery.setString(3, pre);
        SelectQuery.setString(4, edit);
        SelectQuery.setString(5, bnu);
        //判断价格输入和年份输入是否非空和是否都为小数型，若非空且都为小数型则进行重置参数
        minPrice = Price.getText().trim();
        maxPrice = Price2.getText().trim();
        minYear = Year.getText().trim();
        maxYear = Year2.getText().trim();
        SelectQuery.setString(6, minPrice);
        SelectQuery.setString(7, maxPrice);
        SelectQuery.setString(8, minYear);
        SelectQuery.setString(9, maxYear);
        if (Tools.isNumeric(minPrice) && Tools.isNumeric(maxPrice) && !Tools.isEmpty(minPrice) && !Tools.isEmpty(maxPrice)){
            SelectQuery.setString(6, minPrice);
            SelectQuery.setString(7, maxPrice);
        }
        if (Tools.stringIsNumber(minYear) && Tools.stringIsNumber(maxYear) && !Tools.isEmpty(minYear) && !Tools.isEmpty(maxYear)){
            SelectQuery.setString(8, minYear);
            SelectQuery.setString(9, maxYear);
        }
        ResultSet rs = SelectQuery.executeQuery();
        rs.last();
        i = rs.getRow();
        Object[][] rawData = new Object[i][9];
        rs.beforeFirst();
        int j = 0;
        while (rs.next()) {
            //取得数据库内的值
            String bno = rs.getString("bno");
            String category = rs.getString("category");
            String title = rs.getString("title");
            String press = rs.getString("press");
            int year = rs.getInt("year");
            String author = rs.getString("author");
            int stock = rs.getInt("stock");
            float price = rs.getFloat("price");
            int total = rs.getInt("total");
            rawData[j][0] = bno;
            rawData[j][1] = category;
            rawData[j][2] = title;
            rawData[j][3] = press;
            rawData[j][4] = year;
            rawData[j][5] = author;
            rawData[j][6] = price;
            rawData[j][7] = stock;
            rawData[j][8] = total;
            j++;
        }
        rs.close();
        SelectQuery.close();
        return rawData;
    }

    /**
     * 为借书卡查询写的一个方法
     * @param cnu 输入一个字符串
     * @return 返回一个二维数组Object
     * @throws SQLException 抛出SQL异常
     */
    public Object[][] searchCardBook(String cnu) throws SQLException {
        int i;
        String SQL = "SELECT bno, category, title, press, year, author, stock, price, total"
                +" FROM book natural join borrow " +
                "WHERE cno = ? and whether_return = '未还'";
        PreparedStatement SelectQuery = conn.prepareStatement(SQL);
        SelectQuery.setString(1, cnu);
        ResultSet rs = SelectQuery.executeQuery();
        rs.last();
        i = rs.getRow();
        Object[][] rawData = new Object[i][9];
        rs.beforeFirst();
        int j = 0;
        while (rs.next()) {
            //取得数据库内的值
            String bno = rs.getString("bno");
            String category = rs.getString("category");
            String title = rs.getString("title");
            String press = rs.getString("press");
            int year = rs.getInt("year");
            String author = rs.getString("author");
            int stock = rs.getInt("stock");
            float price = rs.getFloat("price");
            int total = rs.getInt("total");
            rawData[j][0] = bno;
            rawData[j][1] = category;
            rawData[j][2] = title;
            rawData[j][3] = press;
            rawData[j][4] = year;
            rawData[j][5] = author;
            rawData[j][6] = price;
            rawData[j][7] = stock;
            rawData[j][8] = total;
            j++;
        }
        rs.close();
        SelectQuery.close();
        return rawData;
    }

    /**
     * 用于检查是否价格区间和年份区间格式有误
     * @return 返回一个逻辑值
     */
    public static boolean checkFormat(){
        boolean f = true;
        if(!Tools.isNumeric(minPrice)||!Tools.isNumeric(maxPrice)||!Tools.stringIsNumber(minYear)||!Tools.stringIsNumber(maxYear)
        ||Tools.isEmpty(minPrice)||Tools.isEmpty(maxPrice)||Tools.isEmpty(minYear)||Tools.isEmpty(maxYear)){
            f = false;
        }
        return f;
    }
}
