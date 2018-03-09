package edu.uco.avalon;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceRepository {

    public static List<Maintenance> readAllMaintenance() throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        List<Maintenance> maintenanceList = new ArrayList<>();

        try {
            String query = "SELECT * FROM maintenances " +
                    "JOIN (equipments) ON (maintenances.equipmentID = equipments.equipmentID)";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Maintenance maintenance = new Maintenance();
                maintenance.setMaintenanceID(rs.getInt("maintenances.maintenanceID"));
				maintenance.setDescription(rs.getString("maintenances.description"));
                maintenance.setEquipmentID(rs.getInt("maintenances.equipmentID"));
                maintenance.setDisplayForEquipmentID(rs.getString("equipments.name"));
                maintenance.setCost(rs.getBigDecimal("maintenances.cost"));
				maintenance.setNextMaintenanceDate(rs.getTimestamp("maintenances.nextMaintenanceDate").toLocalDateTime());
                maintenance.setLastUpdatedDate(rs.getTimestamp("maintenances.lastUpdatedDate").toLocalDateTime());
                maintenance.setLastUpdatedBy(rs.getString("maintenances.lastUpdatedBy"));
                maintenance.setDeleted(rs.getBoolean("maintenances.isDeleted"));
                maintenanceList.add(maintenance);
            }
        }
        finally {
            conn.close();
        }

        return maintenanceList;

    }

    public static void createMaintenance(Maintenance maintenance) throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "INSERT INTO maintenances(description, equipmentID, cost, nextMaintenanceDate, lastUpdatedDate, lastUpdatedBy)" +
                    " values (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, maintenance.getDescription());
            ps.setInt(2, maintenance.getEquipmentID());
			ps.setBigDecimal(3, maintenance.getCost());
			ps.setTimestamp(4, Timestamp.valueOf(maintenance.getNextMaintenanceDate()));
            ps.setTimestamp(5, Timestamp.valueOf(maintenance.getLastUpdatedDate()));
            ps.setString(6, maintenance.getLastUpdatedBy());
            ps.executeUpdate();
        }
        finally {
            conn.close();
        }

    }

	public static void updateMaintenance(Maintenance maintenance) throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "UPDATE maintenances SET description=?, equipmentID=?, cost=?, nextMaintenanceDate=?, " +
                    "lastUpdatedDate=?, lastUpdatedBy=? WHERE maintenanceID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, maintenance.getDescription());
            ps.setInt(2, maintenance.getEquipmentID());
			ps.setBigDecimal(3, maintenance.getCost());
			ps.setTimestamp(4, Timestamp.valueOf(maintenance.getNextMaintenanceDate()));
            ps.setTimestamp(5, Timestamp.valueOf(maintenance.getLastUpdatedDate()));
            ps.setString(6, maintenance.getLastUpdatedBy());
            ps.setInt(7, maintenance.getMaintenanceID());
            ps.executeUpdate();
        }
        finally {
            conn.close();
        }

    }
	
    public static void deleteMaintenanceByMaintenanceID(Maintenance maintenance) throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "UPDATE maintenances SET isDeleted=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE maintenanceID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setTimestamp(2, Timestamp.valueOf(maintenance.getLastUpdatedDate()));
            ps.setString(3, maintenance.getLastUpdatedBy());
            ps.setInt(4, maintenance.getMaintenanceID());
            ps.executeUpdate();
        }
        finally {
            conn.close();
        }

    }

    public static void deleteMaintenanceByEquipmentID(Maintenance maintenance) throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "UPDATE maintenances SET isDeleted=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE equipmentID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setTimestamp(2, Timestamp.valueOf(maintenance.getLastUpdatedDate()));
            ps.setString(3, maintenance.getLastUpdatedBy());
            ps.setInt(4, maintenance.getEquipmentID());
            ps.executeUpdate();
        }
        finally {
            conn.close();
        }

    }

    public static LocalDateTime getLatestNextMaintenanceDateForEquipment(int equipmentID) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        LocalDateTime latestMaintenanceDate = null;

        try {
            String query = "SELECT * FROM allocations JOIN maintenances ON allocations.equipmentID=maintenances.equipmentID WHERE allocations.equipmentID=? AND maintenances.isDeleted=false ORDER BY maintenances.nextMaintenanceDate DESC";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, equipmentID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                latestMaintenanceDate = rs.getTimestamp("maintenances.nextMaintenanceDate").toLocalDateTime();
            }
        }
        finally {
            conn.close();
        }

        return latestMaintenanceDate;

    }

}