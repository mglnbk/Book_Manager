package Dboperation;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static View.loginFrame.conn;

public class BookOperation {

    /**
     * 借书的SQL操作，库存-1
     * @param bookNumber 要借的书名
     * @return 一个boolean值查看是否借书成功，若成功返回真
     * @throws SQLException 抛出异常，库存需要大于等于0
     */
    public static boolean borrowBook(String bookNumber, String cno) throws SQLException {
        String SQL = "UPDATE book set stock = stock-1 where bno = ? ";
        PreparedStatement borrowQuery;
        if(!blowZero(bookNumber)&&!isOnLendList(cno, bookNumber)&&isExist(cno)) {
            try {
                borrowQuery = conn.prepareStatement(SQL);
                borrowQuery.setString(1, bookNumber);
                borrowQuery.execute();
                borrowQuery.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        else
            return false;
    }

    /**
     * 还书的静态方法，使库存+1
     * @param bookNumber 要还书的书名
     */
    public static void returnBook(String bookNumber){
        String SQL = "UPDATE book set stock = stock+1 where bno = ?";
        PreparedStatement returnQuery;
        try {
            returnQuery = conn.prepareStatement(SQL);
            returnQuery.setString(1, bookNumber);
            returnQuery.execute();
            returnQuery.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 检测该借书卡是否存在于数据库中
     * @param cno 借书卡号
     * @return 返回一个是否存在的逻辑值
     * @throws SQLException 抛出异常
     */
    public static boolean isExist(String cno) throws SQLException {
        boolean flag = false;
        String SQLExist = "SELECT * FROM Card where cno =?";
        PreparedStatement isQuery = conn.prepareStatement(SQLExist);
        isQuery.setString(1, cno);
        ResultSet rs = isQuery.executeQuery();
        if(rs.next()){
            flag = true;
        }
        isQuery.close();
        rs.close();
        return flag;
    }

    /**
     * 判定借书后该库存是否会小于零
     * @param bno 图书编号
     * @return 返回一个是否小于零的逻辑值，true为借书后则会小于零
     * @throws SQLException 抛出异常
     */
    public static boolean blowZero(String bno) throws SQLException {
        String query = "SELECT stock FROM book where bno = ?";
        boolean flag = false;
        PreparedStatement psmt = conn.prepareStatement(query);
        psmt.setString(1, bno);
        ResultSet rs = psmt.executeQuery();
        int a = 1;
        if(rs.next()){
            a = rs.getInt("stock");
        }
        if(a<=0){
            flag = true;
        }
        psmt.close();
        rs.close();
        return flag;
    }

    /**
     * 返回库存为0的书籍的相关信息如上一次还款的日期等
     * @param rawData 输入一个二元数组储存着信息
     * @return 返回一个JScrollPane
     */
    public static JScrollPane cardBorrowCreate(Object[][] rawData){
        Object[] colName= {"书码", "借书证号", "书名", "借书日期", "应还日期", "实还日期", "库存"};
        JTable jt = new JTable(rawData, colName);
        JTableHeader jTableHeader = jt.getTableHeader();
        jTableHeader.setResizingAllowed(false);
        jTableHeader.setReorderingAllowed(false);
        jt.setRowHeight(25);
        jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//禁止自动适应
        jt.getColumnModel().getColumn(0).setPreferredWidth(80);
        jt.getColumnModel().getColumn(1).setPreferredWidth(80);
        jt.getColumnModel().getColumn(2).setPreferredWidth(150);
        jt.getColumnModel().getColumn(3).setPreferredWidth(140);
        jt.getColumnModel().getColumn(4).setPreferredWidth(140);
        jt.getColumnModel().getColumn(5).setPreferredWidth(140);
        jt.getColumnModel().getColumn(6).setPreferredWidth(40);
        jt.setEnabled(false);//禁止对表进行编辑
                /*
                设置JScrollPane, 应用JTable, 取消水平滚轮, 竖直滚轮需要时获得
                 */
        JScrollPane jScrollPane = new JScrollPane(jt, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBounds(370, 20, 770, 400);
        return jScrollPane;
    }

    /**
     * 用于返回一个储存借书证的借书情况的数组的一个方法
     * @param bnu 书籍编号
     * @return 返回一个二维数组
     * @throws SQLException 抛出异常
     */
    public Object[][] searchCardBorrow(String bnu) throws SQLException {
        int i;
        String SQL = "SELECT bno, cno, title, borrow_date, return_date, true_return, stock"
                +" FROM book natural join borrow " +
                "WHERE bno = ? and return_date > (select now()) and whether_return = '未还' order by return_date ";
        PreparedStatement SelectQuery = conn.prepareStatement(SQL);
        SelectQuery.setString(1, bnu);
        ResultSet rs = SelectQuery.executeQuery();
        rs.last();
        i = rs.getRow();
        Object[][] rawData = new Object[i][7];
        rs.beforeFirst();
        int j = 0;
        while (rs.next()) {
            //取得数据库内的值
            String bno = rs.getString("bno");
            String cno = rs.getString("cno");
            String title = rs.getString("title");
            String borrowDate = rs.getString("borrow_date");
            String returnDate = rs.getString("return_date");
            String true_return = rs.getString("true_return");
            int stock = rs.getInt("stock");
            rawData[j][0] = bno;
            rawData[j][1] = cno;
            rawData[j][2] = title;
            rawData[j][3] = borrowDate;
            rawData[j][4] = returnDate;
            rawData[j][5] = true_return;
            rawData[j][6] = stock;
            j++;
        }
        rs.close();
        SelectQuery.close();
        return rawData;
    }

    /**
     * 插入到借书记录里的一个方法
     * @param cno 借书证号
     * @param bno 图书编号
     * @param borrow_date 借书日期
     * @param return_date 应还书日期
     * @return 返回一个是否插入的逻辑值
     * @throws SQLException 抛出SQL异常
     */
    public static int insertBorrowRecord(String cno, String bno, String borrow_date, String return_date, String id)
            throws SQLException {
        String insertQuery = "INSERT INTO borrow VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement psmt = conn.prepareStatement(insertQuery);
        psmt.setString(1, cno);
        psmt.setString(2, bno);
        psmt.setString(3, borrow_date);
        psmt.setString(4, return_date);
        psmt.setString(5, return_date);
        psmt.setString(6, "未还");
        psmt.setString(7, id);
        int i = psmt.executeUpdate();
        psmt.close();
        return i;
    }

    /**
     * 还书的数据库操作方法使true_return记录更新
     * @param cno 借书证编号
     * @param bno 图书编号
     * @param true_return 借书实还日期
     * @throws SQLException 抛出异常
     */
    public static void updateReturn(String cno, String bno, String true_return)
            throws SQLException{
        String updateReturn = "UPDATE borrow set true_return = ? where cno = ? and bno = ? and whether_return = '未还'";
        String changeQuery = "Update borrow set whether_return = '已还' where cno = ? and bno = ? and whether_return = '未还'";
        PreparedStatement psmt = conn.prepareStatement(updateReturn);
        PreparedStatement psmo = conn.prepareStatement(changeQuery);
        psmo.setString(1, cno);
        psmo.setString(2, bno);
        psmt.setString(1, true_return);
        psmt.setString(2, cno);
        psmt.setString(3, bno);
        psmo.execute();
        psmt.executeUpdate();
        psmo.close();
        psmt.close();
    }

    /**
     * 判断是否已经还书超时的方法
     * @param deserved_date 应还日期String类型
     * @param return_date 实还日期String类型
     * @return 返回逻辑值
     */
    public static boolean isOverTime(String deserved_date, String return_date) throws ParseException {
        boolean flag = true;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date deserve = df.parse(deserved_date);
        Date true_return = df.parse(return_date);
        int a = deserve.compareTo(true_return);
        System.out.println(df.format(deserve));
        if(a>=0) {
            flag = false;
        }
        return flag;
    }

    /**
     * 查询是否该人已经借阅过相同的书籍
     * @param card 卡号
     * @param book 书号
     * @return 返回一个逻辑值
     * @throws SQLException 抛出异常
     */
    public static boolean isOnLendList(String card, String book) throws SQLException {
        boolean flag;
        String Query = "SELECT * FROM borrow where cno = ? and bno = ? and whether_return = '未还'";
        PreparedStatement psmt = conn.prepareStatement(Query);
        psmt.setString(1, card);
        psmt.setString(2, book);
        ResultSet rs = psmt.executeQuery();
        flag = rs.next();
        rs.close();
        psmt.close();
        return flag;
    }

    /**
     * 查询借书应还日期
     * @param cno 卡号
     * @param bno 书号
     * @return 返回一个日期String类型
     * @throws SQLException 抛出异常
     */
    public static String selectReturn_date(String cno, String bno) throws SQLException {
        String date = null;
        String query = "SELECT return_date FROM borrow where cno = ? and bno = ?";
        PreparedStatement psmt = conn.prepareStatement(query);
        psmt.setString(1, cno);
        psmt.setString(2, bno);
        ResultSet rs = psmt.executeQuery();
        if(rs.next())
        date = rs.getString("return_date");
        rs.close();
        psmt.close();
        return date;
    }

    /**
     * 用于还书后更新该书已经被还了
     * @param bno 书记编码
     * @param cno 卡号
     */
    public static void changeConditionOfBookInTime(String bno, String cno) throws SQLException {
        String Update = "update borrow set whether_return = '已还' where bno = ? and cno = ? and whether_return = '未还'";
        PreparedStatement psmt = conn.prepareStatement(Update);
        psmt.setString(1, bno);
        psmt.setString(2, cno);
        psmt.execute();
        psmt.close();
    }

    /**
     * 用于超期归还的数目设置书籍状态
     * @param bno 书本编号
     * @param cno 借书卡号
     * @throws SQLException 抛出异常
     */
    public static void changeConditionOfBookOutTime(String bno, String cno) throws SQLException {
        String Update = "update borrow set whether_return = '超期归还' where bno = ? and cno = ? and whether_return = '未还'";
        PreparedStatement psmt = conn.prepareStatement(Update);
        psmt.setString(1, bno);
        psmt.setString(2, cno);
        psmt.execute();
        psmt.close();
    }

    /**
     * 返回该借书证当前在借多少本书
     * @param cno 卡号
     * @return 返回借书书量
     * @throws SQLException 抛出
     */
    public static int calculateBorrowNumber(String cno) throws SQLException {
        String findNum = "SELECT * FROM borrow where cno = ? and whether_return = '未还'";
        PreparedStatement query = conn.prepareStatement(findNum);
        query.setString(1, cno);
        ResultSet rs = query.executeQuery();
        int cal;
        rs.last();
        cal = rs.getRow();
        query.close();
        rs.close();
        return cal;
    }


}
