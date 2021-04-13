/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.7.30 : Database - gobang
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 */IF NOT EXISTS`gobang` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `gobang`;

/*Table structure for table `address_user` */

DROP TABLE IF EXISTS `address_user`;

CREATE TABLE `address_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT, # 主键
  `account` varchar(15) NOT NULL, # 用户账号
  `address` varchar(15) NOT NULL, # 用户ip地址 唯一
  `remember` int(1) NOT NULL, # 是否记住密码 1表示记住 0表示不记住
  PRIMARY KEY (`id`),
  UNIQUE KEY `address` (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `address_user` */

/*Table structure for table `chess_record` */

DROP TABLE IF EXISTS `chess_record`;

CREATE TABLE `chess_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT, # 主键
  `black` varchar(15) NOT NULL, # 黑方账号
  `white` varchar(15) NOT NULL, # 白方账号
  `chesstime` datetime NOT NULL, # 棋局结束时间
  `result` char(1) NOT NULL, # 1代表黑胜 2代表白胜 3代表和棋
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `chess_record` */

/*Table structure for table `chess_user` */

DROP TABLE IF EXISTS `chess_user`;

CREATE TABLE `chess_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT, # id 主键
  `account` varchar(15) NOT NULL, # 用户账号 唯一
  `password` varchar(32) NOT NULL, # 用户密码
  `regtime` datetime NOT NULL, # 注册时间
  `score` int(11) NOT NULL, # 用户积分
  `totalnums` int(11) NOT NULL, # 总对局数
  `winnums` int(11) NOT NULL, # 赢棋局数
  `lostnums` int(11) NOT NULL, # 输棋局数
  `drawnums` int(11) NOT NULL, # 和棋局数
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `chess_user` */

/*Table structure for table `sinfo` */

DROP TABLE IF EXISTS `sinfo`;

CREATE TABLE `sinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT, # 主键
  `account` varchar(15) NOT NULL, # 用户账号 唯一
  `address` varchar(15) NOT NULL, # 用户ip地址
  `status` int(1) NOT NULL, # 玩家当前状态 0表示离线 1代表空闲 2代表对战中
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sinfo` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
