package edu.uco.avalon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AllocationRepository {

    public static List<Allocation> readAllAllocation() throws SQLException {

        List<Allocation> allocationList = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection()) {
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

        return allocationList;

    }

    public static Allocation readOneAllocationByEquipmentID(int equipmentID) throws SQLException {
        Allocation allocation = new Allocation();

        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM allocations " +
                    "JOIN (equipments) ON (allocations.equipmentID = equipments.equipmentID) " +
                    "LEFT JOIN (projects) ON (allocations.projectID = projects.projectID) " +
                    "WHERE allocations.equipmentID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, equipmentID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                allocation.setAllocationID(rs.getInt("allocations.allocationID"));
                allocation.setEquipmentID(rs.getInt("allocations.equipmentID"));
                allocation.setDisplayForEquipmentID(rs.getString("equipments.name"));
                allocation.setProjectID(rs.getInt("allocations.projectID"));
                allocation.setDisplayForProjectID(rs.getString("projects.name"));
                allocation.setLastUpdatedDate(rs.getTimestamp("allocations.lastUpdatedDate").toLocalDateTime());
                allocation.setLastUpdatedBy(rs.getString("allocations.lastUpdatedBy"));
                allocation.setDeleted(rs.getBoolean("allocations.isDeleted"));
            }
        }

        return allocation;

    }
    
    public static void addAllocation(int[] selectedAllocation, Allocation allocation) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "UPDATE allocations SET projectID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE equipmentID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, allocation.getProjectID());
            ps.setTimestamp(2, Timestamp.valueOf(allocation.getLastUpdatedDate()));
            ps.setString(3, allocation.getLastUpdatedBy());
            for (int aSelectedAllocation : selectedAllocation) {
                ps.setInt(4, aSelectedAllocation);
                ps.executeUpdate();
            }
        }
    }

    public static void removeAllocationByAllocationID(int[] removedAllocation, Allocation allocation) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "UPDATE allocations SET projectID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE allocationID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setNull(1, Types.INTEGER);
            ps.setTimestamp(2, Timestamp.valueOf(allocation.getLastUpdatedDate()));
            ps.setString(3, allocation.getLastUpdatedBy());
            for (int aRemovedAllocation : removedAllocation) {
                ps.setInt(4, aRemovedAllocation);
                ps.executeUpdate();
            }
        }
    }

    public static void removeAllocationByProject(Project project) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "UPDATE allocations SET projectID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE projectID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setNull(1, Types.INTEGER);
            ps.setTimestamp(2, Timestamp.valueOf(project.getLastUpdatedDate()));
            ps.setString(3, project.getLastUpdatedBy());
            ps.setInt(4, project.getProjectID());
            ps.executeUpdate();
        }
    }

    public static void createAllocation(Allocation allocation) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "INSERT INTO allocations(equipmentID, lastUpdatedDate, lastUpdatedBy)" +
                    " values (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, allocation.getEquipmentID());
            ps.setTimestamp(2, Timestamp.valueOf(allocation.getLastUpdatedDate()));
            ps.setString(3, allocation.getLastUpdatedBy());
            ps.executeUpdate();
        }
    }

    public static void deleteAllocation(Allocation allocation) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "UPDATE allocations SET isDeleted=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE allocationID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setTimestamp(2, Timestamp.valueOf(allocation.getLastUpdatedDate()));
            ps.setString(3, allocation.getLastUpdatedBy());
            ps.setInt(4, allocation.getAllocationID());
            ps.executeUpdate();
        }
    }
}
