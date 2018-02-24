package edu.uco.avalon;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipmentRepository {

    public static List<Equipment> readAllEquipment() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }

        List<Equipment> equipmentList = new ArrayList<>();

        try {
            String query = "SELECT * FROM equipments eq JOIN lu_equipment_types let ON eq.typeID = let.typeID";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setEquipmentID(rs.getInt("equipmentID"));
                equipment.setName(rs.getString("name"));
                equipment.setType(rs.getString("description"));
                equipment.setTypeID(rs.getInt("typeID"));
                equipment.setLastUpdatedDate(rs.getTimestamp("lastUpdatedDate").toLocalDateTime());
                equipment.setLastUpdatedBy(rs.getString("lastUpdatedBy"));
                equipment.setDeleted(rs.getBoolean("isDeleted"));
                equipmentList.add(equipment);
            }
        } finally {
            conn.close();
        }

        return equipmentList;
    }

    public static Equipment readOneEquipment(int equipmentID) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }

        Equipment equipment = new Equipment();

        try {
            String query = "SELECT * FROM equipments eq JOIN lu_equipment_types let ON eq.typeID = let.typeID WHERE equipmentID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, equipmentID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                equipment.setEquipmentID(rs.getInt("equipmentID"));
                equipment.setName(rs.getString("name"));
                equipment.setType(rs.getString("description"));
                equipment.setTypeID(rs.getInt("typeID"));
                equipment.setLastUpdatedDate(rs.getTimestamp("lastUpdatedDate").toLocalDateTime());
                equipment.setLastUpdatedBy(rs.getString("lastUpdatedBy"));
                equipment.setDeleted(rs.getBoolean("isDeleted"));
            }

        } finally {
            conn.close();
        }
        return equipment;
    }

    public static void createEquipment(Equipment equipment) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }

        try {
            String query = "INSERT INTO equipments(name, typeID, lastUpdatedDate, lastUpdatedBy) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, equipment.getName());
            //ps.setString(2, equipment.getType());
            ps.setInt(2,equipment.getTypeID());
            ps.setTimestamp(3, Timestamp.valueOf(equipment.getLastUpdatedDate()));
            ps.setString(4, equipment.getLastUpdatedBy());
            ps.executeUpdate();
        } finally {
            conn.close();
        }
    }

    public static void updateEquipment(Equipment equipment) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }

        try {
            String query = "UPDATE equipments SET name=?, typeID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE equipmentID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, equipment.getName());
            //ps.setString(2, equipment.getType());
            ps.setInt(2,equipment.getTypeID());
            ps.setTimestamp(3, Timestamp.valueOf(equipment.getLastUpdatedDate()));
            ps.setString(4, equipment.getLastUpdatedBy());
            ps.setInt(5, equipment.getEquipmentID());
            ps.executeUpdate();
        } finally {
            conn.close();
        }
    }

    public static void deleteEquipment(int equipmentID) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }

        try {
            String query = "UPDATE equipments SET isDeleted=? WHERE equipmentID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setInt(2, equipmentID);
            ps.executeUpdate();
        } finally {
            conn.close();
        }
    }
}

