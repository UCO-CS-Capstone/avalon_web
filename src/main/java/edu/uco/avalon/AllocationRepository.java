package edu.uco.avalon;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AllocationRepository {

    public static List<Allocation> readAllAllocation() throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        List<Allocation> allocationList = new ArrayList<>();

        try {
            String query = "SELECT * FROM allocations " +
                    "JOIN (equipments) ON (allocations.equipmentID = equipments.equipmentID) " +
                    "LEFT JOIN (projects) ON (allocations.projectID = projects.projectID)";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Allocation allocation = new Allocation();
                allocation.setAllocationID(rs.getInt("allocations.allocationID"));
                allocation.setEquipmentID(rs.getInt("allocations.equipmentID"));
                allocation.setDisplayForEquipmentID(rs.getString("equipments.name"));
                allocation.setProjectID(rs.getInt("allocations.projectID"));
                allocation.setDisplayForProjectID(rs.getString("projects.name"));
                allocation.setLastUpdatedDate(rs.getTimestamp("allocations.lastUpdatedDate").toLocalDateTime());
                allocation.setLastUpdatedBy(rs.getString("allocations.lastUpdatedBy"));
                allocation.setDeleted(rs.getBoolean("allocations.isDeleted"));
                allocationList.add(allocation);
            }
        }
        finally {
            conn.close();
        }

        return allocationList;

    }

    public static void addAllocation(int[] selectedAllocation, Allocation allocation) throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "UPDATE allocations SET projectID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE equipmentID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, allocation.getProjectID());
            ps.setTimestamp(2, Timestamp.valueOf(allocation.getLastUpdatedDate()));
            ps.setString(3, allocation.getLastUpdatedBy());
            for (int i = 0; i < selectedAllocation.length; i++) {
                ps.setInt(4, selectedAllocation[i]);
                ps.executeUpdate();
            }
        }
        finally {
            conn.close();
        }

    }

    public static void removeAllocationByAllocationID(int[] removedAllocation, Allocation allocation) throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "UPDATE allocations SET projectID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE allocationID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setNull(1, Types.INTEGER);
            ps.setTimestamp(2, Timestamp.valueOf(allocation.getLastUpdatedDate()));
            ps.setString(3, allocation.getLastUpdatedBy());
            for (int i = 0; i < removedAllocation.length; i++) {
                ps.setInt(4, removedAllocation[i]);
                ps.executeUpdate();
            }
        }
        finally {
            conn.close();
        }

    }

    public static void removeAllocationByProject(Project project) throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "UPDATE allocations SET projectID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE projectID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setNull(1, Types.INTEGER);
            ps.setTimestamp(2, Timestamp.valueOf(project.getLastUpdatedDate()));
            ps.setString(3, project.getLastUpdatedBy());
            ps.setInt(4, project.getProjectID());
            ps.executeUpdate();
        }
        finally {
            conn.close();
        }

    }

    public static void createAllocation(Allocation allocation) throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "INSERT INTO allocations(equipmentID, lastUpdatedDate, lastUpdatedBy)" +
                    " values (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, allocation.getEquipmentID());
            ps.setTimestamp(2, Timestamp.valueOf(allocation.getLastUpdatedDate()));
            ps.setString(3, allocation.getLastUpdatedBy());
            ps.executeUpdate();
        }
        finally {
            conn.close();
        }

    }

    public static void deleteAllocation(Allocation allocation) throws SQLException {

//        if (ds == null) {
//            throw new SQLException("ds is null.");
//        }
//        Connection conn = ds.getConnection();
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        try {
            String query = "UPDATE allocations SET isDeleted=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE allocationID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setTimestamp(2, Timestamp.valueOf(allocation.getLastUpdatedDate()));
            ps.setString(3, allocation.getLastUpdatedBy());
            ps.setInt(4, allocation.getAllocationID());
            ps.executeUpdate();
        }
        finally {
            conn.close();
        }

    }


}
