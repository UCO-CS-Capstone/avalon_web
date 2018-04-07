package edu.uco.avalon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentRepository {

    public static List<Equipment> readAllEquipment() throws SQLException {
        List<Equipment> equipmentList = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM equipments eq JOIN lu_equipment_types let ON eq.typeID = let.typeID WHERE eq.isDeleted = 0";
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
        }

        return equipmentList;
    }

    public static Equipment readOneEquipment(int equipmentID) throws SQLException {
        Equipment equipment = new Equipment();

        try (Connection conn = ConnectionManager.getConnection()) {
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

        }
        return equipment;
    }

    public static int createEquipment(Equipment equipment) throws SQLException {
        // Viet was here
        int generatedID = 0;
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "INSERT INTO equipments(name, typeID, lastUpdatedDate, lastUpdatedBy) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, equipment.getName());
            //ps.setString(2, equipment.getType());
            ps.setInt(2, equipment.getTypeID());
            ps.setTimestamp(3, Timestamp.valueOf(equipment.getLastUpdatedDate()));
            ps.setString(4, equipment.getLastUpdatedBy());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        }
        return generatedID;
    }

    public static void updateEquipment(Equipment equipment) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "UPDATE equipments SET name=?, typeID=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE equipmentID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, equipment.getName());
            //ps.setString(2, equipment.getType());
            ps.setInt(2, equipment.getTypeID());
            ps.setTimestamp(3, Timestamp.valueOf(equipment.getLastUpdatedDate()));
            ps.setString(4, equipment.getLastUpdatedBy());
            ps.setInt(5, equipment.getEquipmentID());
            ps.executeUpdate();
        }
    }

    public static void deleteEquipment(int equipmentID) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "UPDATE equipments SET isDeleted=? WHERE equipmentID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setInt(2, equipmentID);
            ps.executeUpdate();
        }
    }

    public static int createEquipmentType(EquipmentType equipmentType) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }

        int generatedID = 0;
        try {
            String query = "INSERT INTO lu_equipment_types(description, lastUpdatedDate, lastUpdatedBy) VALUES ( ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, equipmentType.getDescription());
            ps.setTimestamp(2, Timestamp.valueOf(equipmentType.getLastUpdatedDate()));
            ps.setString(3, equipmentType.getLastUpdatedBy());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } finally {
            conn.close();
        }
        return generatedID;
    }

    /*public static List<EquipmentType> readAllEquipmentType() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }

        List<EquipmentType> equipmentTypeList = new ArrayList<>();

        try {
            String query = "SELECT * FROM lu_equipment_types eq";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                EquipmentType equipmentType = new EquipmentType();
                equipmentType.setEquipmentTypeID(rs.getInt("typeID"));
                equipmentType.setDescription(rs.getString("description"));
                equipmentType.setLastUpdatedDate(rs.getTimestamp("lastUpdatedDate").toLocalDateTime());
                equipmentType.setLastUpdatedBy(rs.getString("lastUpdatedBy"));
                equipmentType.setDeleted(rs.getBoolean("isDeleted"));
                equipmentTypeList.add(equipmentType);
            }
        } finally {
            conn.close();
        }

        return equipmentTypeList;
    }*/
}

