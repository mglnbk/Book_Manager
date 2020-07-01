package View;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import Utils.*;
import Entity.*;

public class loginFrame {
    public static Connection conn;
    public static User newUser = new User();
    public static void LoginFrame() {
        JFrame loginFrame= new JFrame("图书管理系统");
        loginFrame.setSize(300, 150);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        loginFrame.add(panel);
        setComponents(panel);
        /*
        设置用户文本框用于输入并设置了他们的大小和位置
         */
        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);
        /*
        设置密码输入文本框
         */
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);
        /*
         * 创建登陆按钮
         */
        JButton loginButton = new JButton("登录");
        loginButton.setBounds(98, 80, 80, 25);
        panel.add(loginButton);
        /*
         * 设置事件监听
         */
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                loginEvent();
            }
            private void loginEvent() {
                String user = userText.getText();
                String password = new String(passwordText.getPassword());
                if(Tools.isEmpty(user) || Tools.isEmpty(password)){
                    JOptionPane.showMessageDialog(null, "用户名或密码不能为空！");
                }
                else{
                    newUser.setUname(user);
                    newUser.setUpwd(password);
                    conn = Dbutil.getCon(newUser.getUname(), newUser.getUpwd());
                    if(conn == null){
                        JOptionPane.showMessageDialog(null, "用户名或密码错误");
                    }
                    else if(newUser.getUname().equals("root") || newUser.getUname().equals("User1")){
                        JOptionPane.showMessageDialog(null, "管理员登陆成功！");
                        adminFrame.mainFrame();
                        loginFrame.dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "用户登陆成功！");
                        userFrame.mainFrame();
                        loginFrame.dispose();
                    }
                }
            }
        });
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
    /**
     * 安排该面板的内容
     * @param panel 安排组成组件
     */
    private static void setComponents(JPanel panel){
        /*
        不进行Layout排版
         */
        panel.setLayout(null);
        /*
        创建两个Label分别作为登陆时的用户名和密码,并分别设置位置和大小
         */
        JLabel userLabel = new JLabel("用户名");
        JLabel passwordLabel = new JLabel("密码");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);
    }

}
