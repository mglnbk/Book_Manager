package Dboperation;

import java.sql.*;
import static View.loginFrame.conn;

public class BookManage {


    /**
     * 添加书籍操作
     * @param addList 应该符合格式（）， （） .... 且不应该出现非法字符，该字符串从JTextField中读取
     * @return boolean 返回一个是否完成操作的逻辑值
     */
    public static boolean addBook(String addList){

        String SQL = "INSERT INTO book VALUES ";
        SQL = SQL  + addList ;
        Statement AddQuery;
        try {
            AddQuery = conn.createStatement();
            AddQuery.execute(SQL);
            AddQuery.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


}
