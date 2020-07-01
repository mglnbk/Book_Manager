package Dboperation;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.sql.*;
import static View.loginFrame.conn;

public class CardManage {
    /**
     * 增添借书卡的方法
     * @param str 增添的字符串语句
     */
    public static int addCard(String str) throws SQLException {
        String addBook = "INSERT INTO card Values";
        addBook = addBook + str;
        Statement stmt = conn.createStatement();
        int a = stmt.executeUpdate(addBook);
        stmt.close();
        return a;
    }

    /**
     * 删除一个借书证
     * @param cno 借书证号码
     * @throws SQLException 抛出异常
     */
    public static void delCard(String cno) throws SQLException {
        String delBook = "delete from card where cno = ?";
        PreparedStatement psmt = conn.prepareStatement(delBook);
        psmt.setString(1, cno);
        psmt.executeUpdate();
        psmt.close();
    }

    /**
     * 返回当前借书证借阅情况和借书证本身信息
     * @param cno 借书证号码
     * @return 返回一个二维数组储存信息
     * @throws SQLException 抛出异常
     */
    public static Object[][] returnCardBorrow(String cno) throws SQLException {
        String Query = "SELECT * FROM card natural join borrow WHERE cno = ?";
        PreparedStatement search = conn.prepareStatement(Query);
        search.setString(1, cno);
        ResultSet rs = search.executeQuery();
        rs.last();
        int i = rs.getRow();
        rs.beforeFirst();
        Object[][] data = new Object[i][9];
        int j = 0;
        while(rs.next()){
            data[j][0] = rs.getString("cno");
            data[j][1] = rs.getString("cname");
            data[j][2] = rs.getString("department");
            data[j][3] = rs.getInt("type");
            data[j][4] = rs.getString("bno");
            data[j][5] = rs.getString("borrow_date");
            data[j][6] = rs.getString("true_return");
            data[j][7] = rs.getString("whether_return");
            j++;
        }
        rs.close();
        search.close();
        return data;
    }

    /**
     * 一个写JScrollPane的方法
     * @param rawData 原数据
     * @return 返回一个JScrollPane
     */
    public static JScrollPane searchScrollPane(Object[][] rawData){
        Object[] colName = {"借书证号码", "姓名", "学院", "学籍", "书籍编号", "借书时间", "归还时间", "在借状态"};
        JTable jt = new JTable(rawData, colName);
        JTableHeader jTableHeader = jt.getTableHeader();
        jTableHeader.setResizingAllowed(false);
        jTableHeader.setReorderingAllowed(false);
        jt.setRowHeight(25);
        jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//禁止自动适应
        jt.getColumnModel().getColumn(0).setPreferredWidth(80);
        jt.getColumnModel().getColumn(1).setPreferredWidth(80);
        jt.getColumnModel().getColumn(2).setPreferredWidth(100);
        jt.getColumnModel().getColumn(3).setPreferredWidth(40);
        jt.getColumnModel().getColumn(4).setPreferredWidth(80);
        jt.getColumnModel().getColumn(5).setPreferredWidth(160);
        jt.getColumnModel().getColumn(6).setPreferredWidth(160);
        jt.getColumnModel().getColumn(7).setPreferredWidth(60);
        jt.setEnabled(false);
        /*
         * 设置JScrollPane, 应用JTable, 取消水平滚轮, 竖直滚轮需要时获得
         */
        JScrollPane jScrollPane = new JScrollPane(jt, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(410, 20, 760, 400);
        return jScrollPane;
    }

    /**
     * 查询该借书证是否有借走图书
     * @param cno 借书证号
     * @return 返回一个逻辑值
     * @throws SQLException 抛出异常
     */
    public static boolean queryIfLend(String cno) throws SQLException {
        String query = "SELECT * FROM borrow WHERE cno = ? and whether_return = '未还'";
        PreparedStatement psmt = conn.prepareStatement(query);
        psmt.setString(1, cno);
        ResultSet rs = psmt.executeQuery();
        boolean f;
        f = rs.next();
        rs.close();
        psmt.close();
        return f;
    }

}
