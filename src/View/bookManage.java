package View;

import Dboperation.BookManage;
import Utils.Tools;

import javax.swing.*;

public class bookManage {
    private String[] strings = {"History", "Mathematics", "Computer", "Music", "Classics", "Medicine", "Economics", "Chemistry"
    , "Chess"};
    private JTextField getNum = new JTextField();
    private JTextField getName = new JTextField();
    private JTextField getPress = new JTextField();
    private JTextField getYear = new JTextField();
    private JTextField getAuthor = new JTextField();
    private JTextField getPrice = new JTextField();
    private JTextField getTotal = new JTextField();
    private JTextField getStock = new JTextField();
    private JTextField getTxtURL = new JTextField("D:\\ecl\\Intellij for mysql\\src\\book.txt");
    private JButton addBtn = new JButton("单本入库");
    private JButton addMultipleBtn = new JButton("批量入库");
    private JComboBox<String> categoryComboBox = new JComboBox<>(strings);
    /**
     * 设置增删图书的图形化界面
     */
    public bookManage(){
        JFrame manage = new JFrame("增删图书");
        manage.setLocationRelativeTo(null);
        manage.setBounds(335,100,580,500);
        JPanel managePane = new JPanel();
        manage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /*
         * 创建按钮触发增加操作
         */
        addBtn.addActionListener(e -> {
            String str = "('" + getNum.getText() + "', '" + categoryComboBox.getSelectedItem() + "', '" + getName.getText() + "', '"
                    + getPress.getText() + "', " + getYear.getText() + ", '" + getAuthor.getText() + "', " +
                    getPrice.getText() + ", " + getTotal.getText() + ", " + getStock.getText() + ")";
            //('书号', '类别', '书名', '出版社', 年份, '作者', 价格, 数量, 库存)
            if (BookManage.addBook(str)) {
                JOptionPane.showMessageDialog(null, "入库成功！");
            } else {
                JOptionPane.showMessageDialog(null, "入库失败！请检查输入数据是否有重复或错误！");
            }
        });
        addMultipleBtn.addActionListener(e -> {
            if(BookManage.addBook(Tools.multipleInsertion(getTxtURL.getText()))){
                JOptionPane.showMessageDialog(null, "导入数据成功");
            }
            else{
                JOptionPane.showMessageDialog(null, "导入失败，请检查文件的文本内容格式或路径！");
            }
        });
        addBtn.setBounds(315, 160, 100, 25);
        addMultipleBtn.setBounds(325, 250, 100, 25);
        setComponent(managePane);
        manage.add(managePane);
        manage.setVisible(true);
    }

    private void setComponent(JPanel jPanel){
        jPanel.setLayout(null);
        /*
         * 设置Label,"比如 ('书号', '类别', '书名', '出版社', 年份, '作者', 价格, 数量, 库存)"
         */
        JLabel addSingle = new JLabel("单本入库");
        JLabel multipleAdd = new JLabel("批量入库");
        JLabel bno = new JLabel("书号");
        JLabel category = new JLabel("类型目录");
        JLabel boName = new JLabel("书名");
        JLabel press = new JLabel("出版社");
        JLabel year = new JLabel("年份");
        JLabel author = new JLabel("作者");
        JLabel price = new JLabel("价格");
        JLabel total = new JLabel("总量");
        JLabel stock = new JLabel("库存");
        JLabel reminder = new JLabel("文件路径通常存储于src文件夹中，路径默认为D:\\ecl\\Intellij for mysql\\src\\book.txt");
        JLabel changePath = new JLabel("修改路径：");
        addSingle.setBounds(250, 10, 100, 25);
        multipleAdd.setBounds(215,250, 100, 25);
        bno.setBounds(100, 40, 50, 25); getNum.setBounds(155, 40, 110, 25);
        category.setBounds(100, 70, 50, 25); categoryComboBox.setBounds(155, 70, 110, 25);
        boName.setBounds(100, 100, 50, 25); getName.setBounds(155, 100, 110, 25);
        press.setBounds(100, 130, 50, 25); getPress.setBounds(155, 130, 110, 25);
        year.setBounds(100, 160, 50, 25); getYear.setBounds(155, 160, 110, 25);
        author.setBounds(270, 40, 50, 25); getAuthor.setBounds(315, 40, 110, 25);
        price.setBounds(270, 70, 50, 25); getPrice.setBounds(315, 70, 110, 25);
        total.setBounds(270, 100, 50, 25); getTotal.setBounds(315, 100, 110, 25);
        stock.setBounds(270, 130, 50, 25); getStock.setBounds(315, 130, 110, 25);
        reminder.setBounds(50, 280, 500, 25); changePath.setBounds(180, 310, 75, 25);
        getTxtURL.setBounds(260, 310, 200, 25);
        jPanel.add(addSingle);
        jPanel.add(multipleAdd);
        jPanel.add(addBtn);
        jPanel.add(addMultipleBtn);
        jPanel.add(bno); jPanel.add(getNum);
        jPanel.add(category); jPanel.add(categoryComboBox);
        jPanel.add(boName); jPanel.add(getName);
        jPanel.add(press); jPanel.add(getPress);
        jPanel.add(year); jPanel.add(getYear);
        jPanel.add(author); jPanel.add(getAuthor);
        jPanel.add(price); jPanel.add(getPrice);
        jPanel.add(total); jPanel.add(getTotal);
        jPanel.add(stock); jPanel.add(getStock);
        jPanel.add(reminder); jPanel.add(changePath);
        jPanel.add(getTxtURL);
    }

}
