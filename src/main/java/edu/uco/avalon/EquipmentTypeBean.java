package edu.uco.avalon;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named(value = "equipmentTypeBean")
@RequestScoped
public class EquipmentTypeBean implements Serializable {

   /* private Map<String, Integer> equipmentTypesList;
    private Integer selectedEquipmentTypeValue;

    @PostConstruct
    public void init() {
        try {
            equipmentTypesList = readAllEquipmentTypes().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        }
        catch (Exception ex) {
            Logger.getLogger(EquipmentTypeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void test() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("form:test", new FacesMessage(FacesMessage.SEVERITY_INFO,
                "The selected equipment ID is " + selectedEquipmentTypeValue, null));
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

    }*/

}
