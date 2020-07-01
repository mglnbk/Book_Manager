package View;

import Dboperation.BookOperation;
import Dboperation.CardManage;
import Utils.Tools;
import javax.swing.*;
import java.sql.SQLException;

public class cardManage {

    private String[] deptName = {"Computer Science", "Mathematics", "History", "English", "Physics", "Chemistry", "Literature",
    "Medicine", "Energy", "Electronic Engineering"};
    private String[] typeNum = {"本科生", "研究生", "教师"};
    private JTextField cardNum = new JTextField();
    private JTextField addNum = new JTextField();
    private JTextField addName = new JTextField();
    private JComboBox<String> addDept= new JComboBox<>(deptName);
    private JComboBox<String> addType = new JComboBox<>(typeNum);
    private JTextField delNum = new JTextField();
    private JButton searchBtn = new JButton("查询");
    private JButton addBtn = new JButton("增加");
    private JButton delBtn = new JButton("删除");
    JScrollPane borrowScroll;

    public cardManage() {
        JFrame cardManage = new JFrame("图书证管理");
        cardManage.setLocationRelativeTo(null);
        cardManage.setBounds(50,40,1200,600);
        JPanel cardPane = new JPanel();
        cardManage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /*
         * 查询按钮事件触发
         */
        searchBtn.addActionListener(e -> {
            String cno = cardNum.getText().trim();
            if(cno.length()==7 && Tools.stringIsNumber(cno)){
                try {
                    if(BookOperation.isExist(cno)) {
                        try {
                            Object[][] data = CardManage.returnCardBorrow(cno);
                            borrowScroll = CardManage.searchScrollPane(data);
                            if (data.length == 0) {
                                JOptionPane.showMessageDialog(null, "之前未借任何书！");
                            }
                            cardPane.removeAll();
                            cardPane.repaint();
                            setComp(cardPane);
                            cardPane.add(borrowScroll);
                            cardPane.revalidate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "不存在该借书证！");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "输入格式错误！");
            }
        });
        /*
         * 增加借书证按钮事件触发
         */
        addBtn.addActionListener(e -> {
            String type;
            if(addType.getSelectedItem() == "本科生"){
                type = String.valueOf(1);
            }
            else if(addType.getSelectedItem() == "研究生"){
                type = String.valueOf(2);
            }
            else
                type = String.valueOf(3);
            String dept = (String) addDept.getSelectedItem();
            String number = addNum.getText().trim();
            String name = addName.getText();
            if(number.length()==7 && Tools.stringIsNumber(number)) {
                if(!Tools.isEmpty(name)) {
                    String query = "(" + "'" + number + "'" + ", " + "'" + name + "'" + ", " + "'" + dept + "'" + ", " + type + ")";
                    try {
                        if (CardManage.addCard(query)!=0){
                            JOptionPane.showMessageDialog(null, "添加成功！");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "添加失败！");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "姓名不能为空！");
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "输入格式错误！");
            }
        });
        /*
         * 删去借书证按钮事件触发
         */
        delBtn.addActionListener(e -> {
            String cno = delNum.getText().trim();
            if(cno.length()==7 && Tools.stringIsNumber(cno)){
                try {
                    if(!CardManage.queryIfLend(cno)) {
                        if (BookOperation.isExist(cno)) {
                            CardManage.delCard(cno);
                            JOptionPane.showMessageDialog(null, "删除成功！");
                        } else
                            JOptionPane.showMessageDialog(null, "不存在该借书证！");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "该借书证有书未还！");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            else
                JOptionPane.showMessageDialog(null, "输入格式错误！");
        });
        setComp(cardPane);
        cardManage.add(cardPane);
        cardManage.setVisible(true);
    }

    private void setComp(JPanel pane){
        pane.setLayout(null);
        /*
         * 查询借书证目前的借阅状况
         */
        JLabel returnCardAllBorrow = new JLabel("查询借书证借阅情况");
        returnCardAllBorrow.setBounds(20, 20, 150, 20);//cardManage.setBounds(50,40,900,400);
        JLabel searchCarNum = new JLabel("查询卡号");
        searchCarNum.setBounds(20, 50, 100, 20);
        cardNum.setBounds(130, 50, 100, 25);
        searchBtn.setBounds(20, 80, 100, 25);
        /*
         * 增添图书证
         */
        JLabel addCard = new JLabel("增添图书证");
        JLabel addCno = new JLabel("图书证编号");
        JLabel addCname = new JLabel("姓名");
        JLabel dept = new JLabel("部门");
        JLabel type = new JLabel("学籍");
        addCard.setBounds(20, 120, 150, 20);
        addCno.setBounds(20, 150, 100, 20);
        addNum.setBounds(130, 150, 100, 25);
        addCname.setBounds(240, 150, 50, 20);
        addName.setBounds(300, 150, 100, 25);
        dept.setBounds(20, 180, 100, 20);
        addDept.setBounds(130, 180, 100, 25);
        type.setBounds(240, 180, 50, 20);
        addType.setBounds(300, 180, 100, 25);
        addBtn.setBounds(20, 210, 100, 25);
        /*
         * 删除书本操作
         */
        JLabel delCard = new JLabel("删除借书证");
        JLabel delCNum = new JLabel("借书证号");
        delCard.setBounds(20, 240, 100, 20);
        delCNum.setBounds(20, 270, 100, 20);
        delNum.setBounds(130, 270, 100,25);
        delBtn.setBounds(20, 310, 100, 25);
        /*
         * 加入到Pane中去
         */
        pane.add(returnCardAllBorrow);
        pane.add(cardNum);
        pane.add(searchBtn);
        pane.add(addCard);
        pane.add(addCno);
        pane.add(addNum);
        pane.add(addCname);
        pane.add(addName);
        pane.add(dept);
        pane.add(addDept);
        pane.add(type);
        pane.add(addType);
        pane.add(addBtn);
        pane.add(delCard);
        pane.add(delCNum);
        pane.add(delBtn);
        pane.add(delNum);
        pane.add(searchCarNum);
    }
}
