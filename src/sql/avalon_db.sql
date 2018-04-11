-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.2.12-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for avalon_db
DROP DATABASE IF EXISTS `avalon_db`;
CREATE DATABASE IF NOT EXISTS `avalon_db` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `avalon_db`;

-- Dumping structure for table avalon_db.admin_assistance
DROP TABLE IF EXISTS `admin_assistance`;
CREATE TABLE IF NOT EXISTS `admin_assistance` (
  `assistanceID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `body` varchar(500) NOT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  `isLocked` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`assistanceID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.admin_assistance: ~0 rows (approximately)
/*!40000 ALTER TABLE `admin_assistance` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_assistance` ENABLE KEYS */;

-- Dumping structure for table avalon_db.allocations
DROP TABLE IF EXISTS `allocations`;
CREATE TABLE IF NOT EXISTS `allocations` (
  `allocationID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `equipmentID` int(10) unsigned NOT NULL,
  `projectID` int(10) unsigned DEFAULT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`allocationID`),
  KEY `FK_allocations_projects` (`projectID`),
  KEY `FK_allocations_equipments` (`equipmentID`),
  CONSTRAINT `FK_allocations_equipments` FOREIGN KEY (`equipmentID`) REFERENCES `equipments` (`equipmentID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_allocations_projects` FOREIGN KEY (`projectID`) REFERENCES `projects` (`projectID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.allocations: ~5 rows (approximately)
/*!40000 ALTER TABLE `allocations` DISABLE KEYS */;
/*!40000 ALTER TABLE `allocations` ENABLE KEYS */;

-- Dumping structure for table avalon_db.equipments
DROP TABLE IF EXISTS `equipments`;
CREATE TABLE IF NOT EXISTS `equipments` (
  `equipmentID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `typeID` int(10) unsigned NOT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`equipmentID`),
  KEY `FK_equipments_lu_equipment_types` (`typeID`),
  CONSTRAINT `FK_equipments_lu_equipment_types` FOREIGN KEY (`typeID`) REFERENCES `lu_equipment_types` (`typeID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.equipments: ~5 rows (approximately)
/*!40000 ALTER TABLE `equipments` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipments` ENABLE KEYS */;

-- Dumping structure for table avalon_db.logs
DROP TABLE IF EXISTS `logs`;
CREATE TABLE IF NOT EXISTS `logs` (
  `logID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event` varchar(500) NOT NULL,
  PRIMARY KEY (`logID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.logs: ~0 rows (approximately)
/*!40000 ALTER TABLE `logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `logs` ENABLE KEYS */;

-- Dumping structure for table avalon_db.lu_equipment_types
DROP TABLE IF EXISTS `lu_equipment_types`;
CREATE TABLE IF NOT EXISTS `lu_equipment_types` (
  `typeID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`typeID`),
  UNIQUE KEY `description` (`description`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.lu_equipment_types: ~3 rows (approximately)
/*!40000 ALTER TABLE `lu_equipment_types` DISABLE KEYS */;
INSERT INTO `lu_equipment_types` (`typeID`, `description`, `lastUpdatedDate`, `lastUpdatedBy`, `isDeleted`) VALUES
	(1, 'Tool', '2018-02-04 18:12:38', 'manual insertion', 0),
	(2, 'Appliance', '2018-02-04 18:13:26', 'manual insertion', 0),
	(3, 'Vehicle', '2018-02-04 18:13:51', 'manual insertion', 0);
/*!40000 ALTER TABLE `lu_equipment_types` ENABLE KEYS */;

-- Dumping structure for table avalon_db.lu_roles
DROP TABLE IF EXISTS `lu_roles`;
CREATE TABLE IF NOT EXISTS `lu_roles` (
  `roleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role` varchar(50) NOT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`roleID`),
  UNIQUE KEY `description` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.lu_roles: ~4 rows (approximately)
/*!40000 ALTER TABLE `lu_roles` DISABLE KEYS */;
INSERT INTO `lu_roles` (`roleID`, `role`, `lastUpdatedDate`, `lastUpdatedBy`, `isDeleted`) VALUES
	(1, 'admin', '2018-02-13 20:08:00', 'manual insertion', 0),
	(2, 'equipmentManager', '2018-02-13 20:08:16', 'manual insertion', 0),
	(3, 'projectOwner', '2018-02-13 20:08:26', 'manual insertion', 0),
	(4, 'maintenanceManager', '2018-02-13 20:08:39', 'manual insertion', 0);
/*!40000 ALTER TABLE `lu_roles` ENABLE KEYS */;

-- Dumping structure for table avalon_db.lu_user_flags
DROP TABLE IF EXISTS `lu_user_flags`;
CREATE TABLE IF NOT EXISTS `lu_user_flags` (
  `flagID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `flag` varchar(50) NOT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`flagID`),
  UNIQUE KEY `flag` (`flag`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.lu_user_flags: ~3 rows (approximately)
/*!40000 ALTER TABLE `lu_user_flags` DISABLE KEYS */;
INSERT INTO `lu_user_flags` (`flagID`, `flag`, `lastUpdatedDate`, `lastUpdatedBy`, `isDeleted`) VALUES
  (0, 'isActive', '2018-02-13 20:02:56', 'manual insertion', 0),
  (1, 'isLocked', '2018-02-13 20:02:56', 'manual insertion', 0),
	(2, 'isDeleted', '2018-02-13 20:03:19', 'manual insertion', 0),
	(3, 'isVerified', '2018-02-13 20:03:38', 'manual insertion', 0);

/*!40000 ALTER TABLE `lu_user_flags` ENABLE KEYS */;

-- Dumping structure for table avalon_db.maintenances
DROP TABLE IF EXISTS `maintenances`;
CREATE TABLE IF NOT EXISTS `maintenances` (
  `maintenanceID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  `equipmentID` int(10) unsigned NOT NULL,
  `cost` decimal(10,2) unsigned NOT NULL,
  `nextMaintenanceDate` datetime NOT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`maintenanceID`),
  KEY `FK_maintenances_equipments` (`equipmentID`),
  CONSTRAINT `FK_maintenances_equipments` FOREIGN KEY (`equipmentID`) REFERENCES `equipments` (`equipmentID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.maintenances: ~9 rows (approximately)
/*!40000 ALTER TABLE `maintenances` DISABLE KEYS */;
/*!40000 ALTER TABLE `maintenances` ENABLE KEYS */;

-- Dumping structure for table avalon_db.milestones
DROP TABLE IF EXISTS `milestones`;
CREATE TABLE IF NOT EXISTS `milestones` (
  `milestoneID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `projectID` int(10) unsigned NOT NULL,
  `cost` decimal(10,2) unsigned DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `deadline` date DEFAULT NULL,
  `dateRecorded` datetime NOT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`milestoneID`),
  KEY `FK_milestones_projects` (`projectID`),
  CONSTRAINT `FK_milestones_projects` FOREIGN KEY (`projectID`) REFERENCES `projects` (`projectID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.milestones: ~0 rows (approximately)
/*!40000 ALTER TABLE `milestones` DISABLE KEYS */;
/*!40000 ALTER TABLE `milestones` ENABLE KEYS */;

-- Dumping structure for table avalon_db.projects
DROP TABLE IF EXISTS `projects`;
CREATE TABLE IF NOT EXISTS `projects` (
  `projectID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `startDate` date DEFAULT NULL,
  `estEndDate` date DEFAULT NULL,
  `actEndDate` date DEFAULT NULL,
  `estCostOverall` decimal(14,2) unsigned DEFAULT NULL,
  `currentCost` decimal(14,2) unsigned DEFAULT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`projectID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.projects: ~1 rows (approximately)
/*!40000 ALTER TABLE `projects` DISABLE KEYS */;
/*!40000 ALTER TABLE `projects` ENABLE KEYS */;

-- Dumping structure for table avalon_db.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `userID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(256) NOT NULL,
  `last_name` varchar(256) NOT NULL,
  `email` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  `flagID` int(10) unsigned DEFAULT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `email` (`email`),
  KEY `FK_users_lu_user_flags` (`flagID`),
  CONSTRAINT `FK_users_lu_user_flags` FOREIGN KEY (`flagID`) REFERENCES `lu_user_flags` (`flagID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.users: ~0 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Dumping structure for table avalon_db.users_roles_xref
DROP TABLE IF EXISTS `users_roles_xref`;
CREATE TABLE IF NOT EXISTS `users_roles_xref` (
  `userID` int(10) unsigned NOT NULL,
  `roleID` int(10) unsigned NOT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`userID`,`roleID`),
  KEY `FK_users_roles_xref_roles` (`roleID`),
  KEY `FK_users_roles_xref_users` (`userID`),
  CONSTRAINT `FK_users_roles_xref_roles` FOREIGN KEY (`roleID`) REFERENCES `lu_roles` (`roleID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_users_roles_xref_users` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `persistent_session`;
CREATE TABLE `persistent_session` (
  `sessionID` varchar(100) NOT NULL,
  `userID` int(10) unsigned NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`sessionID`),
  UNIQUE KEY `persistent_session_sessionID_uindex` (`sessionID`),
  KEY `persistent_session_users_userID_fk` (`userID`),
  CONSTRAINT `persistent_session_users_userID_fk` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table avalon_db.users_roles_xref: ~0 rows (approximately)
/*!40000 ALTER TABLE `users_roles_xref` DISABLE KEYS */;
/*!40000 ALTER TABLE `users_roles_xref` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
