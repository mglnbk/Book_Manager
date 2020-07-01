package Utils;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

public class Tools {

    /**
     * 检查字符串是否为空
     * @param str 一个字符串
     * @return 一个逻辑值
     */
    public static boolean isEmpty(String str){
        boolean flag = false;
        if(str == null || str.trim().equals("")){
            flag = true;
        }
        return flag;
    }

    /**
     * 检测是否该字符是否由1234567890和.小数点组成
     * @param str 输入一个字符串
     * @return 返回一个逻辑值
     */
    public static boolean isNumeric(String str)
    {
        for(int i=str.length();--i>=0;)
        {
            int chr=str.charAt(i);
    if(chr < 48 && chr != 46 || chr > 57)
        return false;
        }
        return true;
    }

    /**
     * 测试是否该字符串都是数字
     * @param str 传递入一个字符串
     * @return 返回一个逻辑值，真则全是数字
     */
    public static boolean stringIsNumber(String str){
        boolean flag = true;
        for(int i = 0; i<str.length(); i++){
            if(str.charAt(i) < 48 || str.charAt(i) > 57){
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 获取几天后的时间
     * @param current 指现在时间或想要设定时间
     * @param day 过了几天
     * @return 返回一个日期，需要dateForm进行转化格式
     */
    public static Date getDateAfter(Date current,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(current);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
    }

    /**
     * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
     */
    public static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    /**
     * 利用Java I/O导入外部TXT文件
     * @param URL 外部地址URL
     * @return 返回一个已经拼接完成的SQL语句
     */
    public static String multipleInsertion(String URL){
        File file = new File(URL);
        BufferedReader reader = null;
        String tempString;
        StringBuilder resultString = new StringBuilder();
        int line =1;
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            while ((tempString = reader.readLine()) != null) {
                System.out.println("Line"+ line + ":" +tempString);
                resultString.append(",").append(tempString);
                line ++ ;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultString.substring(1);
    }



}
