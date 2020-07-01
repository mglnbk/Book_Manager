package View;

import Utils.Dbutil;

import javax.swing.*;
import java.sql.SQLException;

import static View.loginFrame.conn;

public class userFrame {

    public static void mainFrame(){
        JFrame mainFrame = new JFrame("用户主界面");
        mainFrame.setSize(400, 200);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        /*
         * 创建按钮和设置位置大小
         */
        JButton b1 = new JButton("图书查询");
        b1.setBounds(50,100,20,40);
        JButton exit = new JButton("退出登录");
        exit.setBounds(150, 100, 20, 40);
        panel.add(b1);
        panel.add(exit);
        /*
         * 创建图书查询的事件触发
         */
        b1.addActionListener(e -> new bookSearch());
        /*
        添加退出到主登录界面的按钮
         */
        exit.addActionListener(e -> {
            try {
                Dbutil.closeCon(conn);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            mainFrame.dispose();
            loginFrame.LoginFrame();
        });
        /*
        将panel加入容器中并给予显示
         */
        mainFrame.add(panel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);


    }

}

