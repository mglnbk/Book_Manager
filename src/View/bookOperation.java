package View;

import Entity.User;
import Utils.Tools;
import Dboperation.BookOperation;
import Dboperation.BookSearch;
import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class bookOperation {

    private Integer[] selectTime = {20, 30, 60};
    private JScrollPane cardTable = null;
    private JScrollPane viewTable = null;
    private JTextField CardInput = new JTextField();
    private JTextField BorrowBno = new JTextField();
    private JTextField ReturnBno = new JTextField();
    private JTextField cardBorrow = new JTextField();
    private JTextField cardReturn = new JTextField();
    private JButton cardSearchBtn = new JButton("查询");
    private JButton borrowBnoBtn = new JButton("借书");
    private JButton returnBnoBtn = new JButton("还书");
    private JComboBox<Integer> borrowInterval = new JComboBox<>(selectTime);

    public bookOperation(){
        JFrame operation = new JFrame("借书还书");
        operation.setLocationRelativeTo(null);
        operation.setBounds(50,40,1200,600);
        JPanel operationPane = new JPanel();
        operation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /*
         * 设置按钮查询借书证是否借了书进而显示出来或显示根本未借任何书或提示错误信息
         */
        cardSearchBtn.addActionListener(e -> {
            String cardNum = CardInput.getText().trim();
            if(cardNum.length()!=7 || !Tools.stringIsNumber(cardNum)){
                JOptionPane.showMessageDialog(null, "请输入正确的借书证号码！");
            }
            else {
                try {
                    if(!Dboperation.BookOperation.isExist(cardNum)){
                        JOptionPane.showMessageDialog(null, "该用户不存在！");
                    }
                    else {
                        BookSearch bs = new BookSearch();
                        try {
                            Object[][] data = bs.searchCardBook(cardNum);
                            viewTable = bookSearch.tableCreate(data);
                            if (data.length == 0) {
                                JOptionPane.showMessageDialog(null, "未借任何书！");
                            }
                            operationPane.removeAll();
                            operationPane.repaint();
                            setComponents(operationPane);
                            operationPane.add(viewTable);
                            operationPane.revalidate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        /*
         * 借书按钮功能实现
         */
        borrowBnoBtn.addActionListener(e ->{
            String bnu = BorrowBno.getText().trim();
            String cnu = cardBorrow.getText().trim();
            try {
                if(bnu.length()==8 && Tools.stringIsNumber(bnu) && cnu.length()==7 && Tools.stringIsNumber(cnu)) {
                    if(BookOperation.calculateBorrowNumber(cnu)>=3){
                        JOptionPane.showMessageDialog(null, "已经借阅最大数量图书！");
                    }
                    else {
                        if (BookOperation.borrowBook(bnu, cnu))
                        {
                            int a = (int) borrowInterval.getSelectedItem();
                            User newUser = new User();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            int f = BookOperation.insertBorrowRecord(cnu, bnu, df.format(new Date()), df.format(
                                    Tools.getDateAfter(new Date(), a)),
                                    newUser.getUname());
                            if (f == 1) {
                                JOptionPane.showMessageDialog(null, "借书成功！");
                            } else {
                                JOptionPane.showMessageDialog(null, "借书失败！");
                            }
                        } else if (BookOperation.blowZero(bnu)) {
                            JOptionPane.showMessageDialog(null, "该库存为零！");
                            BookOperation bo = new BookOperation();
                            Object[][] data = bo.searchCardBorrow(bnu);
                            cardTable = BookOperation.cardBorrowCreate(data);
                            operationPane.removeAll();
                            operationPane.repaint();
                            setComponents(operationPane);
                            operationPane.add(cardTable);
                            operationPane.revalidate();
                        } else if (!BookOperation.isExist(cnu)) {
                            JOptionPane.showMessageDialog(null, "不存在该借书证！");
                        } else if(BookOperation.isOnLendList(cnu, bnu)){
                            JOptionPane.showMessageDialog(null, "先前未归还同类型书！");
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "格式有误, 请重新输入！");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "数据库错误查询错误！");
            }
        });
        /*
         * 还书功能实现
         */
        returnBnoBtn.addActionListener(e ->{
            String cno = cardReturn.getText();
            String bno = ReturnBno.getText();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = df.format(new Date());
            //在时候发现我们不能够允许一个人借多本相同的书否则的话会出现错误，导致多本书一齐被还因为都是一样的书号借书号不可能
            //区分借阅时间，所以这里我们默认只能多本不同的书。
            try {
                if(bno.length()==8 && Tools.stringIsNumber(bno) && cno.length()==7 && Tools.stringIsNumber(cno)) {
                    if(BookOperation.isOnLendList(cno, bno)) {
                        BookOperation.updateReturn(cno, bno, now);
                        if (!BookOperation.isOverTime(BookOperation.selectReturn_date(cno, bno), now))
                        {
                            BookOperation.returnBook(bno);
                            BookOperation.changeConditionOfBookInTime(bno, cno);
                            JOptionPane.showMessageDialog(null, "还书成功！");
                        }
                        else if(BookOperation.isOverTime(BookOperation.selectReturn_date(cno, bno), now)) {
                            BookOperation.returnBook(bno);
                            BookOperation.changeConditionOfBookOutTime(bno, cno);
                            JOptionPane.showMessageDialog(null, "还书已超出应还日期！");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "还书失败！");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "不存在借书记录！");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "格式错误！");
                }
            } catch (SQLException | ParseException ex) {
                ex.printStackTrace();
            }
        });
        setComponents(operationPane);
        operation.add(operationPane);
        operation.setVisible(true);
    }

    /**
     * 设置panel各个组分布局等
     * @param operationPane 传入一个JPanel
     */
    private void setComponents(JPanel operationPane){
        operationPane.setLayout(null);
        /*
         * 设置借书的组件组
         */
        JLabel borrowBook = new JLabel("书号; 借书证号");
        borrowBook.setBounds(20,170,100,30);
        BorrowBno.setBounds(130, 170, 100, 50);
        borrowBnoBtn.setBounds(20, 220,100,25);
        cardBorrow.setBounds(240, 170, 100, 50);
        /*
         * 设置还书的组件组
         */
        JLabel returnBook = new JLabel("书号; 借书证号");
        returnBook.setBounds(20, 270, 100,30);
        ReturnBno.setBounds(130, 270, 100, 50);
        returnBnoBtn.setBounds(20, 320, 100, 25);
        cardReturn.setBounds(240, 270, 100, 50);
        /*
         * 设置查询所借数目的组件组
         */
        JLabel searchCard = new JLabel("查询已借书目");
        searchCard.setBounds(20,20,150,30);
        JLabel cardInput = new JLabel("借书卡号");
        cardInput.setBounds(20, 70, 100,30);
        CardInput.setBounds(130,70,200,50);
        cardSearchBtn.setBounds(20, 120, 100,25);
        /*
         * 设置下拉列表框
         */
        borrowInterval.setBounds(130, 220, 50, 25);
        /*
         * 添加组件到容器JPanel
         */
        operationPane.add(borrowBook);operationPane.add(BorrowBno);operationPane.add(borrowBnoBtn);
        operationPane.add(returnBook);operationPane.add(ReturnBno);operationPane.add(returnBnoBtn);
        operationPane.add(searchCard);operationPane.add(cardInput);operationPane.add(CardInput);
        operationPane.add(cardSearchBtn);operationPane.add(borrowInterval);operationPane.add(cardBorrow);
        operationPane.add(cardReturn);

    }
}
