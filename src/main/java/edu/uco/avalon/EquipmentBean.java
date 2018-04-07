package edu.uco.avalon;

import net.bootsfaces.component.fullCalendar.FullCalendarEventList;
import org.omnifaces.util.Faces;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.*;
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

    // Viet was here
    private List<Maintenance> maintenanceList;
    private int maintenanceID;
    private String description;
    private String cost;
    private Date nextMaintenanceDate;
    private boolean isEditingMaintenance;
    private String myStyle;

    public String getMyStyle() {
        return myStyle;
    }

    public void setMyStyle(String myStyle) {
        this.myStyle = myStyle;
    }

    public LocalDateTime getLatestMaintenanceDateForEquipment(Equipment equipment) throws SQLException {
        LocalDateTime dateToCompare = MaintenanceRepository.
                getLatestNextMaintenanceDateForEquipment(equipment.getEquipmentID());
        if (dateToCompare != null) {
            if (dateToCompare.isAfter(LocalDateTime.now())) {
                this.myStyle = "text-primary";
            }
            else {
                this.myStyle = "text-danger";
            }
            return dateToCompare;
        }
        else {
            return null;
        }
    }

    public String getProjectAllocatedTo(Equipment equipment) throws SQLException {
        return AllocationRepository.readOneAllocationByEquipmentID(equipment.getEquipmentID()).getDisplayForProjectID();
    }

    @PostConstruct
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            equipmentList = EquipmentRepository.readAllEquipment().stream().
                    filter(x -> !x.isDeleted()).collect(Collectors.toList());
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

    public List<Maintenance> getMaintenanceList() { return  maintenanceList; }

    public boolean isEditingMaintenance() {
        return isEditingMaintenance;
    }

    public void prepareEditingMaintenance(Maintenance maintenance) {
        this.maintenanceID = maintenance.getMaintenanceID();
        this.description = maintenance.getDescription();
        this.cost = String.valueOf(maintenance.getCost());
        this.nextMaintenanceDate = java.sql.Timestamp.valueOf(maintenance.getNextMaintenanceDate());
        this.isEditingMaintenance = true;
    }

    public String beforeCreate() {
        this.equipmentID = 0;
        this.name = null;
        this.type = null;
        this.typeID = 0;
        return "/equipment/create";
    }

    public String beforeCreateType() {
        this.typeID = 0;
        this.description = null;
        return "/equipment/createType";
    }

    public String createEquipment() throws Exception{
        Equipment newEquipment = new Equipment();
        newEquipment.setName(this.name);
        //newEquipment.setType(this.type);
        newEquipment.setTypeID(this.selectedEquipmentTypeValue);
        newEquipment.setLastUpdatedBy("user");
        newEquipment.setLastUpdatedDate(LocalDateTime.now());
        int generatedID = EquipmentRepository.createEquipment(newEquipment);
        // Viet was here
        Allocation allocation = new Allocation();
        allocation.setEquipmentID(generatedID);
        allocation.setLastUpdatedDate(LocalDateTime.now());
        allocation.setLastUpdatedBy("user");
        AllocationRepository.createAllocation(allocation);
        equipmentList = EquipmentRepository.readAllEquipment().stream().
                filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return "/equipment/index";
    }

    public String equipmentDetail(int equipmentID) throws Exception {
        Equipment equipment = EquipmentRepository.readOneEquipment(equipmentID);
        this.equipmentID = equipment.getEquipmentID();
        this.name = equipment.getName();
        this.type = equipment.getType();
        this.typeID = equipment.getTypeID();
        this.description = null;
        this.cost = null;
        this.nextMaintenanceDate = null;
        this.isEditingMaintenance = false;
        maintenanceList = MaintenanceRepository.readAllMaintenance().stream().
                filter(x -> !x.isDeleted() && x.getEquipmentID() == equipmentID).collect(Collectors.toList());
        return "/equipment/detail";
    }

    public void createMaintenance() throws Exception {
        Maintenance maintenance = new Maintenance();
        maintenance.setEquipmentID(this.equipmentID);
        maintenance.setDescription(this.description);
        maintenance.setCost(Helpers.parse(this.cost, Locale.US));
        maintenance.setNextMaintenanceDate(new java.sql.Timestamp(this.nextMaintenanceDate.
                getTime()).toLocalDateTime());
        maintenance.setLastUpdatedDate(LocalDateTime.now());
        maintenance.setLastUpdatedBy("user");
        MaintenanceRepository.createMaintenance(maintenance);
        this.description = null;
        this.cost = null;
        this.nextMaintenanceDate = null;
        maintenanceList = MaintenanceRepository.readAllMaintenance().stream().
                filter(x -> !x.isDeleted() && x.getEquipmentID() == equipmentID).collect(Collectors.toList());
    }

    public void editMaintenance() throws Exception {
        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenanceID(this.maintenanceID);
        maintenance.setEquipmentID(this.equipmentID);
        maintenance.setDescription(this.description);
        maintenance.setCost(Helpers.parse(this.cost, Locale.US));
        maintenance.setNextMaintenanceDate(new java.sql.Timestamp(this.nextMaintenanceDate.
                getTime()).toLocalDateTime());
        maintenance.setLastUpdatedDate(LocalDateTime.now());
        maintenance.setLastUpdatedBy("user");
        MaintenanceRepository.updateMaintenance(maintenance);
        this.description = null;
        this.cost = null;
        this.nextMaintenanceDate = null;
        this.isEditingMaintenance = false;
        maintenanceList = MaintenanceRepository.readAllMaintenance().stream().
                filter(x -> !x.isDeleted() && x.getEquipmentID() == equipmentID).collect(Collectors.toList());
    }

    public void cancelEditMaintenance() {
        this.description = null;
        this.cost = null;
        this.nextMaintenanceDate = null;
        this.isEditingMaintenance = false;
    }

    public void deleteMaintenance(int maintenanceID) throws Exception {
        Maintenance maintenance= new Maintenance();
        maintenance.setMaintenanceID(maintenanceID);
        maintenance.setLastUpdatedDate(LocalDateTime.now());
        maintenance.setLastUpdatedBy("user");
        MaintenanceRepository.deleteMaintenanceByMaintenanceID(maintenance);
        maintenanceList = MaintenanceRepository.readAllMaintenance().stream().
                filter(x -> !x.isDeleted() && x.getEquipmentID() == equipmentID).collect(Collectors.toList());
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
        equipmentList = EquipmentRepository.readAllEquipment().stream().
                filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return "/equipment/index";
    }

    public void deleteEquipment(int equipmentID) throws Exception{
        // Viet was here
        System.out.println(equipmentID);
        Allocation allocation = new Allocation();
        allocation.setAllocationID(AllocationRepository.readOneAllocationByEquipmentID(equipmentID).getAllocationID());
        allocation.setLastUpdatedDate(LocalDateTime.now());
        allocation.setLastUpdatedBy("user");
        AllocationRepository.deleteAllocation(allocation);
        Maintenance maintenance = new Maintenance();
        maintenance.setEquipmentID(equipmentID);
        maintenance.setLastUpdatedDate(LocalDateTime.now());
        maintenance.setLastUpdatedBy("user");
        MaintenanceRepository.deleteMaintenanceByEquipmentID(maintenance);
        EquipmentRepository.deleteEquipment(equipmentID);
        equipmentList = EquipmentRepository.readAllEquipment().stream().
                filter(x -> !x.isDeleted()).collect(Collectors.toList());
    }

    public static Map<String, Integer> readAllEquipmentTypes() throws SQLException {

        Map<String, Integer> equipmentTypesList = new LinkedHashMap<>();

        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM lu_equipment_types";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                equipmentTypesList.put(rs.getString("description"), rs.getInt("typeID"));
            }
        }

        return equipmentTypesList;

    }

    public String createEquipmentType() throws Exception{
        EquipmentType newEquipmentType = new EquipmentType();
        newEquipmentType.setDescription(this.description);
        newEquipmentType.setLastUpdatedBy("user");
        newEquipmentType.setLastUpdatedDate(LocalDateTime.now());
        int generatedID = EquipmentRepository.createEquipmentType(newEquipmentType);
        equipmentTypesList = readAllEquipmentTypes().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return "/equipment/index";
    }


    public static void toCSV() throws IOException, SQLException {
        CellProcessor[] processors = new CellProcessor[] {
                new UniqueHashCode(), // projectID (must be unique)
                new NotNull(), // name
                new Optional(), // startDate
                new Optional(), // estEndDate
                new Optional(), // actEndDate
        };
        // the header elements are used to map the bean values to each column (names must match)
        final String[] headers = new String[] {
                "equipmentID",
                "name",
                "type",
                "lastUpdatedDate",
                "lastUpdatedBy"//,
                //"isDeleted"
        };
        List<Equipment> equipment = EquipmentRepository.readAllEquipment();
        InputStream data = CSVBuilder.create(processors, headers, equipment);
        String filename = CSVBuilder.filename("equipment");
        Faces.sendFile(data, filename, true);
    }

    public FullCalendarEventList getCalendarEventList() throws SQLException {
        FullCalendarEventList list = new FullCalendarEventList();
        for (Maintenance maintenance : this.maintenanceList) {
            Date date = Date.from(maintenance.getNextMaintenanceDate().atZone(ZoneId.systemDefault()).toInstant());
            CalendarEntry entry = new CalendarEntry(maintenance.getMaintenanceID(), maintenance.getDescription(), date);
            list.getList().add(entry);
        }
        return list;
    }

    public String getCalendarEventListJson() throws SQLException {
        return this.getCalendarEventList().toJson();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Date getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(Date nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

}
