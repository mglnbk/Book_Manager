# Book_Manager
学校课程大作业
## 一、实验目的
1. 设计并实现一个精简的图书管理系统, 具有图书入库、查询、借书、还书、借书证管理等功能。
2. 通过该图书馆系统的设计与实现，提高学生的系统编程能力，加深对数据库系统原理及应用理解。
## 二、实验总体设计
### I. 系统框架概述
该图书管理系统采用Java编写，设想的图书管理系统为由图书馆工作人员输入借书信息从而进行操作，基于此理念，采用**Java Swing**设计前端界面，采用JDBC与MySQL进行数据库连接进行相关操作，具体实现了**书籍查询，书籍入库，书籍借阅，图书证管理**四大功能板块。有**Dboperation，View，Utils，Entity**四个包来实现功能，在数据库层面设计了**book，card，borrow，user**四个关系表来存储数据。同时在设计时对错误信息处理采用应用层面和数据库层同时进行控制，从而基本实现了要求功能。同时在编写过程中，增添了一些功能方便图书管理，比如**输出借书证所借的书，关键字查询，从外部文件TXT文件通过Java I/O批量入库**，也增加了表的属性如已是否还书的属性**Whether_return**便于之后的程序功能编写。
- **Dboperation包**：包含大部分直接与数据库通过JDBC API交互的方法

- **View包**：主要为前端设计，以及按钮监听等

- **Utils包**：一些必要工具，如判断字符是否全为数字，数据库连接getCon()方法等

- **Entity包**：用于封装存储登录User用户的信息

### II. 数据库表结构(Schema)
#### i）E-R关系设计和表结构

| 对象名称    |包含属性     |
| :-- | :-- |
| 书    | 书号, 类别, 书名, 出版社, 年份, 作者, 价格, 总藏书量, 库存    |
|    借书证 | 卡号, 姓名, 单位, 类别 (教师, 本科生, 研究生)    |
|  管理员   | 管理员ID, 密码， 姓名， 联系方式    |
|  借书记录   |  卡号, 借书证号, 借期, 应还还期, 实还日期, 是否已还书, 经手人（管理员ID）   |

> **user表用于储存管理员姓名，密码及联系方式，管理员有对book和card表的修改权限**

> **card表和book表之间由borrow关系集连接，mapping cardinality为many to many**
#### ii）定义表的SQL语句
```MySQL
//book表
CREATE TABLE `book` (
  `bno` char(8) NOT NULL,
  `category` varchar(15) DEFAULT NULL,
  `title` varchar(40) DEFAULT NULL,
  `press` varchar(30) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `author` varchar(20) DEFAULT NULL,
  `price` decimal(7,2) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  PRIMARY KEY (`bno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

//borrow表
CREATE TABLE `borrow` (
  `cno` char(7) NOT NULL,
  `bno` char(8) NOT NULL,
  `borrow_date` datetime NOT NULL,
  `return_date` datetime DEFAULT NULL,
  `true_return` datetime DEFAULT NULL,
  `whether_return` enum('未还','已还','超期归还') NOT NULL,
  PRIMARY KEY (`cno`,`bno`,`borrow_date`),
  KEY `bno` (`bno`),
  CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`bno`) REFERENCES `book` (`bno`),
  CONSTRAINT `borrow_ibfk_2` FOREIGN KEY (`cno`) REFERENCES `card` (`cno`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8

//card表
CREATE TABLE `card` (
  `cno` char(7) NOT NULL,
  `cname` varchar(20) DEFAULT NULL,
  `department` varchar(40) DEFAULT NULL,
  `type` int(1) DEFAULT NULL,
  PRIMARY KEY (`cno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

//User表
CREATE TABLE `user` (
  `id` char(3) NOT NULL,
  `userName` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `tel` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

## 三、实验所采用技术
MySQL Community Server，Windows 10，MySQL Workbench，JDK-14.0.1，IntelliJ Idea，JDBC，Java Swing
