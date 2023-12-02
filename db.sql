CREATE DATABASE IF NOT EXISTS `mydb`;
USE mydb;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
                           `account_id` varchar(50) NOT NULL,
                           `full_name` varchar(50) NOT NULL,
                           `password` varchar(50) NOT NULL,
                           `email` varchar(50) DEFAULT NULL,
                           `phone` varchar(50) DEFAULT NULL,
                           `status` tinyint(4) NOT NULL,
                           PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


BEGIN;
INSERT INTO `account` (`account_id`, `full_name`, `password`, `email`, `phone`, `status`) VALUES ('1', 'tuyen', '123123', 'tuyen@gmail.com', '111', 1);
COMMIT;


DROP TABLE IF EXISTS `grant_access`;
CREATE TABLE `grant_access` (
                                `role_id` varchar(50) NOT NULL,
                                `account_id` varchar(50) NOT NULL,
                                `is_grant` enum('1','0','-1') NOT NULL,
                                `note` varchar(250) DEFAULT '',
                                PRIMARY KEY (`role_id`,`account_id`),
                                KEY `account_grant` (`account_id`),
                                CONSTRAINT `account_grant` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                CONSTRAINT `role_grant` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


BEGIN;
INSERT INTO `grant_access` (`role_id`, `account_id`, `is_grant`, `note`) VALUES ('asdas', '1', '1', 'asdasd');
COMMIT;

DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                       `account_id` varchar(50) NOT NULL,
                       `login_time` datetime NOT NULL,
                       `logout_time` datetime DEFAULT NULL,
                       `notes` varchar(250) NOT NULL DEFAULT '',
                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=latin1 COMMENT='ghi logs';


DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
                        `role_id` varchar(50) NOT NULL,
                        `role_name` varchar(50) NOT NULL,
                        `description` varchar(50) DEFAULT NULL,
                        `status` tinyint(4) NOT NULL,
                        PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


BEGIN;
INSERT INTO `role` (`role_id`, `role_name`, `description`, `status`) VALUES ('asdas', '1', 'dasd', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

