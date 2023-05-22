drop database if exists metrotransactions;
create database metrotransactions;
use metrotransactions;

CREATE TABLE `customer` (
  `CUSTOMER_ID` varchar(4) NOT NULL,
  `CUSTOMER_NAME` varchar(30) NOT NULL,
  PRIMARY KEY (`CUSTOMER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `product` (
  `PRODUCT_ID` varchar(6) NOT NULL,
  `PRODUCT_NAME` varchar(30) NOT NULL,
  `PRICE` double DEFAULT NULL,
  PRIMARY KEY (`PRODUCT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `store` (
  `STORE_ID` varchar(4) NOT NULL,
  `STORE_NAME` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`STORE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `supplier` (
  `SUPPLIER_ID` varchar(5) NOT NULL,
  `SUPPLIER_NAME` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`SUPPLIER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `time` (
  `TIME_ID` varchar(8) NOT NULL,
  `DATE` int DEFAULT NULL,
  `MONTH` int DEFAULT NULL,
  `QUARTER` int DEFAULT NULL,
  `YEAR` int DEFAULT NULL,
  `DAY` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`TIME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `metrotransactions` (
  `SALE_ID` int NOT NULL AUTO_INCREMENT,
  `PRODUCT_ID` varchar(6) DEFAULT NULL,
  `SUPPLIER_ID` varchar(45) DEFAULT NULL,
  `CUSTOMER_ID` varchar(4) DEFAULT NULL,
  `STORE_ID` varchar(4) DEFAULT NULL,
  `TIME_ID` varchar(8) DEFAULT NULL,
  `QUANTITY` double DEFAULT NULL,
  `SALES` double DEFAULT NULL,
  PRIMARY KEY (`SALE_ID`),
  UNIQUE KEY `SALE_ID_UNIQUE` (`SALE_ID`),
  KEY `product_fk_idx` (`PRODUCT_ID`),
  KEY `customer_fk_idx` (`CUSTOMER_ID`),
  KEY `store_fk_idx` (`STORE_ID`),
  KEY `time_fk_idx` (`TIME_ID`),
  KEY `supplier_fk_idx` (`SUPPLIER_ID`),
  CONSTRAINT `customer_fk` FOREIGN KEY (`CUSTOMER_ID`) REFERENCES `customer` (`CUSTOMER_ID`),
  CONSTRAINT `product_fk` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `product` (`PRODUCT_ID`),
  CONSTRAINT `store_fk` FOREIGN KEY (`STORE_ID`) REFERENCES `store` (`STORE_ID`),
  CONSTRAINT `supplier_fk` FOREIGN KEY (`SUPPLIER_ID`) REFERENCES `supplier` (`SUPPLIER_ID`),
  CONSTRAINT `time_fk` FOREIGN KEY (`TIME_ID`) REFERENCES `time` (`TIME_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=160319 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;