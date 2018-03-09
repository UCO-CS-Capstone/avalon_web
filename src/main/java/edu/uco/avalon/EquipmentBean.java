package edu.uco.avalon;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named(value = "equipmentBean")
@SessionScoped

public class EquipmentBean implements Serializable {

    private List<Equipment> equipmentList;
    private Map<String, Integer> equipmentTypesList;
    private Integer selectedEquipmentTypeValue;
    private int equipmentID;
    private String name;
    private String type;
    private int typeID;

    @PostConstruct
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            equipmentList = EquipmentRepository.readAllEquipment().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
            equipmentTypesList = readAllEquipmentTypes().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        } catch (Exception ex) {
            Logger.getLogger(EquipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public String beforeCreate() {
        this.equipmentID = 0;
        this.name = null;
        this.type = null;
        this.typeID = 0;
        return "/equipment/create";
    }

    public String createEquipment() throws Exception{
        Equipment newEquipment = new Equipment();
        newEquipment.setName(this.name);
        //newEquipment.setType(this.type);
        newEquipment.setTypeID(this.selectedEquipmentTypeValue);
        newEquipment.setLastUpdatedBy("user");
        newEquipment.setLastUpdatedDate(LocalDateTime.now());
        EquipmentRepository.createEquipment(newEquipment);
        equipmentList = EquipmentRepository.readAllEquipment().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return "/equipment/index";
    }

    public String beforeEditing(int equipmentID) throws Exception{
        Equipment equipment = EquipmentRepository.readOneEquipment(equipmentID);
        this.equipmentID = equipment.getEquipmentID();
        this.name = equipment.getName();
        this.type = equipment.getType();
        this.typeID = equipment.getTypeID();
        this.selectedEquipmentTypeValue  = equipment.getTypeID();
        return "/equipment/edit";
    }

    public String editEquipment () throws Exception{
        Equipment oldEquipment = new Equipment();
        oldEquipment.setEquipmentID(this.equipmentID);
        oldEquipment.setName(this.name);
        oldEquipment.setType(this.type);
        oldEquipment.setTypeID(this.selectedEquipmentTypeValue);
        oldEquipment.setLastUpdatedDate(LocalDateTime.now());
        oldEquipment.setLastUpdatedBy("user");
        EquipmentRepository.updateEquipment(oldEquipment);
        equipmentList = EquipmentRepository.readAllEquipment().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return "/equipment/index";
    }

    public void deleteEquipment(int equipmentID) throws Exception{
        EquipmentRepository.deleteEquipment(equipmentID);
        equipmentList = EquipmentRepository.readAllEquipment().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());

    }

    public static Map<String, Integer> readAllEquipmentTypes() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null");
        }

        Map<String, Integer> equipmentTypesList = new LinkedHashMap<>();

        try {
            String query = "SELECT * FROM lu_equipment_types";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                equipmentTypesList.put(rs.getString("description"), rs.getInt("typeID"));
            }
        } finally {
            conn.close();
        }

        return equipmentTypesList;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public Map<String, Integer> getEquipmentTypesList() {
        return equipmentTypesList;
    }

    public Integer getSelectedEquipmentTypeValue() {
        return selectedEquipmentTypeValue;
    }

    public void setSelectedEquipmentTypeValue(Integer selectedEquipmentTypeValue) {
        this.selectedEquipmentTypeValue = selectedEquipmentTypeValue;
    }
}
