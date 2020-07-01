//
//* Connection to Local Database, insertion method
//* @Sun
//
package Utils;
import java.sql.*;

public class Dbutil {
    static final String DB_URL = "jdbc:mysql://localhost:3306/db2020?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8";
    /**
     * 取得MySQL的连接
     * @Return 一个物理连接
     */
    public static Connection getCon(String User_Name, String User_Password){
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("驱动加载成功...");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("数据库驱动加载失败");
        }
        try {
            conn = DriverManager.getConnection(DB_URL, User_Name, User_Password);
            System.out.println("建立连接成功...");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("建立连接失败...");
        }
        return conn;
    }
    /**
     * 中断MySQL的连接，释放内存
     */
    public static void closeCon(Connection conn) throws SQLException {
        if(conn!=null){
            conn.close();
        }
    }


}



