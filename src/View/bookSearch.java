package View;
import Dboperation.BookSearch;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.sql.SQLException;

public class bookSearch {
    /**
     * 设置需要的组件比如button，JTextFiled...
     */
    private static JRadioButton orderPrice = new JRadioButton("按价格大到小排序");
    private static JRadioButton OrderPrice = new JRadioButton("按价格小到大排序");
    private static JRadioButton orderYear = new JRadioButton("按年份大到小排序");
    private static JRadioButton OrderYear = new JRadioButton("按年份小到大排序");
    public static JTextField Price = new JTextField("0", 30);
    public static JTextField Year = new JTextField("0", 30);
    public static JTextField Year2 = new JTextField("2050",30);
    public static JTextField Price2 = new JTextField("10000",30);
    JTextField Author = new JTextField(30);
    JTextField Bna = new JTextField(30);
    JTextField Cate = new JTextField(30);
    JTextField Pre = new JTextField(30);
    JTextField bnu = new JTextField(30);
    JButton searchBtn = new JButton("查询");
    private JScrollPane jScrollPane = null;

    /**
     * 进行查询界面设计
     */
    public bookSearch(){
        JFrame search = new JFrame("查询界面");
        search.setLocationRelativeTo(null);
        search.setBounds(50,40,1200,600);
        JPanel searchPane = new JPanel();
        search.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /*
         * 设置JTextField用于用户输入
         */
        Author.setBounds(130,20,200,50);
        Bna.setBounds(130,70,200,50);
        Cate.setBounds(130,120,200,50);
        Pre.setBounds(130,170,200,50);
        bnu.setBounds(130,220,200,50);
        Price.setBounds(130,270,80,50);
        Year.setBounds(130,320,80,50);
        Price2.setBounds(220,270, 80, 50);
        Year2.setBounds(220,320,80,50);
        /*
         * 设置按钮组
         */
        ButtonGroup Order = new ButtonGroup();
        Order.add(orderPrice);Order.add(OrderPrice);
        Order.add(orderYear);Order.add(OrderYear);
        orderPrice.setBounds(20,500,200,20);
        orderYear.setBounds(20,450,200,20);
        OrderPrice.setBounds(230, 500, 200,20);
        OrderYear.setBounds(230, 450,200,20);
        /*
         * 设置一个查询按钮显示
         */
        searchBtn.setBounds(800,450,90,80);
        searchPane.add(searchBtn);
        /*
         * 设置查询按钮的触发时间
         */
        searchBtn.addActionListener(evt -> {
            BookSearch booking = new BookSearch();
            /*
             * 点击按钮后获取TestField内的内容通常以String的形式
             */
            try {
                Object[][] data = booking.findBook
                        (bnu.getText().trim(), Bna.getText().trim(), Cate.getText().trim(),
                                Pre.getText().trim(), Author.getText());
                if(BookSearch.checkFormat()) {
                    if (data.length == 0) {
                        JOptionPane.showMessageDialog(null, "查无此书！");
                    }
                    jScrollPane = tableCreate(data);
                }else{
                    data = new Object[0][9];
                    jScrollPane = tableCreate(data);
                    JOptionPane.showMessageDialog(null, "输入格式有误!");
                }
                /*
                 * 下面的操作是为了重置JScrollPane分为removeAll, repaint, add, revalidate
                 */
                searchPane.removeAll();//去除所有组件
                searchPane.repaint();//重绘面板
                reset(searchPane);//重新对面板进行添加
                searchPane.add(jScrollPane);//加入新查询获得的jScrollPane
                searchPane.revalidate();//重新布局
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        reset(searchPane);
        search.add(searchPane);
        search.setVisible(true);
    }

    /**
     * 对JPanel布局进行设置
     * @param searchPane 传入一个JPanel文件
     */
    private static void setComponents(JPanel searchPane) {
        /*
         * 进行layout设置
         */
        searchPane.setLayout(null);
        /*
         * String bna, String cate, String pre, int miny, int maxy, String edit, float minp, float maxp
         * 设置大小，位置
         */
        JLabel author = new JLabel("作者");
        JLabel bna = new JLabel("书名");
        JLabel cate = new JLabel("类型目录");
        JLabel pre = new JLabel("出版社名");
        JLabel bnu = new JLabel("编号");
        JLabel pri = new JLabel("价格");
        JLabel yea = new JLabel("出版年份");
        author.setBounds(20,20,50,50);
        bna.setBounds(20,70,50,50);
        cate.setBounds(20,120,100,50);
        pre.setBounds(20,170,100,50);
        bnu.setBounds(20,220,100, 50);
        pri.setBounds(20,270,50,50);
        yea.setBounds(20,320,100,50);
        //将Label添加入该面板
        searchPane.add(author);searchPane.add(bna);searchPane.add(cate);
        searchPane.add(pre);searchPane.add(bnu);searchPane.add(pri);
        searchPane.add(yea);
    }

    /**
     * 检查复选框按钮，返回复选框选中后对应的SQL带参数语句
     * @return 一个SQL字符串
     */
    public static String checkRadioButton(){
        String choose;
        if(orderPrice.isSelected()){
            choose = "SELECT * FROM book " +
                    "where title like ? and category like ? and press like ? and author like ? and bno like ? " +
                    "and price between ? and ? and year between ? and ?" +
                    " order by price desc";
        }
        else if(OrderPrice.isSelected()){
            choose = "SELECT * FROM book " +
                    "where title like ? and category like ? and press like ? and author like ? and bno like ? " +
                    "and price between ? and ? and year between ? and ?" +
                    " order by price";
        }
        else if(orderYear.isSelected()){
            choose = "SELECT * FROM book " +
                    "where title like ? and category like ? and press like ? and author like ? and bno like ? " +
                    "and price between ? and ? and year between ? and ?" +
                    " order by year desc";
        }
        else if (OrderPrice.isSelected()){
            choose = "SELECT * FROM book " +
                    "where title like ? and category like ? and press like ? and author like ? and bno like ? " +
                    "and price between ? and ? and year between ? and ?" +
                    " order by year";
        }
        else{
            choose = "SELECT * FROM book " +
                    "where title like ? and category like ? and press like ? and author like ? and bno like ? " +
                    "and price between ? and ? and year between ? and ?" +
                    " order by title";
        }
        return choose;
    }

    /**
     * 设置一个添加组件到容器的方法
     * @param searchPane 传递进一个面板对面板进行重新组建的添加
     */
    public void reset(JPanel searchPane){
        setComponents(searchPane);
        searchPane.add(Author);
        searchPane.add(Bna);
        searchPane.add(Cate);
        searchPane.add(Pre);
        searchPane.add(bnu);
        searchPane.add(Price);
        searchPane.add(Year);
        searchPane.add(Price2);
        searchPane.add(Year2);
        searchPane.add(searchBtn);
        searchPane.add(orderPrice);searchPane.add(OrderPrice);
        searchPane.add(orderYear);searchPane.add(OrderYear);
    }
    /**
     * table create方法通过rawData参数创建JTable, 放置到JScrollPane容器中, 用于查询
     * @param rawData 经过SQL查询后保存到rawData中返回到tableCreate方法
     * @return 返回一个JScrollPane类型值
     */
    public static JScrollPane tableCreate(Object[][] rawData){
        Object[] colName= {"编码", "类型目录", "书名", "出版社", "出版年份", "作者", "价格", "库存", "总量"};
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
        jt.getColumnModel().getColumn(4).setPreferredWidth(80);
        jt.getColumnModel().getColumn(5).setPreferredWidth(120);
        jt.getColumnModel().getColumn(6).setPreferredWidth(40);
        jt.getColumnModel().getColumn(7).setPreferredWidth(40);
        jt.getColumnModel().getColumn(8).setPreferredWidth(40);
        jt.setEnabled(false);//禁止对表进行编辑
                /*
                设置JScrollPane, 应用JTable, 取消水平滚轮, 竖直滚轮需要时获得
                 */
        JScrollPane jScrollPane = new JScrollPane(jt, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBounds(370, 20, 790, 400);
        return jScrollPane;
    }
}
