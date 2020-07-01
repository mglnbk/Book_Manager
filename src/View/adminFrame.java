package View;


import Utils.Dbutil;
import javax.swing.*;
import java.sql.SQLException;
import static View.loginFrame.conn;

public class adminFrame {

    public static void mainFrame() {
        JFrame mainFrame = new JFrame("管理主界面");
        mainFrame.setSize(400, 200);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        /*
         * 创建按钮和设置位置大小
         */
        JButton b1 = new JButton("图书查询");
        JButton b2 = new JButton("图书增删");
        JButton b3 = new JButton("图书证管理");
        JButton b4 = new JButton("借书还书");
        b1.setBounds(50,100,20,40);
        b2.setBounds(100,100,20,40);
        b3.setBounds(150,100,20,40);
        b4.setBounds(200,100,20,40);
        panel.add(b1); panel.add(b2); panel.add(b3); panel.add(b4);
        /*
         * 创建图书查询的事件触发
         */
        b1.addActionListener(e -> new bookSearch());
        /*
         * 创建图书删除增加的事件触发
         */
        b2.addActionListener(e -> new bookManage());
        /*
         * 创建借书还书的事件触发
         */
        b4.addActionListener(e -> new bookOperation());
        /*
         * 创建图书馆里的界面触发
         */
        b3.addActionListener(e -> new cardManage());
        /*
         * 创建退出登录按钮，并创建监听事件
         */
        JButton exit = new JButton("退出登录");
        exit.setBounds(150, 150, 20, 40);
        panel.add(exit);
        exit.addActionListener(e -> {
            try {
                Dbutil.closeCon(conn);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            mainFrame.dispose();
            loginFrame.LoginFrame();
        });
        mainFrame.add(panel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }
}
